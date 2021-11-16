package com.example.pkce.repositories;

import com.example.pkce.entities.PasswordChangeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasswordChangeTokenRepository extends JpaRepository<PasswordChangeToken, UUID> {

  void deleteAllByEmail(String email);
}
