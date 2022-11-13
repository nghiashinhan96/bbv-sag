package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.services.domain.bo.dto.CustomerSettingsBODto;
import com.sagag.services.domain.bo.dto.PaymentSettingBODto;
import com.sagag.services.domain.eshop.dto.CustomerSettingsDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;

import java.util.List;

public interface CustomerSettingsService {

  /**
   * Save a customerSetting
   *
   * @param customerSettings
   */
  void saveCustomerSetting(CustomerSettings customerSettings);

  CustomerSettingsDto updateCustomerSetting(final Long userId,
      final CustomerSettingsDto customerSettingsDto);

  CustomerSettingsDto getCustomerSetting(final Long userId);

  /**
   * Returns the customer settings from organisation id.
   *
   * @param orgId the organisation id
   * @return an {@link CustomerSettings}.
   */
  CustomerSettings findSettingsByOrgId(int orgId);

  /**
   * Gets customer setting from DB by ordCode.
   *
   * @param orgCode
   * @return PaymentSettingBODto
   */
  PaymentSettingBODto getCustomerSettingByOrgCodeByAdmin(String orgCode);

  /**
   * Updates customerSetting.
   *
   * @param customerSettingsBODto the {@link CustomerSettingsBODto}
   */
  void updateCustomerSettingByOrgCode(CustomerSettingsBODto customerSettingsBODto);

  /**
   * Creates customer settings.
   *
   * @param customer
   * @param addresses
   * @return the object of {@link CustomerSettings}
   */
  CustomerSettings createCustomerSettings(Customer customer, List<Address> addresses);
}
