package com.example.pkce.elasticSearchEntities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "users")
public class ElasticUser {

  @Id
  private int id;

  @Field(type = FieldType.Text, name = "email")
  private String email;

  @Field(type = FieldType.Keyword, name = "emailKeyword", index = true)
  private String emailKeyword;

  @Field(type = FieldType.Text, name = "firstName")
  private String firstName;

  @Field(type = FieldType.Keyword, name = "firstNameKeyword", index = true)
  private String firstNameKeyword;

  @Field(type = FieldType.Text, name = "lastName")
  private String lastName;

  @Field(type = FieldType.Keyword, name = "lastNameKeyword", index = true)
  private String lastNameKeyword;

  @Field(type = FieldType.Auto, name = "createdAt")
  private Date createdAt;

  public ElasticUser() {}

  public ElasticUser(int id, String email, Date createdAt) {
    this.id = id;
    this.email = email;
    this.emailKeyword = email;
    this.createdAt = createdAt;
  }

  public ElasticUser(int id, String email, String firstName, String lastName, Date createdAt) {
    this.id = id;
    this.email = email;
    this.emailKeyword = email;
    this.firstName = firstName;
    this.firstNameKeyword = firstName;
    this.lastName = lastName;
    this.lastNameKeyword = lastName;
    this.createdAt = createdAt;
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

  public String getEmailKeyword() {
    return emailKeyword;
  }

  public void setEmailKeyword(String emailKeyword) {
    this.emailKeyword = emailKeyword;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getFirstNameKeyword() {
    return firstNameKeyword;
  }

  public void setFirstNameKeyword(String firstNameKeyword) {
    this.firstNameKeyword = firstNameKeyword;
  }

  public String getLastNameKeyword() {
    return lastNameKeyword;
  }

  public void setLastNameKeyword(String lastNameKeyword) {
    this.lastNameKeyword = lastNameKeyword;
  }

  @Override
  public String toString() {
    return "ElasticUser{"
        + "id="
        + id
        + ", email='"
        + email
        + '\''
        + ", emailKeyword='"
        + emailKeyword
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", firstNameKeyword='"
        + firstNameKeyword
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", lastNameKeyword='"
        + lastNameKeyword
        + '\''
        + ", createdAt="
        + createdAt
        + '}';
  }
}
