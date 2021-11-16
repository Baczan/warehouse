package com.example.pkce.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

  @Id
  @Column(unique = true, nullable = false)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "user", referencedColumnName = "id")
  private User user;

  public RefreshToken() {}

  public RefreshToken(UUID id, User user) {
    this.id = id;
    this.user = user;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "RefreshToken{" + "id=" + id + ", user=" + user + '}';
  }
}
