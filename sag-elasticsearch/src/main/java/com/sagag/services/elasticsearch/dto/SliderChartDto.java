package com.sagag.services.elasticsearch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SliderChartDto {
  
  private String criteriaValue;
  
  private Integer numberOfArticles;

}
