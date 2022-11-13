package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsDto {

  private int id;

  private int allocationId;

  private int paymentId;

  private int invoiceId;

  private int deliveryId;

  private int collectiveDelivery;

  private boolean viewBilling;

  private boolean netPriceView;

  private boolean netPriceConfirm;

  private boolean showDiscount;

  private boolean showGross;

  private String billingAddressId;

  private String deliveryAddressId;

  private boolean emailNotificationOrder;

  private PriceSettingDto priceSettings;

  private boolean classicCategoryView;

  private boolean priceDisplayChanged;

  private boolean singleSelectMode;

}
