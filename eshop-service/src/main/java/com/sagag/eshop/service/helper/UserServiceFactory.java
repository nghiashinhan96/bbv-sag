package com.sagag.eshop.service.helper;

import com.sagag.eshop.service.api.UserSettingsCreateService;
import com.sagag.services.common.utils.WholesalerUtils;
import com.sagag.services.domain.eshop.dto.OrganisationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserServiceFactory {

  @Autowired
  @Qualifier("finalCustomerUserServiceImpl")
  private UserSettingsCreateService finalCustomerSettingCreateService;

  @Autowired
  @Qualifier("userSettingsServiceImpl")
  private UserSettingsCreateService userSettingsService;

  public UserSettingsCreateService getSettingsCreateService(OrganisationDto input) {
    if (Objects.nonNull(input) && WholesalerUtils.isFinalCustomer(input.getOrgCode())) {
      return finalCustomerSettingCreateService;
    }
    return userSettingsService;
  }
}
