package com.example.pkce.services;

import com.example.pkce.entities.PasswordChangeToken;
import com.example.pkce.exceptions.GenericBadRequestError;
import com.example.pkce.repositories.PasswordChangeTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordChangeTokenService {

  @Autowired private PasswordChangeTokenRepository repository;

  public PasswordChangeToken save(PasswordChangeToken token) {
    return repository.save(token);
  }

  public PasswordChangeToken findById(UUID id) {
    return repository.findById(id).orElseThrow(()-> new GenericBadRequestError("invalid_token"));
  }

  public void deleteAllByEmail(String email) {
    repository.deleteAllByEmail(email);
  }
}
