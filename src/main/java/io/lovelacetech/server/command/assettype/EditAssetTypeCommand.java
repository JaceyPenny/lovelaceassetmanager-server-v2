package io.lovelacetech.server.command.assettype;

import io.lovelacetech.server.model.api.response.DefaultApiResponse;
import org.assertj.core.util.Strings;

public class EditAssetTypeCommand extends AssetTypeCommand<EditAssetTypeCommand> {
  private String existingAssetTypeName;
  private String newAssetTypeName;

  public EditAssetTypeCommand setExistingAssetTypeName(String existingAssetTypeName) {
    this.existingAssetTypeName = existingAssetTypeName.trim();
    return this;
  }

  public EditAssetTypeCommand setNewAssetTypeName(String newAssetTypeName) {
    this.newAssetTypeName = newAssetTypeName.trim();
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && !Strings.isNullOrEmpty(existingAssetTypeName)
        && !Strings.isNullOrEmpty(newAssetTypeName);
  }

  public DefaultApiResponse execute() {
    if (!checkCommand()) {
      return new DefaultApiResponse().setDefault();
    }

    if (!userCanModifyAssetTypes()) {
      return new DefaultApiResponse().setDefault();
    }

    int modified = getAssetTypeRepository().setTypeByType(newAssetTypeName, existingAssetTypeName);
    if (modified == 0) {
      return new DefaultApiResponse().setNotFound();
    }

    return new DefaultApiResponse().setSuccess();
  }
}
