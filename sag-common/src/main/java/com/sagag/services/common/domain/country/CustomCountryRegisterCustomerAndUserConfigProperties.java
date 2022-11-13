package com.sagag.services.common.domain.country;

import lombok.Data;

@Data
public class CustomCountryRegisterCustomerAndUserConfigProperties {

  private CustomCountryRegisterCustomerConfigProperties customerSetting;

  private CustomCountryRegisterUserConfigProperties userSetting;

}
