package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.ExternalOrganisation;
import com.sagag.services.common.enums.ExternalApp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExternalOrganisationRepository extends JpaRepository<ExternalOrganisation, Long> {

  Optional<ExternalOrganisation> findFirstByExternalCustomerNameAndExternalApp(
      String externalCustomerName, ExternalApp app);

  @Query(value = "select case when count(e) > 0 then 'true' else 'false' end "
      + "from ExternalOrganisation e where e.externalCustomerName = :externalCustomerName")
  boolean isCustomerNameExisted(@Param("externalCustomerName") String externalCustomerName);

  @Query("select e.externalCustomerId "
      + "from ExternalOrganisation e where e.orgId = :orgId and e.externalApp = :externalApp")
  String findExternalCustomerIdByOrgIdAndExternalApp(@Param("orgId") int orgId,
      @Param("externalApp") ExternalApp externalApp);

  @Query("select e.externalCustomerName "
      + "from ExternalOrganisation e where e.orgId = :orgId and e.externalApp = :externalApp")
  String findExternalCustomerNameByOrgIdAndExternalApp(@Param("orgId") int orgId,
      @Param("externalApp") ExternalApp externalApp);

}
