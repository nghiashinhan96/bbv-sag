package com.sagag.services.stakis.erp.config;

import java.util.Map;

import com.sagag.services.common.enums.PaymentMethodType;

import lombok.Data;

@Data
public class StakisConfigData {

  private Map<PaymentMethodType, String> payment;

  private Map<String, String> fgasItemText;
}
