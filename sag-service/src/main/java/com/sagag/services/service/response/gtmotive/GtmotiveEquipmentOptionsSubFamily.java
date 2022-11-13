package com.sagag.services.service.response.gtmotive;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class GtmotiveEquipmentOptionsSubFamily implements Serializable {


  private static final long serialVersionUID = -5631136826901832571L;
  private String code;
  private String description;
  private List<GtmotiveEquipmentOptionsDto> equipmentOptions;
}
