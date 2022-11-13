package com.sagag.services.article.api.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Class to contain availability info to send to ERP.
 *
 */
@Data
public abstract class AvailabilityRequest implements Serializable {

  private static final long serialVersionUID = -7497761737258069514L;

  private Boolean isTourTimetable = Boolean.FALSE;
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

  /** The id of the address where the basketPositions will be delivered. */
  private String deliveryAddressId;

  /**
   * Abbreviation of the store where the customer will pick up the articles from, in case
   * sendMethodCode is PICKUP. For PICKUP with no abbreviation, the default of customer will be used
   */
  private String pickupBranchAbbreviation;

  /**
   * This attribute indicates whether the calculation will consider articles of the deprecated UMAR,
   * which are identically constructed parts or just the specific articleId which is set in the
   * basketPosition. Because umarId will not be supported by upcoming AX ERP system, it is strongly
   * recommended to provide specific article identifiers. Default is true, for the preferred article
   * identification schema.
   */
  private Boolean calcSpecificArticleIds;

  /**
   * Indicator for including the time which SAG needs to process a back order in case the whole
   * quantity is not available. In case of true and need of back order, the result of arrivalTime
   * will contain fictitious time. In case of false, the result for an article can be split to
   * available with arrivalTime and backOrder false and no arrivalTime (null) with backOrder true
   */
  private Boolean includeBackOrderTime;

  /**
   * List of article positions for which availabilities are requested. The list must contain at
   * least one item.
   */
  private List<BasketPosition> basketPositions;

  /**
   * Returns the relative availability URL.
   *
   * @return the availability url.
   */
  public abstract String getAvailabilityUrl();
}
