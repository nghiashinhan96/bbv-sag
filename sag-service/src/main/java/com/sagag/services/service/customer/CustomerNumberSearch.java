package com.sagag.services.service.customer;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.elasticsearch.criteria.CustomerSearchCriteria;
import com.sagag.services.elasticsearch.domain.customer.CustomerDoc;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.ErpCustomerNotFoundException.SearchMode;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerNumberSearch extends AbstractCustomerSearch {

  @Override
  public CustomerDataResultDto search(final SupportedAffiliate affiliate, final String custNr,
      final Pageable pageable) throws ErpCustomerNotFoundException, UserValidationException {

    final List<String> supportedAffiliates =
        getAffiliateCompanyNamesWithinCountry(affiliate.getAffiliate());

    final CustomerDoc esCustomer = executeCustomerSearchByCustomerNr(custNr, supportedAffiliates)
        .orElseThrow(() -> new ErpCustomerNotFoundException(custNr, SearchMode.CUSTNR));
    return searchFromCache(affiliate.getAffiliate(), esCustomer);
  }

  private Optional<CustomerDoc> executeCustomerSearchByCustomerNr(String customerNr,
      List<String> affiliates) {
    final CustomerSearchCriteria criteria = new CustomerSearchCriteria();
    criteria.setAffiliates(affiliates);
    criteria.setCustomerNumber(customerNr);
    return customerESService.searchCustomerByCustomerNumber(criteria);
  }
}
