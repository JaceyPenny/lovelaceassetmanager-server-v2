package io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces;

import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.BaseRepositoryInterface;
import io.lovelacetech.lovelaceassetmanager.models.entities.EmployeeEntity;

public interface EmployeeRepositoryInterface extends BaseRepositoryInterface<EmployeeEntity> {
	EmployeeEntity byCredentials(String employeeId, String password);
}
