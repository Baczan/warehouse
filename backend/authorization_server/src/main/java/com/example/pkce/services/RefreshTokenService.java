package com.example.pkce.services;

import com.example.pkce.entities.RefreshToken;
import com.example.pkce.exceptions.TokenNotFoundException;
import com.example.pkce.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefreshTokenService {

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  public RefreshToken save(RefreshToken refreshToken) {
    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken findById(UUID id) throws TokenNotFoundException {
    return refreshTokenRepository
        .findById(id)
        .orElseThrow(() -> new TokenNotFoundException("Token not found"));
  }
}
