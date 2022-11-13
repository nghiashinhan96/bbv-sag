package com.sagag.services.service.validator;

import com.sagag.services.ax.domain.AxCustomerInfo;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.service.exception.customer.CustomerValidationException;
import com.sagag.services.service.exception.customer.CustomerValidationException.CustomerErrorCase;
import com.sagag.services.service.validator.criteria.AxCustomerCriteria;

import org.springframework.stereotype.Component;

@Component
@CzProfile
public class CzCustomerValidator extends CustomerValidator {

  @Override
  public boolean validate(AxCustomerCriteria criteria) throws CustomerValidationException {

    final AxCustomerInfo customerInfo = (AxCustomerInfo) criteria.getCustomerInfo();
    if (!customerInfo.equalsAffiliate(criteria.getAffiliate())) {
      throw new CustomerValidationException(CustomerErrorCase.CE_CBA_001,
          "Customer is not belong to " + criteria.getAffiliate());
    }

    // Verify post code #3468
    final String postCode = criteria.getPostCode();
    if (!customerInfo.equalsInvoicePostCode(postCode)) {
      final String msg = "Post code is not valid.";
      throw new CustomerValidationException(CustomerErrorCase.CE_IPC_001, msg);
    }

    final String custNr = String.valueOf(customerInfo.getCustomer().getNr());
    final boolean existsUsers = vUserDetailRepo.existsUsersByOrgCode(custNr);
    if (existsUsers) {
      final String errorMsg = String.format("User is existing in same customer %s.", custNr);
      throw new CustomerValidationException(CustomerErrorCase.CE_ICU_001, errorMsg);
    }
    return true;
  }

}
