package com.example.pkce.controllers;

import com.example.pkce.entities.Client;
import com.example.pkce.entities.RoleTemplate;
import com.example.pkce.entities.User;
import com.example.pkce.exceptions.ClientNotFoundException;
import com.example.pkce.exceptions.GenericBadRequestError;
import com.example.pkce.exceptions.GenericUnauthorizedException;
import com.example.pkce.models.ClientModel;
import com.example.pkce.repositories.ClientRepository;
import com.example.pkce.repositories.RoleRepository;
import com.example.pkce.repositories.RoleTemplateRepository;
import com.example.pkce.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleTemplateRepository roleTemplateRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private Environment env;

    private List<String> admins;

    @PostConstruct
    private void loadAdmins() {
        admins = Arrays.asList(Objects.requireNonNull(env.getProperty("app.admins")).split(","));
    }

    @PostMapping("/createClient")
    public ResponseEntity<?> createClient(@RequestParam String name, Authentication authentication)
            throws GenericBadRequestError, GenericUnauthorizedException {


        User user = User.getUserFromAuthentication(authentication);

        if (clientRepository.existsByOwnerAndName(user.getId(), name)) {
            throw new GenericBadRequestError("already_exists");
        }

        if (!admins.contains(user.getEmail())) {
            throw new GenericUnauthorizedException();
        }

        Client client = new Client();

        client.setClientId(UUID.randomUUID().toString());

        String password = UUID.randomUUID().toString();
        client.setClientSecretPlain(password);
        client.setClientSecret(passwordEncoder.encode(password));

        client.setName(name);
        client.setOwner(user.getId());

        client = clientRepository.save(client);

        // Add default roles
        List<RoleTemplate> roleTemplates = new ArrayList<>();
        roleTemplates.add(new RoleTemplate(client.getClientId(), true, true, "MANAGER"));
        roleTemplates.add(new RoleTemplate(client.getClientId(), true, false, "ADMIN"));

        roleTemplateRepository.saveAll(roleTemplates);

        return new ResponseEntity<>(new ClientModel(client), HttpStatus.OK);
    }

    // Get all clients
    @GetMapping("/clients")
    public ResponseEntity<?> clients(Authentication authentication) {

        User user = User.getUserFromAuthentication(authentication);

        List<Client> clients = clientService.findAllOfUserClients(user.getId());

        List<ClientModel> clientModelList =
                clients.stream().map(ClientModel::new).collect(Collectors.toList());

        return new ResponseEntity<>(clientModelList, HttpStatus.OK);
    }

    // Get single client
    @GetMapping("/client")
    public ResponseEntity<?> client(@RequestParam String id, Authentication authentication)
            throws ClientNotFoundException, GenericUnauthorizedException {

        User user = User.getUserFromAuthentication(authentication);

        Client client = clientService.loadClientByClientId(id);
        checkIfOwnerAndAdmin(user, client);

        return new ResponseEntity<>(new ClientModel(client), HttpStatus.OK);
    }

    @PostMapping("/updateClient")
    public ResponseEntity<?> updateClient(
            @ModelAttribute ClientModel clientModel, Authentication authentication)
            throws ClientNotFoundException, GenericUnauthorizedException, GenericBadRequestError {

        try {

            User user = User.getUserFromAuthentication(authentication);
            Client client = clientService.loadClientByClientId(clientModel.getClientId());

            checkIfOwnerAndAdmin(user, client);


            //Check if all properties all set correctly
            List<Client> otherClients =
                    clientRepository.findAllByOwnerAndClientIdIsNot(user.getId(), client.getClientId());

            List<String> clientNames =
                    otherClients.stream().map(Client::getName).collect(Collectors.toList());

            if (clientNames.contains(clientModel.getName())) {
                throw new GenericBadRequestError("already_exists");
            }

            if (!clientModel.isAuthenticationMethodBasic()
                    && !clientModel.isAuthenticationMethodNone()
                    && !client.isAuthenticationMethodPost()) {
                throw new GenericBadRequestError("no_authentication_method");
            }

            if (!client.isAuthorizationGrantTypeClientCredentials()
                    && !client.isAuthorizationGrantTypePassword()
                    && !client.isAuthorizationGrantTypeImplicit()
                    && !client.isAuthorizationGrantTypeAuthorizationCode()) {
                throw new GenericBadRequestError("no_grant_type");
            }

            if (clientModel.getRedirectUrls().isEmpty()) {
                throw new GenericBadRequestError("no_redirect_urls");
            }

            try {
                Duration.parse(clientModel.getAccessTokenTimeToLive());
            } catch (DateTimeParseException e) {
                throw new GenericBadRequestError("access_token_wrong_duration_format");
            }

            try {
                Duration.parse(clientModel.getRefreshTokenTimeToLive());
            } catch (DateTimeParseException e) {
                throw new GenericBadRequestError("refresh_token_wrong_duration_format");
            }

            client.setFromClientModel(clientModel);

            clientRepository.save(client);

            return new ResponseEntity<>("updated", HttpStatus.OK);

        } catch (NullPointerException e) {
            throw new GenericBadRequestError("property_missing");
        }
    }

    @PostMapping("/clientDelete")
    @Transactional
    public ResponseEntity<?> clientDelete(
            @RequestParam String clientId, Authentication authentication)
            throws ClientNotFoundException, GenericUnauthorizedException {

        User user = User.getUserFromAuthentication(authentication);
        Client client = clientService.loadClientByClientId(clientId);

        checkIfOwnerAndAdmin(user, client);

        roleTemplateRepository.deleteAllByClientId(client.getClientId());
        roleRepository.deleteAllByClientId(client.getClientId());

        clientRepository.delete(client);

        return new ResponseEntity<>("client_deleted", HttpStatus.OK);
    }

    @PostMapping("/clientSecretRefresh")
    @Transactional
    public ResponseEntity<?> clientSecretRefresh(
            @RequestParam String clientId, Authentication authentication)
            throws ClientNotFoundException, GenericUnauthorizedException {

        User user = User.getUserFromAuthentication(authentication);
        Client client = clientService.loadClientByClientId(clientId);

        checkIfOwnerAndAdmin(user, client);

        String password = UUID.randomUUID().toString();
        client.setClientSecretPlain(password);
        client.setClientSecret(passwordEncoder.encode(password));

        clientRepository.save(client);

        return new ResponseEntity<>(password, HttpStatus.OK);
    }

    private void checkIfOwnerAndAdmin(User user, Client client) throws GenericUnauthorizedException {

        if (!admins.contains(user.getEmail())) {
            throw new GenericUnauthorizedException();
        }

        if (user.getId() != client.getOwner()) {
            throw new GenericUnauthorizedException();
        }
    }
}
