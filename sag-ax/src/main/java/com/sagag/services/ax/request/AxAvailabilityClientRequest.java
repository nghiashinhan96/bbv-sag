package com.sagag.services.ax.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.article.api.request.AvailabilityRequest;
import com.sagag.services.ax.enums.SendMethod;
import com.sagag.services.ax.utils.AxBranchUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The class to build AX availability request to send to AX API.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AxAvailabilityClientRequest implements Serializable {

  private static final long serialVersionUID = 2236408951616632321L;

  private String customerNr;

  private String sendMethod;

  private boolean partialDelivery;

  private Boolean isTourTimetable;

  private String deliveryAddressId;

  private String pickupBranchId;

  private List<AxBasketItem> items;

  public static AxAvailabilityClientRequest copy(AvailabilityRequest request) {
    AxAvailabilityRequest req = (AxAvailabilityRequest) request;
    AxAvailabilityClientRequest clientRequest = new AxAvailabilityClientRequest();
    clientRequest.setIsTourTimetable(req.getIsTourTimetable());
    clientRequest.setCustomerNr(req.getCustomerNr());
    clientRequest.setDeliveryAddressId(String.valueOf(req.getDeliveryAddressId()));
    clientRequest.setSendMethod(req.getSendMethodCode());
    clientRequest.setPartialDelivery(req.getPartialDelivery());

    if (SendMethod.ABH.equals(SendMethod.valueOf(clientRequest.getSendMethod()))) {
      clientRequest.setPickupBranchId(AxBranchUtils.getDefaultBranchIfNull(req.getPickupBranchId()));
    }

    List<AxBasketItem> items = req.getBasketPositions().stream()
        .map(AxBasketItem::copy).collect(Collectors.toList());
    clientRequest.setItems(items);
    return clientRequest;
  }

}
