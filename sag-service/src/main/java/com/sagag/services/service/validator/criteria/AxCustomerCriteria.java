package com.sagag.services.service.validator.criteria;

import com.sagag.services.article.api.domain.customer.CustomerInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AxCustomerCriteria {

  private CustomerInfo customerInfo;

  private String affiliate;

  private String postCode;

}
