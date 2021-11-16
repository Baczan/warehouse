package com.example.pkce.models;

import com.example.pkce.entities.Authority;
import com.example.pkce.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class EmailUser implements UserDetails {

  private String password;
  private String username;
  private User user;
  private Collection<GrantedAuthority> authorities;

  public EmailUser(User user) {
    password = user.getPassword();
    username = String.valueOf(user.getId());
    setUser(user);
    authorities = new ArrayList<>();
    for (Authority auth : user.getAuthorities()) {
      GrantedAuthority authority = new AuthorityModel(auth.getRole());
      authorities.add(authority);
    }
  }

  public EmailUser() {}

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "EmailUser{"
        + "password='"
        + password
        + '\''
        + ", username='"
        + username
        + '\''
        + ", authorities="
        + authorities
        + '}';
  }
}
