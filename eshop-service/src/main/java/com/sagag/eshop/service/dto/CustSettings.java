package com.sagag.eshop.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

/**
 * Customer settings class at garage level.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustSettings extends AffiliateSettings {

  private static final long serialVersionUID = -7686134401012864863L;

  private static final int DEFAULT_SESSION_TIMEOUT_SECONDS = 3600;

  private int sessionTimeoutSeconds = DEFAULT_SESSION_TIMEOUT_SECONDS;
  private boolean viewBilling;
  private boolean netPriceConfirm;
  private boolean demoCustomer;
  private boolean normautoDisplay;
  private WssDeliveryProfileDto wssDeliveryProfile;
  private Integer wssMarginGroup;
  private boolean wholeSalerHasNetPrice;
  private boolean finalCustomerHasNetPrice;

  @JsonIgnore
  private boolean showOciVat;

  public void setSessionTimeoutSeconds(Integer timeout) {
    if (!Objects.isNull(timeout)) {
      sessionTimeoutSeconds = timeout;
    }
  }

}
