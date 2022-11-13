package com.sagag.eshop.repo.entity;

import com.sagag.services.common.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DELIVERY_PROFILE")
public class DeliveryProfile implements Serializable {

  private static final long serialVersionUID = 6908543904134037333L;

  private static final int DF_VENDOR_CUT_OFF_TIME_HOUR = 18;

  private static final LocalTime DF_VENDOR_CUT_OFF_TIME =
      LocalTime.of(DF_VENDOR_CUT_OFF_TIME_HOUR, 0);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String country;

  @NotNull(message = "deliveryProfileId is null")
  private Integer deliveryProfileId;

  @NotNull(message = "distributionBranchId is null")
  private Integer distributionBranchId;

  @NotNull(message = "deliveryBranchId is null")
  private Integer deliveryBranchId;

  private Integer nextDay;

  private LocalTime vendorCutOffTime;

  @NotNull(message = "lastDelivery is null")
  private LocalTime lastDelivery;

  private LocalTime latestTime;

  @NotNull(message = "deliveryDuration is null")
  private Integer deliveryDuration;

  private Long createdUserId;

  private Date createdDate;

  private Long modifiedUserId;

  private Date modifiedDate;

  private String deliveryProfileName;

  public static class DeliveryProfileBuilder {

    public DeliveryProfileBuilder deliveryDuration(String deliveryDurationTime) {
      this.deliveryDuration = convertDuration(deliveryDurationTime);
      return this;
    }

    public DeliveryProfileBuilder vendorCutOffTime(Date vendorCutOffTime) {
      if (vendorCutOffTime == null) {
        this.vendorCutOffTime = DF_VENDOR_CUT_OFF_TIME;
        return this;
      }
      this.vendorCutOffTime = DateUtils.convertDateToLocalTime(vendorCutOffTime);
      return this;
    }

    public DeliveryProfileBuilder lastDelivery(Date lastDelivery) {
      this.lastDelivery = DateUtils.convertDateToLocalTime(lastDelivery);
      return this;
    }

    public DeliveryProfileBuilder latestTime(Date lastestTime) {
      this.latestTime = DateUtils.convertDateToLocalTime(lastestTime);
      return this;
    }

    public DeliveryProfileBuilder createdDate() {
      Calendar currentDate = Calendar.getInstance();
      this.createdDate = currentDate.getTime();
      return this;
    }

    public DeliveryProfileBuilder rawCountry(String countryCode) {
      this.country = countryCode;
      return this;
    }

    private Integer convertDuration(String durationtime) {
      if (StringUtils.isBlank(durationtime)) {
        return NumberUtils.INTEGER_ZERO;
      }
      LocalTime time = LocalTime.parse(durationtime,
          DateTimeFormatter.ofPattern(DateUtils.TIME_OPTIONAL_PATTERN));
      return time.toSecondOfDay();
    }
  }
}
