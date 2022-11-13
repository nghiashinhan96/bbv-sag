package com.sagag.services.elasticsearch.criteria.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.common.contants.SagConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleSearchTermCriteria {

  private Integer makeId;

  private Integer modelId;

  private String yearFrom;

  private String fuelType;

  private String vehicleDesc;

  private String vehicleData;

  private String freeText;

  private String motorCodeDesc;

  @JsonIgnore
  private List<String> vehicleCodes;

  @JsonIgnore
  public boolean hasVehData() {
    return !StringUtils.isBlank(vehicleData);
  }

  @JsonIgnore
  public String getFormattedVehData() {
    return StringUtils.defaultString(vehicleData).replaceAll(SagConstants.SPACE, StringUtils.EMPTY);
  }
}
