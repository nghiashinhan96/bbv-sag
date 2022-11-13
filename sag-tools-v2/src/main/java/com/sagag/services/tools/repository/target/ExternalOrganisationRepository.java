package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.ExternalOrganisation;
import com.sagag.services.tools.support.ExternalApp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalOrganisationRepository extends JpaRepository<ExternalOrganisation, Long> {

  Optional<ExternalOrganisation> findFirstByOrgId(Integer orgId);

  Optional<ExternalOrganisation> findFirstByExternalCustomerNameAndExternalApp(String externalCustomerName, ExternalApp app);

  @Query(value = "select case when count(e) > 0 then 'true' else 'false' end "
      + "from ExternalOrganisation e where e.externalCustomerName = :externalCustomerName")
  boolean isCustomerNameExisted(@Param("externalCustomerName") String externalCustomerName);

}
