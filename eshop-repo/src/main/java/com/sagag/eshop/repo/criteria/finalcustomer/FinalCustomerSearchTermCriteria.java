package com.sagag.eshop.repo.criteria.finalcustomer;

import com.sagag.services.common.enums.FinalCustomerStatus;
import com.sagag.services.common.enums.FinalCustomerType;

import lombok.Data;

import java.io.Serializable;

@Data
public class FinalCustomerSearchTermCriteria implements Serializable {

  private static final long serialVersionUID = -3036513554391306767L;

  private String name;

  private FinalCustomerType finalCustomerType;

  private String address;

  private String contactInfo;

  private FinalCustomerStatus status;

}
