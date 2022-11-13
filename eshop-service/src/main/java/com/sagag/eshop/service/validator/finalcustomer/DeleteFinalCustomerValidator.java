package com.sagag.eshop.service.validator.finalcustomer;

import com.sagag.eshop.repo.api.VFinalCustomerRepository;
import com.sagag.eshop.repo.entity.VFinalCustomer;
import com.sagag.eshop.service.exception.FinalCustomerValidationException;
import com.sagag.eshop.service.exception.FinalCustomerValidationException.FinalCustomerErrorCase;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.common.validator.IDataValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteFinalCustomerValidator implements IDataValidator<Integer> {

  @Autowired
  private VFinalCustomerRepository vFinalCustomerRepo;

  @Override
  public boolean validate(Integer finalCustomerOrgId) throws ValidationException {
    boolean inValid = vFinalCustomerRepo.findByOrgId(finalCustomerOrgId)
        .map(VFinalCustomer::getHasInProgressOrders).orElse(false);
    if (inValid) {
      throw new FinalCustomerValidationException(FinalCustomerErrorCase.FC_HIP_001,
          "Has order in progress for this final customer");
    }
    return inValid;
  }

}
