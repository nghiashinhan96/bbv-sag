package com.sagag.services.article.api.domain.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalStockSearchCriteria {

  private String companyName;
  private String deliveryType;
  private String pickupBranch;
  private String defaultBranch;
}
