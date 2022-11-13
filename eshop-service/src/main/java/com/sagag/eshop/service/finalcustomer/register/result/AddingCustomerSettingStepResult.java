package com.sagag.eshop.service.finalcustomer.register.result;

import com.sagag.eshop.repo.entity.CustomerSettings;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddingCustomerSettingStepResult {

  private CustomerSettings customerSettings;
}
