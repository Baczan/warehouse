package com.example.pkce.entities;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,name = "user_id")
    private int userId;

    @Column(name = "role_template_id",nullable = false)
    private int roleTemplateId;

    @Column(name = "client_id",nullable = false)
    private String clientId;

    public Role() {
    }

    public Role(int userId, int roleTemplateId, String clientId) {
        this.userId = userId;
        this.roleTemplateId = roleTemplateId;
        this.clientId = clientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleTemplateId() {
        return roleTemplateId;
    }

    public void setRoleTemplateId(int roleTemplateId) {
        this.roleTemplateId = roleTemplateId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
