package com.sagag.services.tools.domain.target;

import com.sagag.services.tools.support.SupportedAffiliate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@NamedQueries(value = { @NamedQuery(name = "UserSettings.findAll", query = "SELECT o FROM UserSettings o") })
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

  @ManyToOne
  @JoinColumn(name = "DELIVERY_ID", referencedColumnName = "ID")
  private DeliveryType deliveryType;

  @ManyToOne
  @JoinColumn(name = "COLLECTIVE_DELIVERY_ID", referencedColumnName = "ID")
  private CollectiveDelivery collectiveDelivery;

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

  @Transient
  private boolean showRecommendedRetailPrice;

  public void buildDefaultSettings(final SupportedAffiliate affiliate) {
    if (affiliate.isAtAffiliate() || affiliate.isChAffiliate()) {
      setShowRecommendedRetailPrice(true);
      setNetPriceView(true);
    } else {
      setShowRecommendedRetailPrice(false);
      setNetPriceView(false);
      setNetPriceConfirm(false);
      setShowDiscount(false);
    }
  }

}
