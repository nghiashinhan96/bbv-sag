package com.sagag.eshop.service.dto.finalcustomer;

import com.sagag.services.domain.eshop.backoffice.dto.PermissionConfigurationDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SavingFinalCustomerDto implements Serializable {

  private static final long serialVersionUID = -2370651656447382330L;

  private String customerType;

  private Boolean isActive;

  private Boolean showNetPrice;

  private String customerNr;

  private String customerName;

  private String salutation;

  private String surName;

  private String firstName;

  private String street;

  private String addressOne;

  private String addressTwo;

  private String poBox;

  private String postcode;

  private String place;

  private String phone;

  private String fax;

  private String email;

  private List<PermissionConfigurationDto> perms;

  private WssDeliveryProfileDto wssDeliveryProfileDto;

  private Integer deliveryId;

  private Integer wssMarginGroup;
}
