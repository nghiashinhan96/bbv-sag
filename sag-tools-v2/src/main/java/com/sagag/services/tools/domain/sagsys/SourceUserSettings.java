package com.sagag.services.tools.domain.sagsys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries(value = {
  @NamedQuery(name = "SourceUserSettings.findAll", query = "SELECT o FROM SourceUserSettings o")
})
@Data
@Builder
@Table(name = "USER_SETTINGS")
@NoArgsConstructor
@AllArgsConstructor
public class SourceUserSettings implements Serializable {

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

  @Column(name = "BILLING_ADDRESS_ID")
  private String billingAddressId;

  @Column(name = "CLASSIC_CATEGORY_VIEW")
  private Boolean classicCategoryView;

  @Column(name = "COLLECTIVE_DELIVERY_ID")
  private Integer collectiveDeliveryId;

  @Column(name = "CURRENT_STATE_NET_PRICE_VIEW")
  private Boolean currentStateNetPriceView;

  @Column(name = "DELIVERY_ADDRESS_ID")
  private String deliveryAddressId;

  @Column(name = "DELIVERY_ID")
  private Integer deliveryId;

  @Column(name = "EMAIL_NOTIFICATION_ORDER")
  private Boolean emailNotificationOrder;

  @Column(name = "HAS_PARTNER_PROGRAM_LOGIN")
  private Boolean hasPartnerProgramLogin;

  @Column(name = "HAS_PARTNER_PROGRAM_VIEW")
  private Boolean hasPartnerProgramView;

  @Column(name = "INVOICE_TYPE")
  private Integer invoiceType;

  @Column(name = "NET_PRICE_CONFIRM")
  private Boolean netPriceConfirm;

  @Column(name = "NET_PRICE_VIEW")
  private Boolean netPriceView;

  @Column(name = "PAYMENT_METHOD")
  private Integer paymentMethod;

  @Column(name = "SALE_ON_BEHALF_OF")
  private Boolean saleOnBehalfOf;

  @Column(name = "SHOW_DISCOUNT")
  private Boolean showDiscount;

  @Column(name = "USE_DEFAULT_SETTING")
  private Boolean useDefaultSetting;

  @Column(name = "VIEW_BILLING")
  private Boolean viewBilling;

}
