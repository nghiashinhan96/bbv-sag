package com.sagag.eshop.repo.entity;

import com.sagag.services.common.contants.UserSettingConstants;
import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.util.Assert;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@NamedQueries(
    value = { @NamedQuery(name = "UserSettings.findAll", query = "SELECT o FROM UserSettings o") })
@Data
@Builder
@Table(name = "USER_SETTINGS")
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings implements Serializable {

  private static final long serialVersionUID = 6353100466100810921L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "ALLOCATION_ID")
  private int allocationId;

  @Column(name = "DELIVERY_ID")
  private int deliveryId;

  @Column(name = "COLLECTIVE_DELIVERY_ID")
  private int collectiveDelivery;

  @Column(name = "NET_PRICE_VIEW", nullable = false)
  private boolean netPriceView;

  @Column(name = "NET_PRICE_CONFIRM", nullable = false)
  private boolean netPriceConfirm;

  @Column(name = "SHOW_DISCOUNT", nullable = false)
  private boolean showDiscount;

  @Column(name = "VIEW_BILLING", nullable = false)
  private boolean viewBilling;

  @Column(name = "ADDRESS_ID")
  private int addressId;

  @Column(name = "BILLING_ADDRESS_ID")
  private String billingAddressId;

  @Column(name = "DELIVERY_ADDRESS_ID")
  private String deliveryAddressId;

  @Column(name = "EMAIL_NOTIFICATION_ORDER")
  private boolean emailNotificationOrder;

  @ManyToOne
  @JoinColumn(name = "INVOICE_TYPE", referencedColumnName = "ID")
  private InvoiceType invoiceType;

  @ManyToOne
  @JoinColumn(name = "PAYMENT_METHOD", referencedColumnName = "ID")
  private PaymentMethod paymentMethod;

  @Column(name = "SHOW_HAPPY_POINTS")
  private boolean showHappyPoints;

  @Column(name = "ACCEPT_HAPPY_POINT_TERM")
  private boolean acceptHappyPointTerm;

  @Column(name = "SALE_ON_BEHALF_OF", nullable = false)
  private boolean saleOnBehalfOf;

  @Column(name = "CURRENT_STATE_NET_PRICE_VIEW", nullable = false)
  private boolean currentStateNetPriceView;

  @Column(name = "CLASSIC_CATEGORY_VIEW")
  private Boolean classicCategoryView;

  @Column(name = "SINGLE_SELECT_MODE")
  private Boolean singleSelectMode;

  @Transient
  private boolean showRecommendedRetailPrice;

  @OneToOne(mappedBy = "userSetting", fetch = FetchType.LAZY)
  private EshopUser eshopUser;

  public void buildDefaultSettings(final SupportedAffiliate affiliate) {
    if (affiliate.isAtAffiliate() || affiliate.isChAffiliate()) {
      setShowRecommendedRetailPrice(true);
    } else {
      setShowRecommendedRetailPrice(false);
    }
  }

  public static UserSettings buildUserSettingsBy(SupportedAffiliate affiliate,
      CustomerSettings customerSettings) {
    Assert.notNull(affiliate, "The given affiliate must not be null");
    Assert.notNull(customerSettings, "The given customer settings must not be null");

    final UserSettings userSettings = new UserSettings();
    userSettings.setAllocationId(customerSettings.getAllocationId());
    userSettings.setCollectiveDelivery(customerSettings.getCollectiveDelivery());
    userSettings.setDeliveryId(customerSettings.getDeliveryId());
    userSettings.setPaymentMethod(customerSettings.getPaymentMethod());
    userSettings.setViewBilling(customerSettings.isViewBilling());
    userSettings.setNetPriceView(customerSettings.isNetPriceView());
    userSettings.setNetPriceConfirm(customerSettings.isNetPriceConfirm());
    userSettings.setEmailNotificationOrder(customerSettings.isEmailNotificationOrder());
    userSettings.setDeliveryAddressId(String.valueOf(customerSettings.getDeliveryAddressId()));
    userSettings.setBillingAddressId(String.valueOf(customerSettings.getBillingAddressId()));
    userSettings.setAcceptHappyPointTerm(false);
    userSettings.setSaleOnBehalfOf(true);
    userSettings.setClassicCategoryView(UserSettingConstants.CLASSIC_CATEGORY_VIEW_DEFAULT);
    userSettings.setSingleSelectMode(UserSettingConstants.SINGLE_SELECT_MODE_DEFAULT);
    userSettings.buildDefaultSettings(affiliate);
    userSettings.setCurrentStateNetPriceView(userSettings.isNetPriceView());
    userSettings.setInvoiceType(customerSettings.getInvoiceType());
    userSettings.setDeliveryId(customerSettings.getDeliveryId());
    return userSettings;
  }

}
