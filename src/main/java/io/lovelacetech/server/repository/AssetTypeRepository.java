package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface AssetTypeRepository extends JpaRepository<AssetType, UUID> {
  AssetType findOneByCompanyIdAndType(UUID companyId, String type);

  List<AssetType> findAllByCompanyId(UUID companyId);

  @Modifying
  @Query("UPDATE AssetType at SET at.type = ?1 WHERE at.type = ?2")
  int setTypeByType(String newType, String oldType);
}
