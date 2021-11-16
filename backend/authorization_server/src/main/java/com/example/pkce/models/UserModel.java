package com.example.pkce.models;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

  private int id;
  private String email;
  private List<String> authorities;

  public UserModel() {}

  public UserModel(EmailUser emailUser) {

    id = emailUser.getUser().getId();
    email = emailUser.getUser().getEmail();

    authorities = new ArrayList<>();

    for (GrantedAuthority authority : emailUser.getAuthorities()) {
      authorities.add(authority.getAuthority());
    }
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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
}
