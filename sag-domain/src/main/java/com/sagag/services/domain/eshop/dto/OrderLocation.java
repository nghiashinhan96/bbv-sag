package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLocation implements Serializable {

  private static final long serialVersionUID = -1395175550613147450L;

  private GrantedBranchDto branch;

  private List<PaymentMethodDto> paymentMethods;

  private List<DeliveryTypeDto> deliveryTypes;

  private List<CourierDto> courierServices;
}
