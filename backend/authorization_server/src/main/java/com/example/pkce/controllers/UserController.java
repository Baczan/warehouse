package com.example.pkce.controllers;

import com.example.pkce.elasticSearchEntities.ElasticUser;
import com.example.pkce.entities.*;
import com.example.pkce.exceptions.GenericBadRequestError;
import com.example.pkce.helpers.EmailSender;
import com.example.pkce.repositories.ElasticUserRepository;
import com.example.pkce.repositories.RegisterTokenRepository;
import com.example.pkce.repositories.UserRepository;
import com.example.pkce.services.PasswordChangeTokenService;
import com.example.pkce.services.RegisterTokenService;
import com.example.pkce.services.UserService;
import com.example.pkce.validators.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private RegisterTokenService registerTokenService;
  @Autowired private RegisterTokenRepository registerTokenRepository;
  @Autowired private EmailSender emailSender;
  @Autowired private PasswordChangeTokenService passwordChangeTokenService;
  @Autowired private ElasticUserRepository elasticUserRepository;

  @GetMapping("/register")
  public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password)
      throws MessagingException {

    if (userRepository.existsByEmailAndEmailActivatedIsTrue(email)) {
      throw new GenericBadRequestError("user_already_exists");
    }

    if (!PasswordValidator.validatePassword(password)) {
      throw new GenericBadRequestError("invalid_password");
    }

    RegisterToken registerToken =
        new RegisterToken(UUID.randomUUID(), email, passwordEncoder.encode(password));

    registerTokenService.save(registerToken);

    emailSender.sendRegistrationEmail(registerToken);

    return new ResponseEntity<>("ok", HttpStatus.OK);
  }

  @GetMapping("/activate")
  @Transactional
  public ResponseEntity<?> activation(@RequestParam String id) {

    UUID tokenId;
    try {
      tokenId = UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new GenericBadRequestError("invalid_token");
    }

    RegisterToken registerToken = registerTokenService.findById(tokenId);

    if (userRepository.existsByEmailAndEmailActivatedIsTrue(registerToken.getEmail())) {
      throw new GenericBadRequestError("account_already_activated");
    }

    if (userRepository.existsByEmail(registerToken.getEmail())) {

      User user = userService.loadUserByEmail(registerToken.getEmail());
      user.setPassword(registerToken.getPassword());
      user.setEmailActivated(true);
      userService.saveUser(user);

    } else {

      User user = new User();
      user.setEmail(registerToken.getEmail());
      user.setPassword(registerToken.getPassword());
      user.setEmailActivated(true);
      user.setAuthorities(Collections.singletonList(new Authority("ROLE_USER", user)));
      user.setUserDetails(new UserDetails());
      user = userService.saveUser(user);

      ElasticUser elasticUser = new ElasticUser(user.getId(), user.getEmail(), user.getCreatedAt());
      elasticUserRepository.save(elasticUser);
    }

    registerTokenRepository.deleteAllByEmail(registerToken.getEmail());
    return new ResponseEntity<>("user_activated", HttpStatus.OK);
  }

  @GetMapping("/password_change_request")
  public ResponseEntity<?> passwordRecoveryRequest(@RequestParam String email)
      throws MessagingException {

    if (!userRepository.existsByEmailAndEmailActivatedIsTrue(email)) {
      throw new GenericBadRequestError("user_not_found");
    }

    PasswordChangeToken passwordChangeToken = new PasswordChangeToken(UUID.randomUUID(), email);
    passwordChangeTokenService.save(passwordChangeToken);

    emailSender.sendPasswordChangeToken(passwordChangeToken);

    return new ResponseEntity<>("email_send", HttpStatus.OK);
  }

  @GetMapping("/password_change")
  @Transactional
  public ResponseEntity<?> passwordRecovery(
      @RequestParam String token, @RequestParam String password){
    UUID tokenId;
    try {
      tokenId = UUID.fromString(token);
    } catch (IllegalArgumentException e) {
      throw new GenericBadRequestError("invalid_token");
    }

    PasswordChangeToken passwordChangeToken = passwordChangeTokenService.findById(tokenId);

    if (!PasswordValidator.validatePassword(password)) {
      throw new GenericBadRequestError("invalid_password");
    }

    User user = userService.loadUserByEmail(passwordChangeToken.getEmail());
    user.setPassword(passwordEncoder.encode(password));
    userService.saveUser(user);

    passwordChangeTokenService.deleteAllByEmail(passwordChangeToken.getEmail());
    return new ResponseEntity<>("password_changed", HttpStatus.OK);
  }

  @GetMapping("/sync")
  @Transactional
  public ResponseEntity<?> sync(){

    elasticUserRepository.deleteAll();

    List<User> userList = userRepository.findAll();

    List<ElasticUser> elasticUsers = userList.stream().map(user -> new ElasticUser(user.getId(), user.getEmail(), user.getCreatedAt())).collect(Collectors.toList());

    elasticUserRepository.saveAll(elasticUsers);

    return new ResponseEntity<>("ok", HttpStatus.OK);
  }
}
