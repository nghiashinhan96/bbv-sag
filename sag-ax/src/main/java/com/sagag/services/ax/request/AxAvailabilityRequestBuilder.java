package com.sagag.services.ax.request;

import java.util.Collection;

import com.sagag.services.article.api.request.BasketPositionRequest;

import lombok.Data;

/**
 * The builder class to build availability request of {@link AxAvailabilityRequest}.
 *
 */
@Data
public class AxAvailabilityRequestBuilder {

  private String customerNr; // required
  private Collection<BasketPositionRequest> baskets; // required
  private String availabilityUrl; // optional
  private String addressId; // optional
  private String sendMethod; // optional
  private boolean partialDelivery; // optional
  private String pickupBranchId;
  private boolean tourTimetable;

  /**
   * Constructs the {@link AxAvailabilityRequestBuilder} instance from user and a list of baskets.
   *
   * @param customerNr the customer number.
   * @param baskets the basket requests
   */
  public AxAvailabilityRequestBuilder(final String customerNr,
      final Collection<BasketPositionRequest> baskets) {
    this.customerNr = customerNr;
    this.baskets = baskets;
  }

  /**
   * Builds the availability URL for ERP request.
   *
   * @param availabilityUrl the availability url
   * @return the {@link AxAvailabilityRequestBuilder}
   */
  public AxAvailabilityRequestBuilder availabilityUrl(final String availabilityUrl) {
    setAvailabilityUrl(availabilityUrl);
    return this;
  }

  /**
   * Builds the address id for request.
   *
   * @param addressId the address id
   * @return the {@link AxAvailabilityRequestBuilder}
   */
  public AxAvailabilityRequestBuilder addressId(final String addressId) {
    setAddressId(addressId);
    return this;
  }

  /**
   * Builds the send method code for request.
   *
   * @param sendMethod the send method code
   * @return the {@link AxAvailabilityRequestBuilder}
   */
  public AxAvailabilityRequestBuilder sendMethod(final String sendMethod) {
    setSendMethod(sendMethod);
    return this;
  }

  public AxAvailabilityRequestBuilder partialDelivery(final boolean partialDelivery) {
    setPartialDelivery(partialDelivery);
    return this;
  }

  public AxAvailabilityRequestBuilder pickupBranchId(final String pickupBranchId) {
    setPickupBranchId(pickupBranchId);
    return this;
  }

  public AxAvailabilityRequestBuilder isTourTimetable(final Boolean isTourTimetable) {
    setTourTimetable(isTourTimetable);
    return this;
  }

  /**
   * Creates the availability request to ERP.
   *
   * @return {@link AxAvailabilityRequest}
   */
  public AxAvailabilityRequest build() {
    return new AxAvailabilityRequest(this);
  }
}
