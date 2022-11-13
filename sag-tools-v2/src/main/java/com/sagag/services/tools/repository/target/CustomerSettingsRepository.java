package com.sagag.services.tools.repository.target;

import com.sagag.services.tools.domain.target.CustomerSettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerSettingsRepository extends JpaRepository<CustomerSettings, Integer> {

  @Query(value = "SELECT cs.* FROM CUSTOMER_SETTINGS cs "
      + "JOIN ORGANISATION o on o.ORDER_SETTINGS_ID = cs.ID "
      + "WHERE cs.HAS_PARTNER_PROGRAM_VIEW = :hasPartnerProgram AND o.ORG_CODE = :orgCode", nativeQuery = true)
  Optional<CustomerSettings> findCustomerSettingByPartnerProgramAndOrgCode(
      @Param("hasPartnerProgram") boolean hasPartnerProgram, @Param("orgCode") String orgCode);

}
