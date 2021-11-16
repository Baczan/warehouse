package com.example.resource.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
public class TestController {

  @GetMapping("/test")
  public String test(Authentication authentication){
    System.out.println(authentication.getAuthorities());
    System.out.println(authentication.getName());
    System.out.println(authentication.getPrincipal());
    return "test";
  }

  @GetMapping("/test_admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String testAdmin(Authentication authentication){
    System.out.println(authentication.getAuthorities());
    return "testAdmin";
  }

  @GetMapping("/test_fake")
  @PreAuthorize("hasRole('FAKE')")
  public String test_fake(Authentication authentication){
    System.out.println(authentication.getAuthorities());
    return "test fake";
  }

}
