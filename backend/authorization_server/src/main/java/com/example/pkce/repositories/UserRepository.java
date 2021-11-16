package com.example.pkce.repositories;

import com.example.pkce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findUserByEmailAndEmailActivatedIsTrue(String email);

  Optional<User> findUserByEmail(String email);

  Optional<User> findByGoogleId(String id);

  Optional<User> findByFacebookId(String id);

  boolean existsByEmail(String email);

  boolean existsByEmailAndEmailActivatedIsTrue(String email);

  boolean existsByGoogleId(String googleId);

  boolean existsByFacebookId(String facebookId);
}
