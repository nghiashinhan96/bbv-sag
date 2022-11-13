package com.sagag.services.ax.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.TourTimeTable;

import lombok.Data;

/**
 * Class to receive the article availability from Dynamic AX ERP.
 *
 */
@Data
@JsonPropertyOrder(
    {
     "articleId",
     "quantity",
     "backOrder",
     "immediateDelivery",
     "arrivalTime",
     "tourName",
     "stockWarehouse",
     "deliveryWarehouse",
     "sendMethod",
     "tiresAvailabilityStatus"
    })
public class AxAvailability implements Serializable {

  private static final long serialVersionUID = 649336307275822014L;

  private String articleId;

  private Integer quantity;

  private Boolean backOrder;

  private String arrivalTime;

  private Boolean immediateDelivery;

  private String stockWarehouse;

  private String deliveryWarehouse;

  private String sendMethod;

  private String tourName;

  private List<AxTourTimeTable> tourTimeTable;

  private String errorMessage;

  private String deliveryTime;

  @JsonIgnore
  public boolean isValid() {
    return StringUtils.isBlank(errorMessage) && articleId != null;
  }

  @JsonIgnore
  private boolean hasTourTimeTable() {
    return CollectionUtils.isNotEmpty(this.tourTimeTable);
  }

  @JsonIgnore
  public Availability toAvailabilityDto() {

    List<TourTimeTable> timeTable = new ArrayList<>();
    if (hasTourTimeTable()) {
      this.tourTimeTable.forEach(axTourTimeTable ->
        timeTable.add(TourTimeTable.builder().startTime(axTourTimeTable.getStartTime())
            .tourName(axTourTimeTable.getTourName()).build()));
    }

    return Availability.builder().articleId(this.articleId)
        .quantity(this.quantity)
        .arrivalTime(this.arrivalTime)
        //DeliveryTime only using for Wint-Serbia
        .deliveryTime(this.deliveryTime)
        .backOrder(BooleanUtils.toBoolean(this.backOrder))
        .immediateDelivery(BooleanUtils.toBoolean(this.immediateDelivery))
        .tourName(this.tourName).stockWarehouse(this.stockWarehouse)
        .deliveryWarehouse(this.deliveryWarehouse)
        .sendMethodCode(this.sendMethod)
        .tourTimeTable(timeTable).build();
  }
}
