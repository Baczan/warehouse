package com.example.pkce.repositories;

import com.example.pkce.entities.RegisterToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegisterTokenRepository extends JpaRepository<RegisterToken, UUID> {

  void deleteAllByEmail(String email);
}
