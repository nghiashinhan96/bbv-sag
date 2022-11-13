package com.sagag.services.hazelcast.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.common.enums.order.OrderType;
import com.sagag.services.domain.eshop.dto.AllocationTypeDto;
import com.sagag.services.domain.eshop.dto.CollectiveDeliveryDto;
import com.sagag.services.domain.eshop.dto.CourierDto;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;
import com.sagag.services.domain.eshop.dto.InvoiceTypeDto;
import com.sagag.services.domain.eshop.dto.PaymentMethodDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.CustomerBranch;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Data
public class EshopBasketContext implements Serializable {

  private static final long serialVersionUID = -2837366933947588212L;

  private AllocationTypeDto allocation;

  private InvoiceTypeDto invoiceType;

  private PaymentMethodDto paymentMethod;

  private DeliveryTypeDto deliveryType;

  private CollectiveDeliveryDto collectionDelivery;

  private CustomerBranch pickupBranch;

  private CustomerBranch finalCustomerPickupBranch;

  private Address billingAddress;

  private Address deliveryAddress;

  private OrderType orderType; // ORDER OR COUNTER type

  private Boolean showKSLMode;

  private String referenceTextByLocation;

  private String messageToBranch;

  private CourierDto courierService;

  private GrantedBranchDto location;

  private GrantedBranchDto pickupLocation;

  private List<EshopBasketContext> eshopBasketContextByLocation;

  private List<TourTimeDto> tourTimes;

  @JsonIgnore
  public String getDeliveryTypeDescCode() {
    if (!hasDeliveryTypeCode()) {
      return StringUtils.EMPTY;
    }
    return this.deliveryType.getDescCode();
  }

  @JsonIgnore
  public boolean hasDeliveryTypeCode() {
    return this.deliveryType != null && !StringUtils.isBlank(this.deliveryType.getDescCode());
  }

  @JsonIgnore
  public String getPaymentMethodDescCode() {
    if (!hasPaymentMethodCode()) {
      return StringUtils.EMPTY;
    }
    return this.paymentMethod.getDescCode();
  }

  @JsonIgnore
  public boolean hasPaymentMethodCode() {
    return this.paymentMethod != null && !StringUtils.isBlank(this.paymentMethod.getDescCode());
  }

  @JsonIgnore
  public Optional<Address> getBillingAddressOptional() {
    return Optional.ofNullable(this.billingAddress);
  }

  @JsonIgnore
  public boolean hasBillingAddressId() {
    return this.billingAddress != null && !StringUtils.isBlank(this.billingAddress.getId());
  }

  @JsonIgnore
  public boolean hasDeliveryAddressId() {
    return this.deliveryAddress != null && !StringUtils.isBlank(this.deliveryAddress.getId());
  }

  @JsonIgnore
  public String getDeliveryAddressId() {
    return getDeliveryAddressOptional().map(Address::getId).orElse(StringUtils.EMPTY);
  }

  @JsonIgnore
  public String getOrDefaultDeliveryAddressId(String defaultAddressId) {
    if (!this.hasDeliveryAddressId()) {
      return defaultAddressId;
    }
    return this.getDeliveryAddressId();
  }

  private Optional<Address> getDeliveryAddressOptional() {
    return Optional.ofNullable(this.deliveryAddress);
  }
}
