package com.sagag.services.domain.eshop.dto;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdvanceVehicleModel {

  private Map<String, List<ModelItem>> models;
  private List<Integer> years;
}
