package com.example.pkce.repositories;

import com.example.pkce.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

  Optional<Client> findByClientId(String clientId);

  boolean existsByOwnerAndName(int owner, String name);

  List<Client> findAllByOwner(int id);

  List<Client> findAllByOwnerAndClientIdIsNot(int owner, String clientId);

}
