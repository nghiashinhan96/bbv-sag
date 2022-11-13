package com.sagag.services.admin.business.service;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.domain.bo.dto.CustomerSettingsBODto;
import com.sagag.services.domain.bo.dto.PaymentSettingBODto;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;

public interface BoCustomerService {

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
   * @throws MdmCustomerNotFoundException 
   * @throws UserValidationException 
   */
  void updateCustomerSettingByOrgCode(CustomerSettingsBODto customerSettingsBODto) throws UserValidationException, MdmCustomerNotFoundException;
}
