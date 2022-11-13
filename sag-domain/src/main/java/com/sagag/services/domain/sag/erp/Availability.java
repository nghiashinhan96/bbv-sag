package com.sagag.services.domain.sag.erp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.Iterables;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Objects;

/**
 * Class to define the properties of availability of an article.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "umarId", "articleId", "leadingArticleId", "quantity", "backOrder",
    "immediateDelivery", "arrivalTime", "tourName", "stockWarehouse", "deliveryWarehouse",
    "sendMethodCode", "tourTimeTable", "availState", "availStateColor", "sofort" })
public class Availability extends ErpArticleAvailability {

  private static final long serialVersionUID = 649336307275822014L;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long umarId;

  @JsonSerialize(using = ToStringSerializer.class)
  private String articleId;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long leadingArticleId;

  private Integer quantity;

  @JsonProperty("backOrder")
  private Boolean backOrder;

  @JsonProperty("immediateDelivery")
  private boolean immediateDelivery;

  private String arrivalTime;

  @JsonIgnore
  private String arrivalTimeAtBranch;

  private String deliveryTime;

  private String tourName;

  private String stockWarehouse;

  private String deliveryWarehouse;

  private String sendMethodCode;

  private List<TourTimeTable> tourTimeTable;

  // immediate delivery
  private boolean sofort;

  private boolean externalSource;

  private boolean venExternalSource;

  private boolean conExternalSource;

  @JsonProperty("externalVendorId")
  private Long vendorId;

  private LocationAvailability location;

  @JsonProperty("externalVendorName")
  private String vendorName;

  @JsonIgnore
  private String externalVendorTypeId;

  @JsonIgnore
  public DateTime getCETArrivalTime() { // this code is running on CET server
    return DateUtils.getCETDateTime(arrivalTime); // implicit timezone conversion
  }

  @JsonIgnore
  public DateTime getDateTimeArrivalTime() {
    return getCETArrivalTime();
  }

  public String getFormattedCETArrivalTime() { // this code is running on CET server
    // #1419: HH:mm -> HH.mm
    // #5241: HH.mm -> HH:mm
    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("HH:mm");
    if (Objects.isNull(this.arrivalTime)) {
      return null;
    }
    return dateFormatter.print(getCETArrivalTime());
  }

  public String getFormattedCETArrivalDate() { // this code is running on CET server
    // 23.08.2017 15:00
    DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");
    if (Objects.isNull(this.arrivalTime)) {
      return null;
    }
    return dateFormatter.print(getCETArrivalTime());
  }

  @JsonIgnore
  public DateTime getUTCArrivalTime() { // this code is running on CET server
    return new DateTime(arrivalTime, DateTimeZone.UTC); // implicit timezone conversion
  }

  @JsonIgnore
  public DateTime getUTCArrivalTimeAtBranch() {
    return new DateTime(arrivalTimeAtBranch, DateTimeZone.UTC);
  }

  @JsonIgnore
  public TourTimeTable getLastTour() {
    if (!hasTourTimeTable()) {
      return null;
    }
    return Iterables.getLast(tourTimeTable);
  }

  @JsonIgnore
  public TourTimeTable getZKNextWorkingTour(DateTime pickUpZKNextWorkingDateTime) {
    for (TourTimeTable tour : tourTimeTable) {
      DateTime time = tour.getCETStartTime();
      if (time.isAfter(pickUpZKNextWorkingDateTime)) {
        return tour;
      }
    }
    // handle exception from AX API
    return getLastTour();
  }

  @JsonIgnore
  public boolean isPickupMode() {
    return SendMethodType.PICKUP.name().equals(this.getSendMethodCode());
  }

  @JsonIgnore
  public boolean hasTourTimeTable() {
    return CollectionUtils.isNotEmpty(tourTimeTable);
  }

  @JsonIgnore
  public boolean isDelivery24Hours() {
    return getAvailState() == SagConstants.DELIVERY_IN_24_HOURS;
  }

  @JsonIgnore
  public boolean isBackOrderTrue() {
    return backOrder != null && backOrder.booleanValue();
  }

  @JsonIgnore
  public boolean isNonVenExternal() {
    return !venExternalSource;
  }

  @JsonIgnore
  public int getDefaultQuantity() {
    if (quantity == null) {
      return 0;
    }
    return quantity.intValue();
  }

}
