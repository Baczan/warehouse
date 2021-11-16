package com.example.resource.jwt;

import java.security.Principal;
import java.util.List;

public class JwtUser implements Principal {

  @Override
  public String getName() {
    return name;
  }


  private String name;
  private String email;
  private List<String> authorities;

  public void setName(String name) {
    this.name = name;
  }

  public JwtUser(String name) {
    this.name = name;
  }

  public JwtUser(String name, String email, List<String> authorities) {
    this.name = name;
    this.email = email;
    this.authorities = authorities;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<String> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(List<String> authorities) {
    this.authorities = authorities;
  }

  @Override
  public String toString() {
    return "JwtUser{" +
      "name='" + name + '\'' +
      ", email='" + email + '\'' +
      ", authorities=" + authorities +
      '}';
  }
}
