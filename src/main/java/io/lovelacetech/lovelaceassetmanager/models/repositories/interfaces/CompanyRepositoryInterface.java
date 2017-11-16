package io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces;

import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.BaseRepositoryInterface;
import io.lovelacetech.lovelaceassetmanager.models.entities.CompanyEntity;

public interface CompanyRepositoryInterface extends BaseRepositoryInterface<CompanyEntity> {
    CompanyEntity byCredentials(String email, String password);
}
