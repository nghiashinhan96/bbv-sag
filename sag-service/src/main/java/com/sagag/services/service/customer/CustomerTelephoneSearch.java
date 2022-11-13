package com.sagag.services.service.customer;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.elasticsearch.criteria.CustomerSearchCriteria;
import com.sagag.services.elasticsearch.criteria.Telephone;
import com.sagag.services.elasticsearch.domain.customer.CustomerDoc;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.ErpCustomerNotFoundException.SearchMode;
import com.sagag.services.service.exception.TelephoneFormatException;
import com.sagag.services.service.utils.TelephoneUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CustomerTelephoneSearch extends AbstractCustomerSearch {

  private static final String DEFAULT_COUNTRY_CODE = "43";

  @Override
  public CustomerDataResultDto search(final SupportedAffiliate affiliate, final String telephone,
      final Pageable pageable)
      throws TelephoneFormatException, ErpCustomerNotFoundException, UserValidationException {

    final String affiliateShortName = affiliate.getAffiliate();
    final List<String> supportedAffiliates =
        getAffiliateCompanyNamesWithinCountry(affiliateShortName);

    final CustomerDoc esCustomer = executeCustomerSearchByTelephone(telephone, supportedAffiliates)
        .orElseThrow(() -> new ErpCustomerNotFoundException(telephone, SearchMode.TEL));

    return searchFromCache(affiliateShortName, esCustomer);
  }

  private Optional<CustomerDoc> executeCustomerSearchByTelephone(String telephone,
      List<String> affiliates) throws TelephoneFormatException {
    final CustomerSearchCriteria criteria = new CustomerSearchCriteria();
    criteria.setAffiliates(affiliates);
    criteria.setTelephone(buildTelephone(telephone));
    if (!criteria.getTelephone().isPresent()) {
      log.error("Invalid search criteria, at least the phone exists");
      throw new UnsupportedOperationException("Invalid customer search arguments");
    }
    return customerESService.searchCustomerByTelephone(criteria);
  }

  private Optional<Telephone> buildTelephone(String tel) throws TelephoneFormatException {
    if (StringUtils.isBlank(tel) || !TelephoneUtils.matchesRegex(tel)) {
      log.error("Telephone does not match with the allowed pattern.");
      return Optional.empty();
    }
    final Optional<Telephone> telOpt = TelephoneUtils.extract(tel);
    if (telOpt.isPresent() && StringUtils.isBlank(telOpt.get().getCountryCode())) {
      // #1484: if no Country code, use default for the "mandant" - system setting
      // use default since the system setting has not been ready
      log.debug("Taking default Telephone Country Code = {}", DEFAULT_COUNTRY_CODE);
      telOpt.get().setCountryCode(DEFAULT_COUNTRY_CODE);
    }
    return telOpt;
  }
}
