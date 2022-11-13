package com.sagag.services.domain.eshop.criteria;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseSearchSortableColumn implements Serializable {

  private static final long serialVersionUID = 4294036299172942894L;

  private Boolean orderByCustomerNrDesc;
  private Boolean orderByPackNameDesc;
  private Boolean orderByTypeOfLicenseDesc;
  private Boolean orderByBeginDateDesc;
  private Boolean orderByEndDateDesc;
  private Boolean orderByQuantityDesc;
  private Boolean orderByQuantityUsedDesc;
}
