package com.example.pkce.services;

import com.example.pkce.entities.Client;
import com.example.pkce.exceptions.ClientNotFoundException;
import com.example.pkce.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

  @Autowired private ClientRepository clientRepository;

  public Client loadClientById(int id) throws ClientNotFoundException {
    Optional<Client> client = clientRepository.findById(id);
    return client.orElseThrow(()-> new ClientNotFoundException("client_with_specified_id_not_found"));
  }

  public Client loadClientByClientId(String id) throws ClientNotFoundException {
    Optional<Client> client = clientRepository.findByClientId(id);
    return client.orElseThrow(()-> new ClientNotFoundException("client_with_specified_client_id_not_found"));
  }

  public List<Client> findAllOfUserClients(int id) {
    return clientRepository.findAllByOwner(id);
  }
}
