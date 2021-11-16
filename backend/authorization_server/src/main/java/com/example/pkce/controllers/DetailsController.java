package com.example.pkce.controllers;

import com.example.pkce.elasticSearchEntities.ElasticUser;
import com.example.pkce.entities.User;
import com.example.pkce.exceptions.ElasticUserNotFound;
import com.example.pkce.models.EmailUser;
import com.example.pkce.models.UserDetailsModel;
import com.example.pkce.repositories.ElasticUserRepository;
import com.example.pkce.repositories.UserRepository;
import com.example.pkce.services.ElasticUserService;
import com.example.pkce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DetailsController {

  @Autowired private UserRepository userRepository;

  @Autowired private UserService userService;

  @Autowired private ElasticUserRepository elasticUserRepository;

  @Autowired private ElasticUserService elasticUserService;

  @PostMapping("/details")
  public ResponseEntity<?> changeDetails(
      @ModelAttribute UserDetailsModel userDetailsModel, Authentication authentication) throws ElasticUserNotFound {

    User user = User.getUserFromAuthentication(authentication);

    user.setUserDetailsFromModel(userDetailsModel);

    user = userRepository.save(user);


    ElasticUser elasticUser = elasticUserService.findById(user.getId());

    elasticUser.setFirstName(userDetailsModel.getFirstName());
    elasticUser.setLastName(userDetailsModel.getLastName());

    elasticUserRepository.save(elasticUser);

    return new ResponseEntity<>("Details changed", HttpStatus.OK);
  }

  @GetMapping("/details")
  public ResponseEntity<?> details(Authentication authentication) {
    User user = User.getUserFromAuthentication(authentication);
    user = userService.loadUserById(user.getId());

    return new ResponseEntity<>(user.getUserDetails(), HttpStatus.OK);
  }
}
