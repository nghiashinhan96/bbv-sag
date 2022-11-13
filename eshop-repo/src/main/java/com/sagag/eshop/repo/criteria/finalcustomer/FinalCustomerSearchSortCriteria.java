package com.sagag.eshop.repo.criteria.finalcustomer;

import lombok.Data;

import java.io.Serializable;

@Data
public class FinalCustomerSearchSortCriteria implements Serializable {

  private static final long serialVersionUID = -224776128129042167L;

  private Boolean orderDescByName;

}
