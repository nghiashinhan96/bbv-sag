package com.sagag.services.domain.eshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.services.common.enums.UserHistoryFromSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Vehicle history Dto class.
 */
@Data
@Builder
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@AllArgsConstructor
public class VehicleHistoryDto implements Serializable {

  private static final long serialVersionUID = -4674474846243895328L;

  @JsonProperty("vehicle_id")
  private String vehicleId;

  @JsonProperty("vehicle_name")
  private String vehicleName;

  @JsonProperty("search_term")
  private String searchTerm;

  private String searchMode;

  @JsonProperty("select_date")
  private Date selectDate;

  @JsonProperty("vehicle_class")
  private String vehicleClass;

  private String fullName;

  private boolean createdBySales;

  /**
   * Constructs the vehicle dto from vehicle info.
   *
   * @param vehicleId the vehicle id
   * @param vehicleName the vehicle name
   * @param selectDate the select date
   */
  public VehicleHistoryDto(final String vehicleId, final String vehicleName, final String vehClass, final Date selectDate) {
    this.vehicleId = vehicleId;
    this.vehicleName = vehicleName;
    this.vehicleClass = vehClass;
    this.selectDate = selectDate;
  }

  public VehicleHistoryDto(final String vehicleId, final String vehicleName, final String vehClass, final Date selectDate,
      String searchTerm, String searchMode, UserHistoryFromSource fromSource) {
    this(vehicleId, vehicleName, vehClass, selectDate);
    this.searchTerm = StringUtils.defaultString(searchTerm);
    this.searchMode = StringUtils.defaultString(searchMode);
    this.createdBySales = fromSource.isSalesOnbehalfMode();
  }

  public VehicleHistoryDto(String vehId, String vehName, String vehClass, Date selectDate, String searchTerm,
      String searchMode, String fullName, UserHistoryFromSource fromSource) {
    this(vehId, vehName, vehClass, selectDate, searchTerm, searchMode, fromSource);
    this.fullName = fullName;
  }
}
