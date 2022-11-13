package com.sagag.services.ax.request;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.article.api.request.ExternalOrderRequest;
import com.sagag.services.ax.enums.SendMethod;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The class to build order request send to AX connection.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxOrderClientRequest implements Serializable {

  private static final long serialVersionUID = -8804161819617921627L;

  private String customerNr;

  private SendMethod sendMethod;

  private boolean partialDelivery;

  private String paymentMethod;

  private String deliveryAddressId;

  private String pickupBranchId;

  private String pickupBranchAbbreviation;

  private String customerRefText;

  private String message;

  private boolean singleInvoice;

  private String personalNumber;

  private String salesOrigin;

  private String orderType;

  private List<AxOrderRequestItem> items;

  private String branchId;

  private String courierServiceCode;

  private String userName;
  
  private String email;
  
  public static AxOrderClientRequest of(ExternalOrderRequest request) {
    AxOrderRequest req = (AxOrderRequest) request;
    AxOrderClientRequest clientRequest = new AxOrderClientRequest();

    clientRequest.setUserName(request.getUsername());
    clientRequest.setEmail(request.getEmail());

    clientRequest.setCustomerNr(req.getCustomerNr());
    clientRequest.setSendMethod(SendMethod.valueOf(req.getSendMethodCode()));
    clientRequest.setPartialDelivery(BooleanUtils.toBoolean(req.getPartialDelivery()));
    clientRequest.setPaymentMethod(req.getPaymentMethod());
    clientRequest.setDeliveryAddressId(req.getDeliveryAddressId());
    clientRequest.setPickupBranchId(AxBranchUtils.getDefaultBranchIfNull(req.getPickupBranchId()));

    clientRequest.setPickupBranchAbbreviation(req.getPickupBranchAbbreviation());
    clientRequest.setCustomerRefText(req.getCustomerRefText());
    clientRequest.setMessage(req.getMessage());
    clientRequest.setSingleInvoice(BooleanUtils.toBoolean(req.getSingleInvoice()));

    clientRequest.setPersonalNumber(req.getPersonalNumber());

    // IMPORTANT(#1497): AX Connection: Adapt for APIs changes
    // Changes from #1648
    // #1648: @Lila suggest change from salesOriginId to salesOrigin as AX API updated from 2017/09/13
    // we will enable the param with old name till next release temporary.
    // 21-09-2017: From Simon:
    // 1) #1648 I snow changed so we can use "salesOriginID" to "orderRequest" and "basketRequest"
    clientRequest.setSalesOrigin(req.getSalesOriginId());

    //This branchId as mandatory field for Serbia Wint
    clientRequest.setBranchId(request.getBranchId());

    // #4488: Change courierServiceID to courierServiceCode
    clientRequest.setCourierServiceCode(request.getCourierServiceId());

    if (!StringUtils.isBlank(req.getOrderType())) {
      clientRequest.setOrderType(req.getOrderType());
    }

    List<AxOrderRequestItem> items = req.getBasketPositions().stream()
        .map(AxOrderRequestItem::copyWithCreateOrder).collect(Collectors.toList());
    clientRequest.setItems(items);

    return clientRequest;
  }

  @JsonIgnore
  @Override
  public String toString() {
    return SagJSONUtil.convertObjectToJson(this);
  }

}
