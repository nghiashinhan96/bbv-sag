package com.sagag.services.domain.eshop.dto.externalvendor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DeliveryProfileDto implements Serializable {

  private static final long serialVersionUID = 6085091963092521662L;

  private Integer id;
  private String country;
  private Integer deliveryProfileId;
  private Integer distributionBranchId;
  private Integer deliveryBranchId;
  private Integer nextDay;
  private LocalTime vendorCutOffTime;
  private LocalTime lastDelivery;
  private LocalTime latestTime;
  private Integer deliveryDuration;
  private String deliveryProfileName;

  public boolean deliverOverNight() {
    return Objects.nonNull(nextDay) && nextDay > 0;
  }

  public DeliveryProfileDto(Integer deliveryProfileId, String deliveryProfileName) {
    this.deliveryProfileId = deliveryProfileId;
    this.deliveryProfileName = deliveryProfileName;
  }

  public boolean isValidConDelvieryProfile() {
    return Objects.nonNull(this.getNextDay());
  }
}
