package com.example.pkce.controllers;

import com.example.pkce.entities.Client;
import com.example.pkce.entities.Role;
import com.example.pkce.entities.RoleTemplate;
import com.example.pkce.entities.User;
import com.example.pkce.exceptions.ClientNotFoundException;
import com.example.pkce.exceptions.GenericBadRequestError;
import com.example.pkce.exceptions.GenericUnauthorizedException;
import com.example.pkce.models.ManagerResponse;
import com.example.pkce.repositories.ClientRepository;
import com.example.pkce.repositories.RoleRepository;
import com.example.pkce.repositories.UserRepository;
import com.example.pkce.services.ClientService;
import com.example.pkce.services.RoleTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RolesController {

  private static final List<String> DEFAULT_ROLES = Arrays.asList("MANAGER", "ADMIN");

  @Autowired private RoleTemplateService roleTemplateService;
  @Autowired private ClientService clientService;
  @Autowired private RoleRepository roleRepository;
  @Autowired private ClientRepository clientRepository;
  @Autowired private UserRepository userRepository;

  @PostMapping("/roles/add")
  public ResponseEntity<?> addRoleTemplate(
      @RequestParam String clientId, @RequestParam String role, Authentication authentication)
      throws ClientNotFoundException, GenericBadRequestError, GenericUnauthorizedException {

    role = role.toUpperCase();

    if (DEFAULT_ROLES.contains(role)) {
      throw new GenericBadRequestError("cannot_be_default_role");
    }

    User user = User.getUserFromAuthentication(authentication);
    checkIfAuthorized(clientId, user);

    RoleTemplate roleTemplate = new RoleTemplate(clientId,role);

    roleTemplate = roleTemplateService.save(roleTemplate);

    return new ResponseEntity<>(roleTemplate, HttpStatus.OK);
  }

  @PostMapping("/roles/update")
  public ResponseEntity<?> updateRoleTemplate(
      @RequestParam int roleTemplateId, @RequestParam String role, Authentication authentication)
      throws ClientNotFoundException, GenericBadRequestError, GenericUnauthorizedException {

    role = role.toUpperCase();

    if (DEFAULT_ROLES.contains(role)) {
      throw new GenericBadRequestError("cannot_be_default_role");
    }

    User user = User.getUserFromAuthentication(authentication);

    RoleTemplate roleTemplate = roleTemplateService.loadTemplateById(roleTemplateId);

    if (roleTemplate.isDefaultRole()) throw new GenericBadRequestError("cant_alter_default_roles");

    checkIfAuthorized(roleTemplate.getClientId(), user);

    roleTemplate.setRole(role);
    roleTemplate = roleTemplateService.save(roleTemplate);

    return new ResponseEntity<>(roleTemplate, HttpStatus.OK);
  }

  @PostMapping("/roles/delete")
  @Transactional
  public ResponseEntity<?> deleteRoleTemplate(
      @RequestParam int roleTemplateId, Authentication authentication)
      throws ClientNotFoundException, GenericBadRequestError, GenericUnauthorizedException {

    User user = User.getUserFromAuthentication(authentication);

    RoleTemplate roleTemplate = roleTemplateService.loadTemplateById(roleTemplateId);

    if (roleTemplate.isDefaultRole()) throw new GenericBadRequestError("cant_alter_default_roles");

    checkIfAuthorized(roleTemplate.getClientId(), user);

    roleTemplateService.delete(roleTemplate);

    roleRepository.deleteAllByRoleTemplateId(roleTemplate.getId());

    return new ResponseEntity<>("deleted", HttpStatus.OK);
  }

  @GetMapping("/roles/get")
  public ResponseEntity<?> getRoles(
      Authentication authentication,
      @RequestParam boolean withDefault,
      @RequestParam(required = false) Integer secondUserId) {

    User user = User.getUserFromAuthentication(authentication);

    List<Client> clientsByOwner = clientRepository.findAllByOwner(user.getId());

    List<Client> clientsByManager =
        roleRepository.findAllByUserId(user.getId()).stream()
            .map(role -> roleTemplateService.loadTemplateById(role.getRoleTemplateId()))
            .filter(roleTemplate -> roleTemplate.getRole().equals("MANAGER"))
            .map(roleTemplate -> clientService.loadClientByClientId(roleTemplate.getClientId()))
            .filter(client -> client.getOwner() != user.getId())
            .collect(Collectors.toList());

    List<Client> clients =
        Stream.concat(clientsByOwner.stream(), clientsByManager.stream())
            .collect(Collectors.toList());

    List<ManagerResponse> managerResponseList = new ArrayList<>();

    clients.forEach(
        client -> {
          ManagerResponse managerResponse = new ManagerResponse(client);

          managerResponse.setRolesTemplates(
              roleTemplateService.loadAllByClientId(client.getClientId()).stream()
                  .filter(roleTemplate -> !roleTemplate.isDefaultRole() || withDefault)
                  .peek(
                      roleTemplate -> {
                        if (secondUserId != null) {
                          roleTemplate.setActive(
                              roleRepository.existsByUserIdAndRoleTemplateId(
                                  secondUserId, roleTemplate.getId()));
                        }
                      })
                  .collect(Collectors.toList()));

          managerResponseList.add(managerResponse);
        });

    return new ResponseEntity<>(managerResponseList, HttpStatus.OK);
  }

  @PostMapping("/roles/assign")
  public ResponseEntity<?> roleAssign(
      @RequestParam int userIdToAssign,
      @RequestParam int roleTemplateId,
      Authentication authentication)
      throws ClientNotFoundException, GenericUnauthorizedException {

    User user = User.getUserFromAuthentication(authentication);

    RoleTemplate roleTemplate = roleTemplateService.loadTemplateById(roleTemplateId);

    checkIfAuthorized(roleTemplate.getClientId(), user);

    checkBeforeRolesAssigmentAndRevocation(roleTemplate, user, userIdToAssign);

    if (roleRepository.existsByUserIdAndRoleTemplateId(userIdToAssign, roleTemplate.getId())) {
      return new ResponseEntity<>("already_assigned", HttpStatus.OK);
    } else {
      Role role = new Role(userIdToAssign,roleTemplate.getId(),roleTemplate.getClientId());
      roleRepository.save(role);
      return new ResponseEntity<>("assigned", HttpStatus.OK);
    }
  }

  @PostMapping("/roles/revoke")
  public ResponseEntity<?> roleRevoke(
      @RequestParam int userIdToAssign,
      @RequestParam int roleTemplateId,
      Authentication authentication)
      throws ClientNotFoundException, GenericUnauthorizedException {

    User user = User.getUserFromAuthentication(authentication);

    RoleTemplate roleTemplate = roleTemplateService.loadTemplateById(roleTemplateId);

    checkIfAuthorized(roleTemplate.getClientId(), user);

    checkBeforeRolesAssigmentAndRevocation(roleTemplate, user, userIdToAssign);

    if (!roleRepository.existsByUserIdAndRoleTemplateId(userIdToAssign, roleTemplate.getId())) {
      return new ResponseEntity<>("already_revoked", HttpStatus.OK);
    } else {
      roleRepository
          .findByUserIdAndRoleTemplateId(userIdToAssign, roleTemplate.getId())
          .ifPresent(role -> roleRepository.delete(role));

      return new ResponseEntity<>("revoked", HttpStatus.OK);
    }
  }

  private void checkIfAuthorized(String clientId, User user)
      throws ClientNotFoundException, GenericUnauthorizedException {

    Client client = clientService.loadClientByClientId(clientId);

    RoleTemplate managerRoleTemplate =
        roleTemplateService.loadByClientIdAndRole(clientId, "MANAGER");

    if (!(client.getOwner() == user.getId()
        || roleRepository.existsByUserIdAndRoleTemplateId(
            user.getId(), managerRoleTemplate.getId()))) {
      throw new GenericUnauthorizedException();
    }
  }

  private void checkBeforeRolesAssigmentAndRevocation(
      RoleTemplate roleTemplate, User user, int userIdToAssign) {

    Client client = clientService.loadClientByClientId(roleTemplate.getClientId());

    if (roleTemplate.isOwnerOnly()) {
      if (client.getOwner() != user.getId()) {
        throw new GenericBadRequestError("only_owner_can_assign_this_role");
      }
    }

    if (!userRepository.existsById(userIdToAssign)) {
      throw new GenericBadRequestError("user_does_not_exists");
    }
  }
}
