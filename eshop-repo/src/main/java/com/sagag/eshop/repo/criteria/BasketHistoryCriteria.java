package com.sagag.eshop.repo.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BasketHistoryCriteria extends SearchCriteria {

  private static final long serialVersionUID = -8227264871494416866L;

  private String basketName;

  private Long createdUserId;

  private Long salesUserId;

  private String customerNumber;

  private String customerName;

  private String createdLastName;

  private String salesLastName;

  private Date updatedDate;

  private String customerRefText;

  private Boolean orderByDescCustomerNumber;

  private Boolean orderByDescCustomerName;

  private Boolean orderByDescBasketName;

  private Boolean orderByDescLastName;

  private Boolean orderByDescGrandTotalExcludeVat;

  private Boolean orderByDescUpdatedDate;

  private Boolean orderByDescCustomerRefText;

  private Boolean active;

}
