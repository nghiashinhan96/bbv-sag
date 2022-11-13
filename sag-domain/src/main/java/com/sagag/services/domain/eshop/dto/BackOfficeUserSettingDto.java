package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackOfficeUserSettingDto {

  private long userId;
  private String userName;
  private int salutationId;
  private String companyName;

  private String firstName;
  private String lastName;
  private String email;
  private String telephone;
  private String fax;
  private int languageId;
  private int typeId;
  private Double hourlyRate;
  private Boolean emailNotificationOrder;
  private Boolean netPriceConfirm;
  private Boolean netPriceView;
  private Boolean showDiscount;

  private Integer deliveryId;
  private Integer collectiveDelivery;
  private Integer paymentId;
  private Boolean isUserActive;
}
