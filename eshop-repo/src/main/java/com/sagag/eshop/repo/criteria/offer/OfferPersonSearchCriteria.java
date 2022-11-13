package com.sagag.eshop.repo.criteria.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferPersonSearchCriteria {

  private String name;

  private String address;

  private String contactInfo;

  private Integer organisationId;

  private Boolean orderDescByName;

  private Boolean orderDescByAddress;

  private Boolean orderDescByContactInfo;
}
