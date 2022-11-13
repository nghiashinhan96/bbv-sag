package com.sagag.eshop.repo.api;

import com.sagag.eshop.repo.entity.CustomerSettings;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerSettingsRepository extends CrudRepository<CustomerSettings, Integer> {

  /**
   * Returns a {@link CustomerSettings}.
   *
   * @return an OrgOrderSettings.
   */
  Optional<CustomerSettings> findOneById(int id);

  /**
   * Returns the customer settings from organisation id.
   *
   * @param orgId the organisation id
   * @return the customer settings
   */
  @Query(value = "select cs from Organisation o join o.customerSettings cs where o.id = :orgId")
  CustomerSettings findSettingsByOrgId(@Param("orgId") int orgId);

  @Query(value = "select cs from Organisation o join o.customerSettings cs where o.orgCode = :orgCode")
  CustomerSettings findSettingsByOrgCode(@Param("orgCode") String orgCode);

  @Query(value = "select case when count(cs) > 0 then 'true' else 'false' end "
      + "from  Organisation o join o.customerSettings cs where o.id = :orgId and cs.wssShowNetPrice = TRUE")
  boolean checkWholesalerEnableViewNetPrice(@Param("orgId") int orgId);

  @Query(value = "select case when count(cs) > 0 then 'true' else 'false' end "
      + "from  CustomerSettings cs where cs.wssDeliveryProfile.id = :deliveryProfileId")
  boolean checkDeliveryProfileBeingUsed(@Param("deliveryProfileId") int deliveryProfileId);
}
