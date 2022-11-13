package com.sagag.services.service.customer;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.elasticsearch.api.CustomerSearchService;
import com.sagag.services.elasticsearch.domain.customer.CustomerDoc;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.ErpCustomerNotFoundException.SearchMode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractCustomerSearch implements ICustomerSearch {

  @Autowired
  protected CustomerSearchService customerESService;
  @Autowired
  private SupportedAffiliateRepository supportedAffiliateRepo;
  @Autowired
  private CustomerCacheService customerCacheService;
  @Autowired
  private UserService userService;

  protected List<String> getAffiliateCompanyNamesWithinCountry(String affiliateShortName) {
    if (StringUtils.isBlank(affiliateShortName)) {
      return Collections.emptyList();
    }
    return supportedAffiliateRepo.findByWithinCountryOfShortName(affiliateShortName).stream()
        .map(entity -> StringUtils.lowerCase(entity.getCompanyName())).collect(Collectors.toList());
  }

  protected CustomerDataResultDto searchFromCache(final String affiliateShortName,
      final CustomerDoc esCustomer) throws ErpCustomerNotFoundException, UserValidationException {
    final Optional<com.sagag.eshop.repo.entity.SupportedAffiliate> supportedAffiliateOpt =
        supportedAffiliateRepo.findFirstByShortName(affiliateShortName);
    if (!supportedAffiliateOpt.isPresent()) {
      final String message =
          String.format("No support the affiliate = %s within country", affiliateShortName);
      throw new IllegalArgumentException(message);
    }
    final String companyName = supportedAffiliateOpt.get().getCompanyName();
    final String customerNr = esCustomer.getCustomerAccountNr();
    final Optional<Customer> customerOpt = customerCacheService.getCachedCustomer(
        customerNr, companyName);
    if (!customerOpt.isPresent()) {
      throw new ErpCustomerNotFoundException(customerNr, SearchMode.CUSTNR);
    }
    return buildNecessaryCustomerData(customerOpt.get(), esCustomer.getIsShopCustomer());
  }

  private CustomerDataResultDto buildNecessaryCustomerData(Customer customer,
      Boolean isShopCustomer) throws UserValidationException {
    // Get admin user name
    final String adminUsername =
        userService.searchCustomerAdminUser(String.valueOf(customer.getNr()));
    if (StringUtils.isBlank(adminUsername)) {
      throw new UserValidationException(UserErrorCase.UE_UBN_001, "Not found user on behalf");
    }
    return CustomerDataResultDto.of(customer, adminUsername, isShopCustomer);
  }
}
