package com.example.pkce.services;

import com.example.pkce.entities.User;
import com.example.pkce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  @Autowired private UserRepository userRepository;

  public User loadUserById(int id) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findById(id);
    return user.orElseThrow(() -> new UsernameNotFoundException("UserService:Username not found"));
  }

  public User loadUserByEmailAndActivatedIsTrue(String email) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findUserByEmailAndEmailActivatedIsTrue(email);
    return user.orElseThrow(() -> new UsernameNotFoundException("UserService:Username not found"));
  }

  public User loadUserByEmail(String email) {
    return userRepository.findUserByEmail(email).orElse(null);
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public User loadUserByGoogleId(String id) {
    Optional<User> user = userRepository.findByGoogleId(id);
    return user.orElse(null);
  }

  public User loadUserByFacebookId(String id) {
    Optional<User> user = userRepository.findByFacebookId(id);
    return user.orElse(null);
  }
}
