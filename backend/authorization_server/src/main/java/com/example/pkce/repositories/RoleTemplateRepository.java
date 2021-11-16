package com.example.pkce.repositories;

import com.example.pkce.entities.RoleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleTemplateRepository extends JpaRepository<RoleTemplate,Integer> {

    Optional<RoleTemplate> getByClientIdAndRole(String clientId,String role);

    List<RoleTemplate> getAllByClientId(String clientId);

    void deleteAllByClientId(String clientId);
}
