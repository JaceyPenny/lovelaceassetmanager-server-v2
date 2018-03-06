package io.lovelacetech.server.command.assettype;

import io.lovelacetech.server.model.AssetType;
import io.lovelacetech.server.model.api.response.DefaultApiResponse;
import org.assertj.core.util.Strings;

public class AddAssetTypeCommand extends AssetTypeCommand<AddAssetTypeCommand> {
  private String newAssetTypeName;

  public AddAssetTypeCommand setNewAssetTypeName(String newAssetTypeName) {
    this.newAssetTypeName = newAssetTypeName.trim();
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && !Strings.isNullOrEmpty(newAssetTypeName);
  }

  @Override
  public DefaultApiResponse execute() {
    if (!checkCommand()) {
      return new DefaultApiResponse().setDefault();
    }

    if (!userCanModifyAssetTypes()) {
      return new DefaultApiResponse().setDefault();
    }

    AssetType existingAssetType = getAssetTypeRepository()
        .findOneByCompanyIdAndType(getUser().getCompanyId(), newAssetTypeName);

    if (existingAssetType != null) {
      return new DefaultApiResponse().setSuccess();
    }

    AssetType assetType = new AssetType();
    assetType.setCompanyId(getUser().getCompanyId());
    assetType.setType(newAssetTypeName);

    getAssetTypeRepository().saveAndFlush(assetType);
    return new DefaultApiResponse().setSuccess();
  }
}
