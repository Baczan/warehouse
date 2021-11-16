package com.example.pkce.models;

import org.springframework.security.core.GrantedAuthority;

public class AuthorityModel implements GrantedAuthority {

  private String role;

  public AuthorityModel(String role) {
    this.role = role;
  }

  public AuthorityModel() {}

  @Override
  public String getAuthority() {
    return role;
  }

  @Override
  public String toString() {
    return "AuthorityModel{" + "role='" + role + '\'' + '}';
  }
}
