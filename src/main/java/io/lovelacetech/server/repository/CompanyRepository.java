package io.lovelacetech.server.repository;

import io.lovelacetech.server.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource
public interface CompanyRepository extends JpaRepository<Company, UUID> {
  Company findByName(String name);

  Company findByPhoneNumber(String phoneNumber);

  List<Company> findByNameOrPhoneNumber(String name, String phoneNumber);
}
