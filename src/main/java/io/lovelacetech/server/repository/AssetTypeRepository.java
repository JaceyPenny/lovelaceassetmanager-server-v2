package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.AssetType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource
public interface AssetTypeRepository extends CrudRepository<AssetType, UUID> {
  AssetType findOneByCompanyIdAndType(UUID companyId, String type);
}
