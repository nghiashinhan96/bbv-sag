package com.sagag.services.rest.resource;

import com.sagag.services.ax.domain.AxCreditLimitInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.service.dto.CustomerDataResultDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.ResourceSupport;

/**
 * Customer response class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerDataResource extends ResourceSupport {

  private Page<Customer> recommendCustomers;

  private Customer customer;

  private String admin;

  private AxCreditLimitInfo creditLimitInfo;

  private Boolean isShopCustomer;

  public CustomerDataResource(CustomerDataResultDto customerDataResult) {
    this.recommendCustomers = customerDataResult.getRecommendCustomers();
    this.admin = customerDataResult.getAdmin();
    this.customer = customerDataResult.getCustomer();
    this.isShopCustomer = customerDataResult.getIsShopCustomer();
  }

}
