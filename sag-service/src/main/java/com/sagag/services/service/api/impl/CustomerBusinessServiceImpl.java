package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.entity.SettingsKeys;
import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.ax.domain.AxCustomerInfo;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.bo.dto.CustomerBODto;
import com.sagag.services.domain.eshop.criteria.CustomerProfileSearchCriteria;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.domain.eshop.dto.organisation.EshopCustomerDto;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.service.api.CustomerBusinessService;
import com.sagag.services.service.customer.CustomerFreetextSearch;
import com.sagag.services.service.customer.CustomerNumberSearch;
import com.sagag.services.service.customer.CustomerTelephoneSearch;
import com.sagag.services.service.customer.registration.APMCustomerRegistrationHandler;
import com.sagag.services.service.customer.registration.FirstCustomerRegistrationHandler;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.dto.CustomerInfoDto;
import com.sagag.services.service.dto.registration.PotentialCustomerRegistrationDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.ErpCustomerNotFoundException.SearchMode;
import com.sagag.services.service.exception.TelephoneFormatException;
import com.sagag.services.service.exception.customer.CustomerValidationException;
import com.sagag.services.service.exception.customer.CustomerValidationException.CustomerErrorCase;
import com.sagag.services.service.mail.registration.PotentialCustomerRegistrationCriteria;
import com.sagag.services.service.mail.registration.PotentialCustomerRegistrationMailSender;
import com.sagag.services.service.request.CustomerSearchForm;
import com.sagag.services.service.request.registration.RetrievedCustomerRequest;
import com.sagag.services.service.validator.CustomerValidator;
import com.sagag.services.service.validator.EshopCustomerValidator;
import com.sagag.services.service.validator.criteria.AxCustomerCriteria;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
public class CustomerBusinessServiceImpl implements CustomerBusinessService {

  private static final String DF_AFFILIATE_EMAIL =
      SettingsKeys.Affiliate.Settings.DEFAULT_EMAIL.toLowerName();

  @Autowired
  private CustomerExternalService custExtService;

  @Autowired
  private EshopCustomerValidator eshopCustomerValidator;

  @Autowired
  private CustomerValidator customerValidator;

  @Autowired
  private OrganisationService orgService;

  @Autowired
  private CustomerNumberSearch customerNumberSearch;

  @Autowired
  private CustomerTelephoneSearch customerTelephoneSearch;

  @Autowired
  private CustomerFreetextSearch customerFreetextSearch;

  @Autowired
  private CustomerCacheService customerCacheService;

  @Autowired
  protected OrganisationCollectionService orgCollectionService;

  @Autowired
  private PotentialCustomerRegistrationMailSender potentialCustomerRegistrationMailSender;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Autowired
  private FirstCustomerRegistrationHandler firstCustRegistrationHandler;

  @Autowired
  private APMCustomerRegistrationHandler apmCustRegistrationHandler;

  @Override
  @Transactional(readOnly = true)
  public EshopCustomerDto retrieveCustomterInfo(final RetrievedCustomerRequest request)
      throws CustomerValidationException {
    Assert.notNull(request, "The given criteria must not be null");
    eshopCustomerValidator.validate(request);

    // Get customer info from AX
    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(request.getAffiliate());
    final String custNr = request.getCustomerNumber();
    Optional<CustomerInfo> axCustomerInfo = Optional.empty();
    try {
      axCustomerInfo = custExtService.getActiveCustomerInfo(affiliate.getCompanyName(), custNr);
    } catch (AxCustomerException ex) {
      final String msg = ex.getMessageKey();
      throw new CustomerValidationException(CustomerErrorCase.CE_OTHER_001, msg);
    }

    // Validate state and affiliate
    if (!axCustomerInfo.isPresent()) {
      throw new CustomerValidationException(CustomerErrorCase.CE_CIN_001, "Customer is inactive.");
    }

    final AxCustomerInfo customerInfo = AxCustomerInfo.of(axCustomerInfo.get());
    customerValidator.validate(AxCustomerCriteria.builder().customerInfo(customerInfo)
        .postCode(request.getPostCode()).affiliate(affiliate.getAffiliate()).build());

    EshopCustomerDto eshopCustomerDto =
        EshopCustomerDto.copyFrom(customerInfo.getCustomer(), customerInfo.getDefaultAddress());
    eshopCustomerDto.setExisted(orgService.findOrganisationByOrgCode(custNr).isPresent());
    return eshopCustomerDto;
  }

