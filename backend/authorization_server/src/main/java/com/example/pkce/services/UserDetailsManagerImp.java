package com.example.pkce.services;

import com.example.pkce.entities.User;
import com.example.pkce.models.EmailUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

public class UserDetailsManagerImp implements UserDetailsManager {

  @Autowired private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    User user = userService.loadUserByEmailAndActivatedIsTrue(s);
    EmailUser emailUser = new EmailUser(user);
    return emailUser;
  }

  @Override
  public void createUser(UserDetails userDetails) {}

  @Override
  public void updateUser(UserDetails userDetails) {}

  @Override
  public void deleteUser(String s) {}

  @Override
  public void changePassword(String s, String s1) {}

  @Override
  public boolean userExists(String s) {
    return false;
  }
}
