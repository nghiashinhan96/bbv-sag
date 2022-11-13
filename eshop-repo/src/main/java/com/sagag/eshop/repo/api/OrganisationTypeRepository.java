package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.OrganisationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrganisationTypeRepository extends JpaRepository<OrganisationType, Integer> {

  Optional<OrganisationType> findOneByName(String name);

  @Query(value = "select o from OrganisationType o where o.name = 'CUSTOMER'")
  OrganisationType getCustomerOrgType();

}
