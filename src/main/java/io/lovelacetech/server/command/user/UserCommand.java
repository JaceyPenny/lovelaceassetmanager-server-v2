package io.lovelacetech.server.command.user;

import io.lovelacetech.server.command.BaseCommand;
import io.lovelacetech.server.repository.UserRepository;

public abstract class UserCommand<T extends UserCommand> implements BaseCommand {
  private UserRepository userRepository;

  public T setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
    return (T) this;
  }

  public UserRepository getUserRepository() {
    return userRepository;
  }

  @Override
  public boolean checkCommand() {
    return userRepository != null;
  }
}
