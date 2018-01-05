package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface CompanyRepository extends CrudRepository<Company, UUID> {
  Company findByName(String name);

  Company findByPhoneNumber(String phoneNumber);

  List<Company> findByNameOrPhoneNumber(String name, String phoneNumber);
}
