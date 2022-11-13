package com.sagag.eshop.service.dto.offer;

import com.sagag.eshop.repo.entity.offer.ViewOffer;
import com.sagag.services.common.contants.SagConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferGeneralDto {

  private Long id;

  private String offerNumber;

  private String customerName;

  private String remark;

  /**
   * vehicle information from all offer positions
   */
  private Set<String> vehicleDescs;

  private Date offerDate;

  private Double price;

  private String status;

  private String username;

  public OfferGeneralDto(final ViewOffer offer) {
    this.id = offer.getId();
    this.offerNumber = offer.getOfferNumber();
    this.customerName = offer.getCustomerName();
    this.remark = offer.getRemark();
    this.vehicleDescs = getVehicleDescs(offer.getVehicleDescriptions());
    this.offerDate = offer.getOfferDate();
    this.price = offer.getTotalGrossPrice();
    this.status = offer.getStatus();
    this.username = offer.getOwnerUsername();
  }

  private Set<String> getVehicleDescs(final String vehicleDescriptions) {
    if (StringUtils.isEmpty(vehicleDescriptions)) {
      return Collections.emptySet();
    }
    return new LinkedHashSet<>(Arrays.asList(vehicleDescriptions.split(SagConstants.SEMICOLON)));
  }

}
