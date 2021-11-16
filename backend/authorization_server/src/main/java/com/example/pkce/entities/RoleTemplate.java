package com.example.pkce.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "role_template")
public class RoleTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "default_role", nullable = false)
    private boolean defaultRole = false;

    @Column(name = "owner_only", nullable = false)
    private boolean ownerOnly = false;

    @Column(name = "role", nullable = false)
    private String role;

    @JsonInclude()
    @Transient
    private boolean active;

    public RoleTemplate() {
    }

    public RoleTemplate(String clientId, boolean defaultRole, boolean ownerOnly, String role) {
        this.clientId = clientId;
        this.defaultRole = defaultRole;
        this.ownerOnly = ownerOnly;
        this.role = role;
    }

    public RoleTemplate(String clientId, String role) {
        this.clientId = clientId;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public boolean isDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(boolean defaultRole) {
        this.defaultRole = defaultRole;
    }

    public boolean isOwnerOnly() {
        return ownerOnly;
    }

    public void setOwnerOnly(boolean ownerOnly) {
        this.ownerOnly = ownerOnly;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
