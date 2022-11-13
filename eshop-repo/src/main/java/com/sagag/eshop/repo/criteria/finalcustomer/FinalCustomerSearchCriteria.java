package com.sagag.eshop.repo.criteria.finalcustomer;

import lombok.Data;

import java.io.Serializable;

@Data
public class FinalCustomerSearchCriteria implements Serializable {

  private static final long serialVersionUID = -3681006157347222L;

  private FinalCustomerSearchTermCriteria term;

  private FinalCustomerSearchSortCriteria sort;
}
