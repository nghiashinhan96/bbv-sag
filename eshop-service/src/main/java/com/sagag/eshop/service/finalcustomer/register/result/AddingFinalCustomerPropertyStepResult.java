package com.sagag.eshop.service.finalcustomer.register.result;

import com.sagag.eshop.repo.entity.FinalCustomerProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddingFinalCustomerPropertyStepResult {

  List<FinalCustomerProperty> finalCustomerProperties;
}
