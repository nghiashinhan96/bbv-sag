package com.sagag.services.common.enums.offer;

import lombok.Getter;

@Getter
public enum OfferPersonPropertyType {

  CARPLATE("carplate"),
  DEFAULT_DELIVERY_TYPE("defaultDeliveryType"),
  DEFAULT_PAYMENT_TYPE("defaultPaymentType"),
  DEFAULT_SHIPMENT_TYPE("defaultShipmentType"),
  FAX("fax"),
  HAS_ARTICLE_NUMBER_PREVIEW("hasArticlenumberPreview"),
  HAS_EMAIL_NOTIFICATION_NEWSLETTER("hasEmailNotificationNewsletter"),
  HAS_EMAIL_NOTIFICATION_ORDER("hasEmailNotificationOrder"),
  HAS_EMAIL_NOTIFICATION_ORDER_ACK("hasEmailNotificationOrderAck"),
  HAS_NETTO_PRICE_EMAIL_NOTIFICATION_ORDER("hasNettopriceEmailNotificationOrder"),
  HAS_NETTO_PRICE_PREVIEW("hasNettopricePreview"),
  HAS_PARTNER_PROGRAM_LOGIN("hasPartnerprogramLogin"),
  HAS_PARTNER_PROGRAM_VIEW("hasPartnerprogramView"),
  HAS_PERMANENT_ORDER_SETTINGS("hasPermanentOrderSettings"),
  HAS_VAT_PREVIEW("hasVatPrview"),
  HOURLY_RATE_WORK("hourlyRateWork"),
  PHONE("phone"),
  SALUTATION("salutation"),
  USER_NUMBER_WHOLE_SALER("usernumberWholesaler");

  private String value;

  public String getValue() {
    return value;
  }

  private OfferPersonPropertyType(final String value) {
    this.value = value;
  }

}
