package com.sagag.services.domain.eshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFreeTextRequest implements Serializable {

  private static final long serialVersionUID = -7225307032172312137L;

  private String keyword = StringUtils.EMPTY;

  private Integer idMake;

  private Integer idModel;

  private String vehicleBrand;

  private String vehicleModel;

  private String make;

  private String model;

  private String builtYearFrom;

  private String builtYearTill;

  private String fuelType;

  private String powerKw;

  private String bodyType;

  private Integer size = 10;

  private Integer pageNumber = 0;
}
