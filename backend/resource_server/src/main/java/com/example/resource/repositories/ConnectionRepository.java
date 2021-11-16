package com.example.resource.repositories;

import com.example.resource.entities.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection,Integer> {

    boolean existsByService(String service);

    Connection getByService(String service);

    void deleteByService(String service);

}
