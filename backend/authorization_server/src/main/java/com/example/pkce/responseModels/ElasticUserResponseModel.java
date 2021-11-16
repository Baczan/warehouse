package com.example.pkce.responseModels;

import com.example.pkce.elasticSearchEntities.ElasticUser;

import java.util.List;

public class ElasticUserResponseModel {

  private List<ElasticUser> users;

  private long numberOfUsers;

  public ElasticUserResponseModel() {}

  public ElasticUserResponseModel(List<ElasticUser> users, long numberOfUsers) {
    this.users = users;
    this.numberOfUsers = numberOfUsers;
  }

  public List<ElasticUser> getUsers() {
    return users;
  }

  public void setUsers(List<ElasticUser> users) {
    this.users = users;
  }

  public long getNumberOfUsers() {
    return numberOfUsers;
  }

  public void setNumberOfUsers(long numberOfUsers) {
    this.numberOfUsers = numberOfUsers;
  }
}
