package com.example.resource.jwt;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
  private String authority;
  @Override
  public String getAuthority() {
    return authority;
  }

  public Authority(String authority) {
    this.authority = authority;
  }

  @Override
  public String toString() {
    return "Authority{" +
      "authority='" + authority + '\'' +
      '}';
  }
}
