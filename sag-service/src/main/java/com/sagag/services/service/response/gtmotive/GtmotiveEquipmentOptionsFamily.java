package com.sagag.services.service.response.gtmotive;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class GtmotiveEquipmentOptionsFamily implements Serializable {

  private static final long serialVersionUID = -8895485549725753812L;
  private String code;
  private String description;
  private List<GtmotiveEquipmentOptionsSubFamily> subFamilies;
}
