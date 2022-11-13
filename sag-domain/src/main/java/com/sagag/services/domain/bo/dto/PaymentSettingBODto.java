package com.sagag.services.domain.bo.dto;

import com.sagag.services.domain.eshop.dto.AllocationTypeDto;
import com.sagag.services.domain.eshop.dto.CollectiveDeliveryDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.InvoiceTypeDto;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSettingBODto implements Serializable{
  
  private static final long serialVersionUID = 7710587471209942118L;
  private CustomerSettingsBODto customerSettingsBODto;
  private List<CollectiveDeliveryDto> collectiveDelivery;
  private List<DeliveryTypeDto> deliveryTypes;
  private List<InvoiceTypeDto> invoiceTypes;
  private List<AllocationTypeDto> allocation;
  private List<PaymentMethodDto> paymentMethods;
}
