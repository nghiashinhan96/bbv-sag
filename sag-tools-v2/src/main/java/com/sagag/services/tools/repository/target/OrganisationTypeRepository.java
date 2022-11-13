package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.OrganisationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrganisationTypeRepository extends JpaRepository<OrganisationType, Integer> {

  Optional<OrganisationType> findOneByName(String name);

  @Query(value = "select o from OrganisationType o where o.name = 'CUSTOMER'")
  OrganisationType getCustomerOrgType();

  Optional<OrganisationType> findByName(String name);

}
