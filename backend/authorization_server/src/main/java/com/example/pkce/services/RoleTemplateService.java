package com.example.pkce.services;

import com.example.pkce.entities.RoleTemplate;
import com.example.pkce.exceptions.GenericBadRequestError;
import com.example.pkce.repositories.RoleTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleTemplateService {

    @Autowired
    private RoleTemplateRepository roleTemplateRepository;

    public RoleTemplate loadTemplateById(int id) throws GenericBadRequestError {
        Optional<RoleTemplate> roleTemplate = roleTemplateRepository.findById(id);
        return roleTemplate.orElseThrow(() -> new GenericBadRequestError("roleTemplate_not_found"));
    }

    public List<RoleTemplate> loadAllByClientId(String clientId){
        return roleTemplateRepository.getAllByClientId(clientId);
    }

    public RoleTemplate loadByClientIdAndRole(String clientId,String role){
        Optional<RoleTemplate> roleTemplate = roleTemplateRepository.getByClientIdAndRole(clientId, role);
        return roleTemplate.orElseThrow(() -> new GenericBadRequestError("roleTemplate_not_found"));
    }

    public RoleTemplate save(RoleTemplate roleTemplate){
        return roleTemplateRepository.save(roleTemplate);
    }

    public void delete(RoleTemplate roleTemplate){
        roleTemplateRepository.delete(roleTemplate);
    }

}
