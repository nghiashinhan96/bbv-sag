package com.sagag.services.domain.eshop.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CustomerProfileSearchCriteria extends BaseRequest implements Serializable {

  private static final long serialVersionUID = 507724704250704574L;

  private String text;
  private String affiliate;
  private String customerNr;
  private String companyName;
  private CustomerSearchSortableColumn sorting;

}
