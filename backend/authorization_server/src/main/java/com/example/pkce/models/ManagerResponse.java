package com.example.pkce.models;

import com.example.pkce.entities.Client;
import com.example.pkce.entities.RoleTemplate;

import java.util.List;

public class ManagerResponse {

    private int ownerId;
    private String clientId;
    private String clientName;
    private List<RoleTemplate> rolesTemplates;

    public ManagerResponse() {
    }

    public ManagerResponse(Client client){
        this.ownerId = client.getOwner();
        this.clientId = client.getClientId();
        this.clientName = client.getName();
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<RoleTemplate> getRolesTemplates() {
        return rolesTemplates;
    }

    public void setRolesTemplates(List<RoleTemplate> rolesTemplates) {
        this.rolesTemplates = rolesTemplates;
    }
}
