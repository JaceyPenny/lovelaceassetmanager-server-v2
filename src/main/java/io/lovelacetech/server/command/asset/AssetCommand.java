package io.lovelacetech.server.command.asset;

import io.lovelacetech.server.command.BaseCommand;
import io.lovelacetech.server.repository.AssetRepository;

public abstract class AssetCommand<T extends AssetCommand> implements BaseCommand {
  private AssetRepository assetRepository;

  public T setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return (T) this;
  }

  public AssetRepository getAssetRepository() {
    return assetRepository;
  }

  @Override
  public boolean checkCommand() {
    return assetRepository != null;
  }
}
