package io.lovelacetech.server.model.api.model;

import io.lovelacetech.server.model.User;
import io.lovelacetech.server.util.RepositoryUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiUserList extends BaseApiModel {
  private List<ApiUser> users;

  public ApiUserList() {
    this.users = new ArrayList<>();
  }

  public ApiUserList(Iterable<User> users) {
    this.users = RepositoryUtils.toApiList(users);
  }

  public ApiUserList(List<ApiUser> users) {
    this.users = users;
  }

  public List<ApiUser> getUsers() {
    return users;
  }

  public ApiUserList setUsers(List<ApiUser> users) {
    this.users = users;
    return this;
  }

  public ApiUserList addUser(ApiUser user) {
    users.add(user);
    return this;
  }
}
