package com.example.pkce.repositories;

import com.example.pkce.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    boolean existsByUserIdAndRoleTemplateId(int userId,int roleTemplateId);

    Optional<Role> findByUserIdAndRoleTemplateId(int userId,int roleTemplateId);

    List<Role> findAllByUserId(int userId);

    List<Role> findAllByUserIdAndClientId(int userId,String clientId);

    void deleteAllByRoleTemplateId(int id);

    void deleteAllByClientId(String clientId);

}
