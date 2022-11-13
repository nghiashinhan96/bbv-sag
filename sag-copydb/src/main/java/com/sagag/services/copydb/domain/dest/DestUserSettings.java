package com.sagag.services.copydb.domain.dest;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.dozer.Mapping;

import lombok.Data;

@Entity
@Table(name = "USER_SETTINGS")
@NamedQuery(name = "DestUserSettings.findAll", query = "SELECT u FROM DestUserSettings u")
@Data
public class DestUserSettings implements Serializable {

  private static final long serialVersionUID = 6353100466100810921L;

  @Id
  @Column(name = "ID")
  private int id;

  @Column(name = "ACCEPT_HAPPY_POINT_TERM")
  private Boolean acceptHappyPointTerm;

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

  @Transient
  private Boolean hasPartnerProgramLogin;

  @Transient
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

  @Column(name = "SHOW_HAPPY_POINTS")
  private Boolean showHappyPoints;

  @Column(name = "SHOW_RECOMMENDED_RETAIL_PRICE")
  private Boolean showRecommendedRetailPrice;

  @Column(name = "USE_DEFAULT_SETTING")
  private Boolean useDefaultSetting;

  @Column(name = "VIEW_BILLING")
  private Boolean viewBilling;

  @Mapping("hasPartnerProgramView")
  public Boolean getShowHappyPoints() {
    return this.showHappyPoints;
  }

  @Mapping("hasPartnerProgramLogin")
  public Boolean getAcceptHappyPointTerm() {
    return this.acceptHappyPointTerm;
  }
}
