package com.example.resource.repositories;

import com.example.resource.entities.LastEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LastEventRepository extends JpaRepository<LastEvent,Integer> {



}
