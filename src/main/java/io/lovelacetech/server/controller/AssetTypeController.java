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

  /**
   * <b> PUT /api/secure/assetTypes/{newAssetTypeName}</b>
   * <br><br>
   * Creates a new AssetType with the specified name.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": null
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * The user must be an admin at their company.
   */
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

  /**
   * <b> POST /api/secure/assetTypes/{existingName}/{newName}</b>
   * <br><br>
   * Updates the name of the existing AssetType with the name "existingName" to the name "newName".
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": null
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * The user must be an admin at their company.
   */
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

  /**
   * <b> DELETE /api/secure/assetTypes/{assetTypeName}</b>
   * <br><br>
   * Deletes the AssetType with the name "assetTypeName". If any Assets are set to the AssetType
   * with name "assetTypeName", their AssetTypes will be set to the AssetType with default name
   * "Asset". If no AssetType
   * exists with the default name, then a new AssetType will be created.
   * <br><br>
   * <b>  RESULT:  </b><br>
   * <pre>{@code    {
   *   "status": 200,
   *   "message": "success",
   *   "response": null
   * }}</pre>
   * <br>
   * <b>  PERMISSIONS  </b><br>
   * The user must be an admin at their company
   */
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
