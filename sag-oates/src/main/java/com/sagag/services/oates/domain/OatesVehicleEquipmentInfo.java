package com.sagag.services.oates.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OatesVehicleEquipmentInfo implements Serializable {

  private static final long serialVersionUID = 66532319435173048L;

  @JsonProperty("@href")
  private String href;

  @JsonProperty("display_name_long")
  private String displayNameLong;

}
