package io.lovelacetech.server.command.assettype;

import io.lovelacetech.server.model.Asset;
import io.lovelacetech.server.model.AssetType;
import io.lovelacetech.server.model.api.model.ApiAssetType;
import io.lovelacetech.server.model.api.response.DefaultApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import org.assertj.core.util.Strings;

import java.util.List;

public class DeleteAssetTypeCommand extends AssetTypeCommand<DeleteAssetTypeCommand> {
  private String assetTypeName;

  private AssetRepository assetRepository;

  public DeleteAssetTypeCommand setAssetTypeName(String assetTypeName) {
    this.assetTypeName = assetTypeName.trim();
    return this;
  }

  public DeleteAssetTypeCommand setAssetRepository(AssetRepository assetRepository) {
    this.assetRepository = assetRepository;
    return this;
  }

  @Override
  public boolean checkCommand() {
    return super.checkCommand()
        && !Strings.isNullOrEmpty(assetTypeName)
        && assetRepository != null;
  }

  public DefaultApiResponse execute() {
    if (!checkCommand()) {
      return new DefaultApiResponse().setDefault();
    }

    if (!userCanModifyAssetTypes()) {
      return new DefaultApiResponse().setAccessDenied();
    }

    AssetType existingAssetType = getAssetTypeRepository()
        .findOneByCompanyIdAndType(getUser().getCompanyId(), assetTypeName);
    if (existingAssetType == null) {
      return new DefaultApiResponse().setNotFound();
    }

    List<Asset> connectedAssets = assetRepository.findAllByAssetTypeEquals(existingAssetType);
    if (connectedAssets != null && !connectedAssets.isEmpty()) {
      // change the asset types for these assets to the default asset type
      AssetType defaultAssetType = getAssetTypeRepository()
          .findOneByCompanyIdAndType(getUser().getCompanyId(), ApiAssetType.DEFAULT_ASSET_TYPE);
      if (defaultAssetType == null) {
        defaultAssetType = new AssetType();
        defaultAssetType.setCompanyId(getUser().getCompanyId());
        defaultAssetType.setType(ApiAssetType.DEFAULT_ASSET_TYPE);
        defaultAssetType = getAssetTypeRepository().save(defaultAssetType);
      }

      for (Asset asset : connectedAssets) {
        asset.setAssetType(defaultAssetType);
      }

      assetRepository.save(connectedAssets);
      assetRepository.flush();
    }

    getAssetTypeRepository().delete(existingAssetType);

    return new DefaultApiResponse().setSuccess();
  }
}
