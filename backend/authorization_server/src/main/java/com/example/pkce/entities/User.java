package com.example.pkce.entities;

import com.example.pkce.models.AuthorityModel;
import com.example.pkce.models.EmailUser;
import com.example.pkce.models.UserDetailsModel;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.Authentication;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "email_activated", nullable = false)
  private boolean emailActivated = false;

  @Column() private String password;

  @Column(name = "google_id", unique = true)
  private String googleId;

  @Column(name = "facebook_id", unique = true)
  private String facebookId;

  @OneToMany(
      mappedBy = "user_id",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<Authority> authorities;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "user_details_id", referencedColumnName = "id")
  private UserDetails userDetails;

  @Column(name = "createdAt", nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;

  public User() {}


  static public User getUserFromAuthentication(Authentication authentication){
    return ((EmailUser) authentication.getPrincipal()).getUser();
  }

  public void setUserDetailsFromModel(UserDetailsModel userDetailsModel){
    this.userDetails.setFirstName(userDetailsModel.getFirstName());
    this.userDetails.setLastName(userDetailsModel.getLastName());
    this.userDetails.setPhone(userDetailsModel.getPhone());
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEmailActivated() {
    return emailActivated;
  }

  public void setEmailActivated(boolean emailActivated) {
    this.emailActivated = emailActivated;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getGoogleId() {
    return googleId;
  }

  public void setGoogleId(String googleId) {
    this.googleId = googleId;
  }

  public String getFacebookId() {
    return facebookId;
  }

  public void setFacebookId(String facebookId) {
    this.facebookId = facebookId;
  }

  public List<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(List<Authority> authorities) {
    this.authorities = authorities;
  }

  public UserDetails getUserDetails() {
    return userDetails;
  }

  public void setUserDetails(UserDetails userDetails) {
    this.userDetails = userDetails;
  }

  public List<AuthorityModel> getAuthoritiesAsGrantedCollection() {
    List<AuthorityModel> grantedAuthoritiesList = new ArrayList<>();
    for (Authority authority : authorities) {
      grantedAuthoritiesList.add(new AuthorityModel(authority.getRole()));
    }

    return grantedAuthoritiesList;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }


}
