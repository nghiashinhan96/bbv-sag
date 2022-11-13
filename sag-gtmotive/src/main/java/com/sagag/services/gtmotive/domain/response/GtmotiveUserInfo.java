package com.sagag.services.gtmotive.domain.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GtmotiveUserInfo implements Serializable {

  private static final long serialVersionUID = -5693371506819870719L;

  private String culture;
  private String currency;
  private GtmotiveBillingCode billingCode;
  private GtmotiveSimpleAttribute country;
  private GtmotiveSimpleAttribute language;
  private String cultureIsoCode;
  private String countryIsoCode;
  private String currencyIsoCode;
}
