package com.sagag.eshop.service.finalcustomer.register;

import com.sagag.eshop.service.dto.finalcustomer.NewFinalCustomerDto;
import com.sagag.eshop.service.finalcustomer.register.result.NewFinalCustomerStepResult;
import com.sagag.services.common.step.AbstractStep;

public abstract class AbstractNewFinalCustomerStep
    extends AbstractStep<NewFinalCustomerDto, NewFinalCustomerStepResult> {

  @Override
  public boolean shouldHandleRequest(NewFinalCustomerDto input,
      NewFinalCustomerStepResult stepResult) {
    return true;
  }
}
