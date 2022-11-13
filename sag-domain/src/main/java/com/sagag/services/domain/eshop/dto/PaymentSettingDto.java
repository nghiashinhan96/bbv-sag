package com.sagag.services.domain.eshop.dto;

import com.sagag.services.domain.sag.erp.Address;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PaymentSettingDto implements Serializable {

  private static final long serialVersionUID = 122584801263074609L;

  private List<AllocationTypeDto> allocationTypes;

  private List<PaymentMethodDto> paymentMethods;

  private List<Address> addresses;

  private List<Address> billingAddresses;

  private List<CollectiveDeliveryDto> collectiveTypes;

  private List<DeliveryTypeDto> deliveryTypes;

  private List<InvoiceTypeDto> invoiceTypes;

  private List<OrderLocation> orderLocations;
}
