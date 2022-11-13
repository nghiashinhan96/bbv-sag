package com.sagag.services.domain.eshop.dto;

import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.CustomerBranch;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EshopBasketDto implements Serializable {

  private static final long serialVersionUID = 6749738500328292481L;

  private AllocationTypeDto allocation;

  private InvoiceTypeDto invoiceType;

  private PaymentMethodDto paymentMethod;

  private DeliveryTypeDto deliveryType;

  private CollectiveDeliveryDto collectionDelivery;

  private Address billingAddress;

  private Address deliveryAddress;

  private CustomerBranch pickupBranch;

  private String orderType;

  private Boolean showKSLMode;

  private GrantedBranchDto location;

  private GrantedBranchDto pickupLocation;

  private CourierDto courierService;

  private String referenceTextByLocation;

  private String messageToBranch;

  private List<EshopBasketDto> eshopBasketContextByLocation;

  private List<TourTimeDto> tourTimes;

}
