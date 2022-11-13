package com.sagag.services.tools.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum VehicleField {

  ID_SAG(StringUtils.EMPTY, "id_sag"),
  CODES(StringUtils.EMPTY, "codes"),
  VEH_CODE_TYPE(CODES.field, "veh_code_type"),
  VEH_CODE_ATTR(CODES.field, "veh_code_attr"),
  VEH_CODE_VALUE(CODES.field, "veh_code_value");

  private String parent;

  private String field;

  public String getPath() {
    return this.getParent() + "." + this.getField();
  }
}
