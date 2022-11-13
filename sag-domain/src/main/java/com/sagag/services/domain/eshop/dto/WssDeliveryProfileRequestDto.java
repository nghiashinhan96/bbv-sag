package com.sagag.services.domain.eshop.dto;


import com.sagag.services.common.enums.WssDeliveryProfileDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WssDeliveryProfileRequestDto implements Serializable {

  private static final long serialVersionUID = -6690906514663917303L;
  private Integer id;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  @NotNull
  private Integer wssBranchId;
  @NotNull
  private WssDeliveryProfileDay supplierTourDay;
  @NotNull
  private String supplierDepartureTime;
  @NotNull
  private Boolean isOverNight;
  private Integer wssTourId;
  private Integer pickupWaitDuration;
  private Integer wssDeliveryProfileTourId;
}
