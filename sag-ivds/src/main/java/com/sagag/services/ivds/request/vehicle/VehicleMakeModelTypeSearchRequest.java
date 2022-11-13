package com.sagag.services.ivds.request.vehicle;

import com.sagag.services.common.enums.SupportedAffiliate;

import lombok.Data;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

@Data
public class VehicleMakeModelTypeSearchRequest implements Serializable {

  private static final long serialVersionUID = 7264021516494897010L;

  private SupportedAffiliate affiliate;

  private String vehicleType;

  private String makeCode;

  private String yearFrom;

  private String modelCode;

  private String fuelType;

  private String typeCode;

  private String cubicCapacity;

  private List<String> vehicleSubClass;

  public boolean isRequestMakeAggForCar() {
    return !isRequestForMotorbike() && Stream
        .of(this.getMakeCode(), this.getModelCode(), this.getTypeCode(), this.getFuelType())
        .allMatch(StringUtils::isBlank);
  }

  public boolean isRequestMakeAggForCMotorbike() {
    return isRequestForMotorbike() && Stream
        .of(this.getMakeCode(), this.getModelCode(), this.getCubicCapacity(), this.getYearFrom())
        .allMatch(StringUtils::isBlank);
  }

  public boolean isRequestModelAggForCar() {
    return !isRequestForMotorbike() && !StringUtils.isBlank(this.getMakeCode())
        && Stream.of(this.getModelCode(), this.getTypeCode(), this.getFuelType())
            .allMatch(StringUtils::isBlank);
  }

  public boolean isRequestModelAggForMotorbike() {
    return isRequestForMotorbike()
        && Stream.of(this.getMakeCode(), this.getCubicCapacity()).allMatch(StringUtils::isNotBlank)
        && Stream.of(this.getModelCode(), this.getYearFrom()).allMatch(StringUtils::isBlank);
  }

  public boolean isRequestTypeAgg() {
    return !isRequestForMotorbike()
        && StringUtils.isNoneBlank(this.getMakeCode(), this.getModelCode())
        && StringUtils.isBlank(this.getTypeCode());
  }

  public boolean isRequestYearAgg() {
    return isRequestForMotorbike() && StringUtils.isNoneBlank(this.getMakeCode(),
        this.getCubicCapacity(), this.getModelCode());
  }

  public boolean isRequestCupicCapacityAgg() {
    return isRequestForMotorbike() && StringUtils.isNoneBlank(this.getMakeCode())
        && Stream.of(this.getCubicCapacity(), this.getModelCode(), this.getYearFrom())
            .allMatch(StringUtils::isBlank);
  }

  public boolean isRequestForMotorbike() {
    return StringUtils.equalsIgnoreCase(VehicleClass.MOTORBIKE.vehicleClassShortName,
        this.getVehicleType()) && CollectionUtils.isNotEmpty(vehicleSubClass);
  }
}
