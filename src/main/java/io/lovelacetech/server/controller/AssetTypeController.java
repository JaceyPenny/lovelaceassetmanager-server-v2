package io.lovelacetech.server.controller;

import io.lovelacetech.server.command.assettype.AddAssetTypeCommand;
import io.lovelacetech.server.command.assettype.DeleteAssetTypeCommand;
import io.lovelacetech.server.command.assettype.EditAssetTypeCommand;
import io.lovelacetech.server.model.api.model.ApiUser;
import io.lovelacetech.server.model.api.response.DefaultApiResponse;
import io.lovelacetech.server.repository.AssetRepository;
import io.lovelacetech.server.repository.AssetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
@Transactional
@RequestMapping("/api/secure/assetTypes")
public class AssetTypeController {
  private final AssetTypeRepository assetTypeRepository;
  private final AssetRepository assetRepository;

  @Autowired
  public AssetTypeController(
      AssetTypeRepository assetTypeRepository,
      AssetRepository assetRepository) {
    this.assetTypeRepository = assetTypeRepository;
    this.assetRepository = assetRepository;
  }

  @RequestMapping(value = "/{newAssetTypeName}", method = RequestMethod.PUT)
  public DefaultApiResponse putAssetType(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String newAssetTypeName) {
    return new AddAssetTypeCommand()
        .setAssetTypeRepository(assetTypeRepository)
        .setUser(authenticatedUser)
        .setNewAssetTypeName(newAssetTypeName)
        .execute();
  }

  @RequestMapping(value = "/{existingName}/{newName}", method = RequestMethod.POST)
  public DefaultApiResponse editAssetType(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String existingName,
      @PathVariable String newName) {
    return new EditAssetTypeCommand()
        .setAssetTypeRepository(assetTypeRepository)
        .setUser(authenticatedUser)
        .setExistingAssetTypeName(existingName)
        .setNewAssetTypeName(newName)
        .execute();
  }

  @RequestMapping(value = "/{assetTypeName}", method = RequestMethod.DELETE)
  public DefaultApiResponse deleteAssetType(
      @RequestAttribute ApiUser authenticatedUser,
      @PathVariable String assetTypeName) {
    return new DeleteAssetTypeCommand()
        .setAssetTypeRepository(assetTypeRepository)
        .setAssetRepository(assetRepository)
        .setUser(authenticatedUser)
        .setAssetTypeName(assetTypeName)
        .execute();
  }
}
