package com.example.pkce.handlers;

import com.example.pkce.elasticSearchEntities.ElasticUser;
import com.example.pkce.entities.Authority;
import com.example.pkce.entities.User;
import com.example.pkce.entities.UserDetails;
import com.example.pkce.models.EmailUser;
import com.example.pkce.repositories.ElasticUserRepository;
import com.example.pkce.repositories.UserRepository;
import com.example.pkce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class CustomAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Autowired private UserService userService;

  @Autowired private UserRepository userRepository;

  @Autowired private ElasticUserRepository elasticUserRepository;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws ServletException, IOException {

    User user = null;
    SecurityContext securityContext = SecurityContextHolder.getContext();
    OAuth2AuthenticationToken auth2AuthenticationToken = ((OAuth2AuthenticationToken) authentication);


    if (auth2AuthenticationToken
        .getAuthorizedClientRegistrationId()
        .equals("google")) {

      String googleId = ((OAuth2User) authentication.getPrincipal()).getAttribute("sub");
      String email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");

      if (userRepository.existsByGoogleId(googleId)) {
        user = userService.loadUserByGoogleId(googleId);

      } else if (userRepository.existsByEmail(email)) {

        User loadedUser = userService.loadUserByEmail(email);
        loadedUser.setGoogleId(googleId);
        user = userRepository.save(loadedUser);

      } else {

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setGoogleId(googleId);
        newUser.setAuthorities(Collections.singletonList(new Authority("ROLE_USER", newUser)));

        String firstName = ((OAuth2User) authentication.getPrincipal()).getAttribute("given_name");
        String lastName = ((OAuth2User) authentication.getPrincipal()).getAttribute("family_name");

        newUser.setUserDetails(new UserDetails(firstName, lastName));
        user = userRepository.save(newUser);

        ElasticUser elasticUser =
            new ElasticUser(
                user.getId(), user.getEmail(), firstName, lastName, user.getCreatedAt());
        elasticUserRepository.save(elasticUser);
      }

    } else {

      String facebookId = ((OAuth2User) authentication.getPrincipal()).getAttribute("id");
      String email = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
      if (userRepository.existsByFacebookId(facebookId)) {
        user = userService.loadUserByFacebookId(facebookId);
      } else if (userRepository.existsByEmail(email)) {
        User loadedUser = userService.loadUserByEmail(email);
        loadedUser.setFacebookId(facebookId);
        user = userRepository.save(loadedUser);
      } else {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFacebookId(facebookId);
        newUser.setAuthorities(Collections.singletonList(new Authority("ROLE_USER", newUser)));
        newUser.setUserDetails(new UserDetails());
        user = userRepository.save(newUser);
        ElasticUser elasticUser =
            new ElasticUser(user.getId(), user.getEmail(), user.getCreatedAt());
        elasticUserRepository.save(elasticUser);
      }
    }

    EmailUser principal = new EmailUser(user);

    Authentication newAuthentication =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    securityContext.setAuthentication(newAuthentication);

    super.onAuthenticationSuccess(request, response, newAuthentication);
  }
}
