package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SettingUpdateDto {
  private Long userId;
  private int allocationId;
  private int paymentId;
  private int invoiceId;
  private int deliveryId;
  private int collectiveDelivery;
  private boolean viewBilling;
  private boolean netPriceView;
  private boolean netPriceConfirm;
  private int addressId;
  private String billingAddressId;
  private String deliveryAddressId;
  private boolean emailNotificationOrder;
  private boolean classicCategoryView;
  private boolean singleSelectMode;
}
