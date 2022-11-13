package com.sagag.services.domain.eshop.mail;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OrderConfirmationMail implements Serializable {

  private static final long serialVersionUID = 2159845507760045334L;

  private String lastName;
  private boolean isNetPriceView;
  private boolean isShowAvailability;
  private String affiliateEmail;
  private String shippingText;
  private String deliveryText;
  private String paymentMethodText;
  private String affiliateText;
  private String orderFrom;
  private String orderNr;
  private String customerRefText;
  private String branchRemark;
  private String customerNr;
  private String invoiceAddr;
  private String deliveryAddr;
  private boolean isFinalUser;
  private String finalCustomerNr;
  private String finalCustomerName;
  private boolean isChAffiliate;
  private boolean isReferenceTextShow;
  private String street;
  private String address1;
  private String address2;
  private String poBox;
  private String postCode;
  private String place;
  private boolean isShowFinalCustomerNetPrice;
  private String finalCustomerDeliveryAddress;
  private boolean isCzAffiliate;
}
