package com.sagag.services.copydb.domain.src;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "CUSTOMER_SETTINGS")
@NamedQuery(name = "CustomerSettings.findAll", query = "SELECT c FROM CustomerSettings c")
@Data
public class CustomerSettings implements Serializable {

  private static final long serialVersionUID = 6353100466100810921L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ADDRESS_ID")
  private Boolean addressId;

  @Column(name = "ALLOCATION_ID")
  private Integer allocationId;

  @Column(name = "ALLOW_NET_PRICE_CHANGED")
  private Boolean allowNetPriceChanged;

  @Column(name = "ALLOW_SHOW_DISCOUNT_CHANGED")
  private Boolean allowShowDiscountChanged;

  @Column(name = "BILLING_ADDRESS_ID")
  private Long billingAddressId;

  @Column(name = "COLLECTIVE_DELIVERY_ID")
  private Integer collectiveDeliveryId;

  @Column(name = "DELIVERY_ADDRESS_ID")
  private Long deliveryAddressId;

  @Column(name = "DELIVERY_ID")
  private Integer deliveryId;

  @Column(name = "EMAIL_NOTIFICATION_ORDER")
  private Boolean emailNotificationOrder;

  @Column(name = "HAS_PARTNER_PROGRAM_VIEW")
  private Boolean hasPartnerProgramView;

  @Column(name = "HOME_BRANCH")
  private String homeBranch;

  @Column(name = "INVOICE_TYPE")
  private Integer invoiceType;

  @Column(name = "NET_PRICE_CONFIRM")
  private Boolean netPriceConfirm;

  @Column(name = "NET_PRICE_VIEW")
  private Boolean netPriceView;

  @Column(name = "NORMAUTO_DISPLAY")
  private Boolean normautoDisplay;

  @Column(name = "PAYMENT_METHOD")
  private Integer paymentMethod;

  @Column(name = "SESSION_TIMEOUT_SECONDS")
  private Integer sessionTimeoutSeconds;

  @Column(name = "SHOW_DISCOUNT")
  private Boolean showDiscount;

  @Column(name = "SHOW_OCI_VAT")
  private Boolean showOciVat;

  @Column(name = "USE_DEFAULT_SETTING")
  private Boolean useDefaultSetting;

  @Column(name = "VIEW_BILLING")
  private Boolean viewBilling;

}
