package io.lovelacetech.server.command.assettype;

import io.lovelacetech.server.command.BaseCommand;
import io.lovelacetech.server.model.api.enums.AccessLevel;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.repository.AssetTypeRepository;
import io.lovelacetech.server.util.AuthenticationUtils;
import io.lovelacetech.server.util.UUIDUtils;

public abstract class AssetTypeCommand<T extends AssetTypeCommand> implements BaseCommand {
  private AssetTypeRepository assetTypeRepository;
  private ApiUser user;

  public T setAssetTypeRepository(AssetTypeRepository assetTypeRepository) {
    this.assetTypeRepository = assetTypeRepository;
    return (T) this;
  }

  AssetTypeRepository getAssetTypeRepository() {
    return assetTypeRepository;
  }

  public T setUser(ApiUser user) {
    this.user = user;
    return (T) this;
  }

  ApiUser getUser() {
    return user;
  }

  @Override
  public boolean checkCommand() {
    return assetTypeRepository != null && user != null;
  }

  boolean userCanModifyAssetTypes() {
    return UUIDUtils.isValidId(user.getCompanyId())
        && AuthenticationUtils.userIsAtLeast(user, AccessLevel.ADMIN);
  }
}
