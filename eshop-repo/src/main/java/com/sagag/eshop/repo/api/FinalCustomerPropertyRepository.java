package com.sagag.eshop.repo.api;


import com.sagag.eshop.repo.entity.FinalCustomerProperty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FinalCustomerPropertyRepository
    extends JpaRepository<FinalCustomerProperty, Long> {

  List<FinalCustomerProperty> findByOrgId(@Param("orgId") final Long orgId);

  @Query(
      value = "select p from FinalCustomerProperty p where p.orgId = :orgId and p.settingKey =:settingKey")
  Optional<FinalCustomerProperty> findByOrgIdAndSettingKey(@Param("orgId") final Long orgId,
      @Param("settingKey") final String settingKey);
}

