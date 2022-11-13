package com.sagag.services.oates.domain;

import java.io.Serializable;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "display_name_long"})
public class OatesRecommendVehicles implements Serializable {

  private static final long serialVersionUID = -6635129530994566832L;

  @JsonProperty("equipment_list")
  private OatesVehicleEquipment equipmentList;

  public boolean isValidOatesVehicle() {
    return this.getEquipmentList() != null
        && !CollectionUtils.isEmpty(this.getEquipmentList().getEquipment());
  }

}
