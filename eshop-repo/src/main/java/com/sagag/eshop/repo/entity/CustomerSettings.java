package com.sagag.eshop.repo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@NamedQuery(name = "CustomerSettings.findAll", query = "SELECT o FROM CustomerSettings o")
@Data
@Builder
@Table(name = "CUSTOMER_SETTINGS")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSettings implements Serializable {

  private static final long serialVersionUID = 6353100466100810921L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "ALLOCATION_ID", nullable = false)
  private int allocationId;

  @Column(name = "DELIVERY_ID", nullable = false)
  private int deliveryId;

  @Column(name = "COLLECTIVE_DELIVERY_ID", nullable = false)
  private int collectiveDelivery;

  @Column(name = "NET_PRICE_VIEW", nullable = false)
  private boolean netPriceView;

  @Column(name = "NET_PRICE_CONFIRM", nullable = false)
  private boolean netPriceConfirm;

  @Column(name = "SHOW_DISCOUNT", nullable = false)
  private boolean showDiscount;

  @Column(name = "ALLOW_NET_PRICE_CHANGED", nullable = false)
  private boolean allowNetPriceChanged;

  @Column(name = "VIEW_BILLING", nullable = false)
  private boolean viewBilling;

  @Column(name = "ADDRESS_ID", nullable = false)
  private boolean addressId;

  @Column(name = "BILLING_ADDRESS_ID")
  private Long billingAddressId;

  @Column(name = "DELIVERY_ADDRESS_ID")
  private Long deliveryAddressId;

  @Column(name = "USE_DEFAULT_SETTING", nullable = false)
  private boolean useDefaultSetting;

  @Column(name = "EMAIL_NOTIFICATION_ORDER", nullable = false)
  private boolean emailNotificationOrder;

  @Column(name = "SESSION_TIMEOUT_SECONDS", nullable = false)
  private Integer sessionTimeoutSeconds;

  @Column(name = "NORMAUTO_DISPLAY")
  private boolean normautoDisplay;

  @OneToOne(mappedBy = "customerSettings")
  @JsonBackReference
  private Organisation organisation;

  @ManyToOne
  @JoinColumn(name = "INVOICE_TYPE", referencedColumnName = "ID")
  private InvoiceType invoiceType;

  @ManyToOne
  @JoinColumn(name = "PAYMENT_METHOD", referencedColumnName = "ID")
  private PaymentMethod paymentMethod;

  @Column(name = "HOME_BRANCH")
  private String homeBranch;

  @Column(name = "SHOW_OCI_VAT")
  private boolean showOciVat;

  @Column(name = "PRICE_DISPLAY_ID")
  private Integer priceDisplayId;

  private boolean demoCustomer;

  @Column(name = "HAS_PARTNER_PROGRAM_VIEW")
  private boolean hasPartnerprogramView;

  @Column(name = "WSS_SHOW_NET_PRICE")
  private boolean wssShowNetPrice;

  @Column(name = "WSS_MARGIN_GROUP")
  private Integer wssMarginGroup;

  @ManyToOne
  @JoinColumn(name = "WSS_DELIVERY_PROFILE_ID")
  private WssDeliveryProfile wssDeliveryProfile;

  @Column(name = "WSS_DELIVERY_ID")
  private Integer wssDeliveryId;
}
