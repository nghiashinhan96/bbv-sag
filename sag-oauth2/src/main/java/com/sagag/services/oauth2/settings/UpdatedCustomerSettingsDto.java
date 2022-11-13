package com.sagag.services.oauth2.settings;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.services.domain.sag.external.Customer;

import lombok.Data;

import java.util.Optional;

@Data
public class UpdatedCustomerSettingsDto {

  private CustomerSettings customerSettings;

  private Customer erpCustomer;

  public Optional<Customer> getCustomerOptional() {
    return Optional.ofNullable(erpCustomer);
  }

}
