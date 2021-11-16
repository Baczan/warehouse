package com.example.pkce.services;

import com.example.pkce.entities.RegisterToken;
import com.example.pkce.exceptions.GenericBadRequestError;
import com.example.pkce.repositories.RegisterTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterTokenService {

  @Autowired private RegisterTokenRepository repository;

  public RegisterToken save(RegisterToken token) {
    return repository.save(token);
  }

  public RegisterToken findById(UUID id) {
    return repository.findById(id).orElseThrow(()-> new GenericBadRequestError("invalid_token"));
  }
}
