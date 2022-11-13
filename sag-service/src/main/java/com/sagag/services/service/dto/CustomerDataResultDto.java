package com.sagag.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Data;

import org.springframework.data.domain.Page;

import java.io.Serializable;

@Data
public class CustomerDataResultDto implements Serializable {

  private static final long serialVersionUID = -5201108239049377285L;

  private Page<Customer> recommendCustomers;

  private Customer customer;

  private String admin;

  private Boolean isShopCustomer;

  public static CustomerDataResultDto of(final Customer customer, final String admin,
      final Boolean isShopCustomer) {
    final CustomerDataResultDto result = new CustomerDataResultDto();
    result.setCustomer(customer);
    result.setAdmin(admin);
    result.setIsShopCustomer(isShopCustomer);
    return result;
  }

  public static CustomerDataResultDto of(final Page<Customer> recommendCustomers) {
    final CustomerDataResultDto result = new CustomerDataResultDto();
    result.setRecommendCustomers(recommendCustomers);
    return result;
  }

  @JsonIgnore
  public boolean hasCustomer() {
    return customer != null;
  }

  @JsonIgnore
  public boolean hasRecommendCustomers() {
    return recommendCustomers != null && recommendCustomers.hasContent();
  }

  @JsonIgnore
  public boolean hasCustomerInfo() {
    return hasCustomer() || hasRecommendCustomers();
  }
}
