package com.sagag.services.common.domain.country;

import lombok.Data;

@Data
public class CustomCountryRegisterCustomerConfigProperties {

  private int sessionTimeoutSeconds;

  private boolean netPriceView;

  private boolean netPriceConfirm;

}
