package com.example.pkce.controllers;

import com.example.pkce.entities.User;
import com.example.pkce.models.EmailUser;
import com.example.pkce.models.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Controller
public class AuthorizationControllers {

  @Autowired
  private Environment env;

  private List<String> admins;

  @PostConstruct
  private void loadAdmins(){
    admins = Arrays.asList(Objects.requireNonNull(env.getProperty("app.admins")).split(","));
  }

  // Redirect to angular app login page
  @GetMapping("/login")
  public String login() {
    return "redirect:/auth/login";
  }

  // Every request that needs to go to angular app comes thorough here
  @RequestMapping("/")
  public String index(
      HttpServletResponse response, CsrfToken csrfToken, Authentication authentication)
      throws JsonProcessingException {

    System.out.println(1);

    addCookies(response, "csrf", csrfToken.getToken(), false);

    // Set authentication and user cookies
    if (authentication == null) {

      addCookies(response, "authenticated", "false", false);
      addCookies(response,"admin","false",false);
      addCookies(response, "user", "", true);

    } else {

      addCookies(response, "authenticated", "true", false);


      User user = User.getUserFromAuthentication(authentication);
      if(admins.contains(user.getEmail())){
        addCookies(response,"admin","true",false);
      }else {
        addCookies(response,"admin","false",false);
      }

      // Convert user to base 64 because cookie can't contain certain characters like dots
      ObjectMapper objectMapper = new ObjectMapper();
      UserModel userModel = new UserModel((EmailUser) authentication.getPrincipal());
      String json_User = objectMapper.writeValueAsString(userModel);
      String base64User = Base64.getEncoder().encodeToString(json_User.getBytes());

      addCookies(response, "user", base64User, false);
    }

    System.out.println(2);

    // Redirect to angular app which is in templates folder
    return "app";
  }

  // Redirect angular routes to angular app to allow deep linking and refreshing site
  @GetMapping({"/auth/**", "/main/**"})
  public String redirect() {
    return "forward:/";
  }

  @GetMapping("/login-error")
  public String loginError(HttpSession session) {
    AuthenticationException ex =
        (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

    if (ex instanceof BadCredentialsException) {
      return "redirect:/auth/login?error=badcredentials";
    }

    return "redirect:/login";
  }

  @RequestMapping("/logout")
  public String logout(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(required = false) String redirect_uri) {

    // Delete authentication from context
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    // Redirect to custom url if provided
    if (redirect_uri != null) {
      return "redirect:" + redirect_uri;
    }

    return "redirect:/login";
  }

  private void addCookies(
      HttpServletResponse response, String name, String value, boolean deleteCookie) {
    Cookie cookie = new Cookie(name, value);
    cookie.setPath("/");

    if (deleteCookie) {
      cookie.setMaxAge(0);
    }

    response.addCookie(cookie);
  }
}
