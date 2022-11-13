package com.sagag.services.article.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Class to contain order request to send to ERP.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalOrderRequest implements Serializable {

  private static final long serialVersionUID = 4201825231335525560L;

  /**
   * Code for method of delivering the goods. Supported by Webshop services are TOUR and PICKUP out
   * of codes supported by ERP.
   */
  private String sendMethodCode;

  /**
   * Flag indicating the desire of basketPositions being delivered separately, in case they have
   * different arrivalTime.
   */
  private Boolean partialDelivery;

  private String paymentMethod;

  /** The id of the address where the basketPositions will be delivered. */
  private String deliveryAddressId;

  private String invoiceAddressId;

  private Boolean singleInvoice;

  private String customerRefText;

  private String message;

  private Boolean orderSpecificArticleIds;

  /**
   * Abbreviation of the store where the customer will pick up the articles from, in case
   * sendMethodCode is PICKUP. For PICKUP with no abbreviation, the default of customer will be used
   */
  private String pickupBranchAbbreviation;

  private String personalNumber;

  /**
   * List of article positions for which order are requested. The list must contain at least one
   * item.
   */
  private List<BasketPosition> basketPositions;

  private String pickupBranchId;

  private String orderType;

  private String salesOriginId;

  private String salesUsername;

  /*
   * branchId as a location id use for Serbia ordering
   */
  private String branchId;

  /*
   * courierServiceId as a selected Courier use for Serbia ordering
   */
  private String courierServiceId;
  
  private String username;
  
  private String email;
}
