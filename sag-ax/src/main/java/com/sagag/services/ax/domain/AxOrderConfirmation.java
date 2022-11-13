package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.domain.sag.erp.OrderAvailabilityResponse;
import com.sagag.services.domain.sag.erp.OrderConfirmation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class to receive the order confirmation info from Dynamic AX ERP.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AxOrderConfirmation implements Serializable {

  private static final long serialVersionUID = 798486424438671125L;

  private String orderNr;

  private String axOrderURL;

  private boolean allPositionsAvailable;

  private boolean creditLimitExceeded;

  private AxAvailabilityResourceSupport effectiveAvailabilityLines;

  private List<String> workIds;

  @JsonProperty("_links")
  private Map<String, Link> links;

  @JsonIgnore
  public OrderConfirmation toOrderConfirmationDto() {
    return OrderConfirmation.builder()
        .orderNr(this.orderNr)
        .axOrderURL(this.axOrderURL)
        .workIds(this.workIds)
        .links(this.links)
        .orderAvailabilities(buildAvailabilityResponse())
        .build();
  }
  
  private List<OrderAvailabilityResponse> buildAvailabilityResponse() {
    List<OrderAvailabilityResponse> result = new ArrayList<>();
    if (!Objects.isNull(effectiveAvailabilityLines)
        && CollectionUtils.isNotEmpty(effectiveAvailabilityLines.getAvailabilities())) {
      effectiveAvailabilityLines.getAvailabilities().stream().map(AxAvailability::toAvailabilityDto)
          .forEach(avail -> {
            result.add(OrderAvailabilityResponse.builder().availabilityResponse(avail).build());
          });
    }
    return result;
  }

}
