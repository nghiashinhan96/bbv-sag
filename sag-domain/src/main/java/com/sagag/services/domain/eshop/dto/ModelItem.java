package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ModelItem {

  private Integer modelId;

  private String model;

  private Integer modelDateBegin;

  private Integer modelDateEnd;

  private Integer sort;
  
  private Integer idMake;

  private String modelSeries;

  private String modelGen;

}
