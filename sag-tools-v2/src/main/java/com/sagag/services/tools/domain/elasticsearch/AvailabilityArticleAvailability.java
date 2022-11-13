package com.sagag.services.tools.domain.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
//@formatter:off
@JsonPropertyOrder(
    {
     "quantity",
     "backOrder",
     "immediateDelivery",
     "arrivalTime",
     "tourName",
     "stockWarehouse",
     "deliveryWarehouse",
     "sendMethodCode",
     "tiresAvailabilityStatus"
    })
public class AvailabilityArticleAvailability implements Serializable {

  private static final long serialVersionUID = 7354606037313480105L;

  private Integer quantity;

  /**
   * Attribute indicating whether the requested quantity is not available and SAG needs to place a
   * back order in order to fulfill.
   */
  private Boolean backOrder;

  /** Available now!. */
  private Boolean immediateDelivery;

  /**
   * Date and Time which the tour will start or the re-stock will arrive in case of PICKUP.
   *
   * <p>Note: For PICKUP and article available immediately, the date and time can be in the past
   * i.e. <'TODAY'> <'SAG midnight'> expressed in Zulu. */
  private String arrivalTime;

  private String tourName;

  private String stockWarehouse;

  private String deliveryWarehouse;

  /**
   * Enumeration value of send method, ie. how goods are usually delivered to the customer. This
   * document lists all possible methods for delivering goods to customers.
   */
  private String sendMethodCode;

  /**
   * Tyres availability status: SAME_DAY_DELIVERY, NEXT_DAY_DELIVERY, NOT_AVAILABLE
   */
  private String tiresAvailabilityStatus;

}
