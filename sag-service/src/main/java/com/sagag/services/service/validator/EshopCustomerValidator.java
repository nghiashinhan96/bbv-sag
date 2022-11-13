package com.sagag.services.service.validator;

import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.services.common.validator.IDataValidator;
import com.sagag.services.service.exception.customer.CustomerValidationException;
import com.sagag.services.service.exception.customer.CustomerValidationException.CustomerErrorCase;
import com.sagag.services.service.request.registration.RetrievedCustomerRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EshopCustomerValidator implements IDataValidator<RetrievedCustomerRequest> {

  @Autowired
  private OrganisationService orgService;

  @Override
  public boolean validate(RetrievedCustomerRequest criteria) throws CustomerValidationException {
    final String custNr = criteria.getCustomerNumber();
    if (StringUtils.isBlank(custNr) || !StringUtils.isNumeric(custNr)) {
      final String msg = String.format("Customer number is not valid: %s", custNr);
      throw new CustomerValidationException(CustomerErrorCase.CE_ICT_001, msg);
    }

    // Find organisation by short name
    final String affiliateShortName = criteria.getAffiliate();
    final String companyName = orgService.searchCompanyName(affiliateShortName)
        .orElse(StringUtils.EMPTY);
    if (StringUtils.isBlank(companyName)) {
      final String msg = String.format("Invalid affiliate = %s.", affiliateShortName);
      throw new CustomerValidationException(CustomerErrorCase.CE_IAF_001, msg);
    }
    return false;
  }

}
