package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VMessageSearchCriteria{

  private String title;

  private String type;

  private String area;

  private String subArea;

  private String locationValue;

  private Boolean active;

  private Boolean orderDescByTitle;

  private Boolean orderDescByType;

  private Boolean orderDescByArea;

  private Boolean orderDescBySubArea;

  private Boolean orderDescByActive;

  private Boolean orderDescByDateValidFrom;

  private Boolean orderDescByDateValidTo;
}
