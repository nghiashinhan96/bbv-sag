package com.sagag.services.domain.eshop.criteria;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LicenseSearchCriteria extends BaseRequest implements Serializable {

  private static final long serialVersionUID = 2057552512873255714L;

  private String affiliate;
  private String customerNr;
  private String packName;
  private String typeOfLicense;
  private String beginDate;
  private String endDate;
  private Integer quantity;
  private Integer quantityUsed;
  private LicenseSearchSortableColumn sorting;

  private int page;
  private int size;
}
