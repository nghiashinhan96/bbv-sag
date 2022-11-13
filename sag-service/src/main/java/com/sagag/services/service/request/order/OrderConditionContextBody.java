package com.sagag.services.service.request.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.domain.eshop.dto.CourierDto;
import com.sagag.services.domain.eshop.dto.GrantedBranchDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({
  "pickupBranchIdOrDefault",
  "hasBillingAddr",
  "hasDeliveryAddr",
  "sendMethodType",
  "paymentMethodType",
  "singleInvoice",
  "partialDelivery" })
public class OrderConditionContextBody implements Serializable {

  private static final long serialVersionUID = 3242312315379352525L;

  @NotBlank(message = "Invoice type must not be empty")
  private String invoiceTypeCode;

  @NotBlank(message = "Payment method must not be empty")
  private String paymentMethod;

  @NotBlank(message = "Delivery type must not be empty")
  private String deliveryTypeCode;

  private String collectiveDeliveryCode;

  private String pickupBranchId;

  @NotBlank(message = "Billing address must not be empty")
  private String billingAddressId;

  @NotBlank(message = "Delivery address must not be empty")
  private String deliveryAddressId;

  private String referenceTextByLocation;

  private String messageToBranch;

  private GrantedBranchDto location;

  private CourierDto courier;

  public String getPickupBranchIdOrDefault(String defaultBranchId) {
    if (StringUtils.isBlank(pickupBranchId)) {
      return defaultBranchId;
    }
    return AxBranchUtils.getDefaultBranchIfNull(pickupBranchId);
  }

  public boolean hasBillingAddr() {
    return !StringUtils.isBlank(billingAddressId);
  }

  public boolean hasDeliveryAddr() {
    return !StringUtils.isBlank(deliveryAddressId);
  }

  public SendMethodType getSendMethodType() {
    if (StringUtils.isBlank(deliveryTypeCode)) {
      return SendMethodType.TOUR;
    }
    return SendMethodType.valueOf(deliveryTypeCode);
  }

  public PaymentMethodType getPaymentMethodType() {
    return PaymentMethodType.valueOfIgnoreCase(paymentMethod);
  }

  public boolean isSingleInvoice() {
    if (StringUtils.isBlank(invoiceTypeCode)) {
      return false;
    }
    ErpInvoiceTypeCode orderInvoiceType = ErpInvoiceTypeCode.valueOf(invoiceTypeCode);
    return ErpInvoiceTypeCode.SINGLE_INVOICE == orderInvoiceType
        || ErpInvoiceTypeCode.SINGLE_INVOICE_WITH_CREDIT_SEPARATION == orderInvoiceType;
  }

  public boolean isPartialDelivery() {
    if (StringUtils.isBlank(collectiveDeliveryCode)) {
      return false;
    }
    return StringUtils.equals("COLLECTIVE_DELIVERY1", collectiveDeliveryCode);
  }

  public String getCourierServiceId() {
    return Optional.ofNullable(this.courier)
        .filter(c -> Objects.nonNull(c) && getSendMethodType() == SendMethodType.COURIER)
        .map(CourierDto::getCourierServiceCode).orElse(null);
  }

  public String getLocationBranchId() {
    return Optional.ofNullable(this.location).filter(Objects::nonNull)
        .map(GrantedBranchDto::getBranchId).orElse(null);
  }

}
