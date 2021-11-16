package com.example.resource.repositories;

import com.example.resource.entities.NewCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewCarRepository extends JpaRepository<NewCar,Integer> {

}
