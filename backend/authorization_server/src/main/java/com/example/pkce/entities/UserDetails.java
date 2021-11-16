package com.example.pkce.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_details")
public class UserDetails implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "first_name")
  private String firstName = "";

  @Column(name = "last_name")
  private String lastName = "";

  @Column(name = "phone")
  private String phone = "";

  public UserDetails() {}

  public UserDetails(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
