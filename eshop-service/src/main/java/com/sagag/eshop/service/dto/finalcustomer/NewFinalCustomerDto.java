package com.sagag.eshop.service.dto.finalcustomer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewFinalCustomerDto extends SavingFinalCustomerDto implements Serializable {

  private static final long serialVersionUID = -3542155919368184823L;

  private Integer customerOrgId;

  private String collectionShortname;
}