  @Override
  public void registerCustomer(UserRegistrationDto userRegistration, String defaultPassword)
      throws ServiceException {
    firstCustRegistrationHandler.handle(userRegistration, defaultPassword);
  }

  @Override
  public void registerAPMCustomer(UserRegistrationDto userRegistration, String defaultPassword)
      throws ServiceException {
    apmCustRegistrationHandler.handle(userRegistration, defaultPassword);
  }

  @Override
  public CustomerDataResultDto search(CustomerSearchForm custSearchForm)
      throws TelephoneFormatException, UserValidationException, ErpCustomerNotFoundException {
    log.debug("Searching customer in with form = {}", custSearchForm);

    final SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(custSearchForm.getAffiliate());

    final String freetext = custSearchForm.getText();
    if (!StringUtils.isBlank(freetext)) {
      // For free-text mode
      return customerFreetextSearch.search(affiliate, freetext, custSearchForm.getPageable());
    }

    final String custNr = custSearchForm.getCustomerNr();
    final String telephone = custSearchForm.getTelephone();
    if (!StringUtils.isBlank(custNr)) {
      return customerNumberSearch.search(affiliate, custNr, Pageable.unpaged());
    } else if (!StringUtils.isBlank(telephone)) {
      return customerTelephoneSearch.search(affiliate, telephone, Pageable.unpaged());
    }
    throw new UnsupportedOperationException("Not support this search mode");
  }

  @Override
  public CustomerInfoDto viewCustomerByNumber(String companyName, String custNr)
      throws ErpCustomerNotFoundException {
    final Customer customer =
        customerCacheService.getCachedCustomer(custNr, companyName).orElseThrow(
            () -> new ErpCustomerNotFoundException(String.valueOf(custNr), SearchMode.CUSTNR));
    List<Address> customerAddresses =
        customerCacheService.getCachedCustomerAddresses(custNr, companyName);

    final CreditLimitInfo creditLimit =
        custExtService.getCreditLimitInfoByCustomer(companyName, custNr)
            .orElseThrow(() -> new NoSuchElementException("Credit limit not found"));
    return new CustomerInfoDto(customer, creditLimit, customerAddresses);
  }

  @Override
  public CustomerBODto getCustomerErpInfo(CustomerProfileSearchCriteria customerSearchForm)
      throws ErpCustomerNotFoundException {
    SupportedAffiliate affiliate = SupportedAffiliate.fromDesc(customerSearchForm.getAffiliate());
    final String custNr = customerSearchForm.getCustomerNr();
    return custExtService.findCustomerByNumber(affiliate.getCompanyName(), custNr).map(cust -> {
      CustomerBODto customerDto = CustomerBODto.fromCustomer(cust);
      customerDto.setAddressesLink(cust.getAddressRelativeUrl());
      if (Objects.nonNull(cust.getBranch())) {
        customerDto.setDefaultBranchId(cust.getBranch().getBranchId());
        customerDto.setDefaultBranchName(cust.getBranch().getBranchName());
      }
      return customerDto;
    }).orElseThrow(() -> new ErpCustomerNotFoundException(custNr, SearchMode.CUSTNR));
  }

  @Override
  @Transactional
  public void registerPotentialCustomer(PotentialCustomerRegistrationDto request)
      throws UserValidationException {

    final List<String> keys =
        Arrays.asList(DF_AFFILIATE_EMAIL, Affiliate.Settings.MARKETING_DEPT_EMAIL.toLowerName());
    final Map<String, String> settingsValues = orgCollectionService
        .findSettingValuesByCollectionShortnameAndKeys(request.getCollectionShortName(), keys);

    final String affiliateEmail = Optional.ofNullable(settingsValues.get(DF_AFFILIATE_EMAIL))
        .orElseThrow(notFoundEmail("Can not find collection setting email"));

    final String marketingDeptEmail = Optional
        .ofNullable(settingsValues.get(Affiliate.Settings.MARKETING_DEPT_EMAIL.toLowerName()))
        .orElseThrow(notFoundEmail("Can not find marketing department email"));

    PotentialCustomerRegistrationCriteria criteria =
        PotentialCustomerRegistrationCriteria.of(marketingDeptEmail, affiliateEmail, request,
            localeContextHelper.toLocale(request.getLangCode()));
    potentialCustomerRegistrationMailSender.send(criteria);
  }

  private static Supplier<UserValidationException> notFoundEmail(String msg) {
    return () -> new UserValidationException(UserErrorCase.UE_NFE_001, msg);
  }

}
