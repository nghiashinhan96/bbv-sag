package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonPropertyOrder({ "id", "allocationId", "paymentId", "deliveryId", "collectiveDelivery",
    "netPriceView", "allowNetPriceChanged", "netPriceConfirm", "viewBilling",
    "allowViewBillingChanged", "addressId", "billingAddressId", "deliveryAddressId",
    "emailNotificationOrder", "priceDisplaySettings" })
public class CustomerSettingsDto implements Serializable {

  private static final long serialVersionUID = -7550926489890016956L;

  private int id;

  private int allocationId;

  private int paymentId;

  private int deliveryId;

  private int collectiveDelivery;

  private boolean netPriceView;

  private boolean netPriceConfirm;

  private boolean viewBilling;

  private boolean addressId;

  private String billingAddressId;

  private String deliveryAddressId;

  private boolean emailNotificationOrder;

  private OrgPropertyOfferDto orgPropertyOffer;

  List<PriceDisplaySettingDto> priceDisplaySettings;

  private boolean wssShowNetPrice;
}
