package com.sagag.services.elasticsearch.criteria.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleSearchCriteria {

  @JsonProperty("term")
  private VehicleSearchTermCriteria searchTerm;

  @JsonProperty("filtering")
  private VehicleFilteringCriteria filtering;

  @JsonProperty("sort")
  private VehicleSearchSortCriteria sort;

  private boolean aggregation;

  @JsonIgnore
  public List<String> getVehicleCodes() {
    if (searchTerm == null) {
      return Collections.emptyList();
    }
    return ListUtils.emptyIfNull(searchTerm.getVehicleCodes());
  }

  public void updateFormattedVehicleData(String formattedVehData) {
    if (searchTerm == null) {
      return;
    }
    searchTerm.setVehicleData(formattedVehData);
  }

  public void updateVehicleCodes(List<String> vehCodes) {
    if (searchTerm == null) {
      return;
    }
    searchTerm.setVehicleCodes(vehCodes);
    searchTerm.setVehicleData(StringUtils.EMPTY);
  }

}
