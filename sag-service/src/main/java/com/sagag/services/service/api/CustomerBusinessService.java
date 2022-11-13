package com.sagag.services.service.api;

import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.bo.dto.CustomerBODto;
import com.sagag.services.domain.eshop.criteria.CustomerProfileSearchCriteria;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.domain.eshop.dto.organisation.EshopCustomerDto;
import com.sagag.services.service.dto.CustomerDataResultDto;
import com.sagag.services.service.dto.CustomerInfoDto;
import com.sagag.services.service.dto.registration.PotentialCustomerRegistrationDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.TelephoneFormatException;
import com.sagag.services.service.exception.customer.CustomerValidationException;
import com.sagag.services.service.request.CustomerSearchForm;
import com.sagag.services.service.request.registration.RetrievedCustomerRequest;

/**
 * Interface to define the APIs for customer.
 */
public interface CustomerBusinessService {

  /**
   * Returns the valid Customer from SAG Systems.
   *
   * @param request registration data model input.
   * @return the valid Customer from ERP.
   */
  EshopCustomerDto retrieveCustomterInfo(RetrievedCustomerRequest request)
      throws CustomerValidationException;

  /**
   * Creates the new custmer and first admin user.
   *
   * @param userRegistrationDto registration data model input.
   * @param defaultPassword the request default password if exists
   */
  void registerCustomer(UserRegistrationDto userRegistrationDto, String defaultPassword)
      throws ServiceException;

  /**
   * Returns the customer from user input.
   *
   * @param custSearchForm the customer search form submitted from UI.
   * @return the customer data with its 'on behalf of' admin user.
   * @throws TelephoneFormatException throws when program fails.
   */
  CustomerDataResultDto search(final CustomerSearchForm custSearchForm)
      throws TelephoneFormatException, UserValidationException, ErpCustomerNotFoundException;

  /**
   * Views the customer information by its number and the company that the customer belongs.
   * <p>
   * the method will also update the cache with newest customer info.
   *
   * @param custNr the customer number to refresh its info
   * @return the refresh customer info, was saved in cache as well
   * @throws ErpCustomerNotFoundException when customer not found
   */
  CustomerInfoDto viewCustomerByNumber(String companyName, String custNr)
      throws ErpCustomerNotFoundException;

  /**
   * Registers new potential customer.
   *
   * @param request registration data model input
   * @throws UserValidationException
   */
  void registerPotentialCustomer(PotentialCustomerRegistrationDto request)
      throws UserValidationException;

  /**
   * Gets customer information from ERP.
   *
   * @param criteria the CustomerProfileSearchCriteria
   * @return {@link CustomerBODto}
   */
  CustomerBODto getCustomerErpInfo(CustomerProfileSearchCriteria criteria)
      throws ErpCustomerNotFoundException;

  /**
   * Creates the new APM customer and first admin user.
   *
   * @param userRegistration
   * @param defaultPassword
   * @throws ServiceException
   */
  void registerAPMCustomer(UserRegistrationDto userRegistration, String defaultPassword)
      throws ServiceException;
}
