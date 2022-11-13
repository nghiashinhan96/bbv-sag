package com.sagag.services.oates.domain;

import java.io.Serializable;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OatesEquipmentProducts implements Serializable {

  private static final long serialVersionUID = -8707480913620650594L;

  @JsonProperty("equipment")
  private OatesEquipment equipment;

  public boolean isValidOatesEquipmentProducts() {
    return this.getEquipment() != null
        && !CollectionUtils.isEmpty(this.getEquipment().getApplications());
  }
}
