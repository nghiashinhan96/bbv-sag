package com.sagag.services.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.ax.domain.AxCreditLimitInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.eshop.dto.CustomerCreditCheckResultDto;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.domain.eshop.dto.organisation.EshopCustomerDto;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.rest.authorization.annotation.CanUsedSubShoppingCartPreAuthorization;
import com.sagag.services.rest.resource.CustomerBranchDataResource;
import com.sagag.services.rest.resource.CustomerDataResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.CustomerBusinessService;
import com.sagag.services.service.api.CustomerCreditService;
import com.sagag.services.service.cart.support.ShopTypeDefault;
import com.sagag.services.service.dto.CustomerInfoDto;
import com.sagag.services.service.dto.registration.PotentialCustomerRegistrationDto;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.TelephoneFormatException;
import com.sagag.services.service.exception.customer.CustomerValidationException;
import com.sagag.services.service.request.CustomerBranchRequest;
import com.sagag.services.service.request.CustomerSearchForm;
import com.sagag.services.service.request.registration.RetrievedCustomerRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller class to provide RESTful APIs for customers.
 */
@RestController
@RequestMapping("/customers")
@Api(tags = "Customers APIs")
public class CustomersController {

  @Autowired
  private CustomerExternalService customerExtService;

  @Autowired
  private CustomerBusinessService custBusServiceV2;

  @Autowired
  private CustomerCreditService customerCreditService;

  @ApiOperation(value = ApiDesc.Customer.RETRIEVE_CUSTOMER_API_NOTE,
      notes = ApiDesc.Customer.RETRIEVE_CUSTOMER_API_DESC)
  @PostMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public EshopCustomerDto getCustomerInfo(@RequestBody RetrievedCustomerRequest request)
      throws CustomerValidationException {
    return custBusServiceV2.retrieveCustomterInfo(request);
  }

  @ApiOperation(value = "Create new customer", notes = "Create new customer")
  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void registerCustomer(@RequestBody UserRegistrationDto userRegistrationDto) throws ServiceException {
    custBusServiceV2.registerCustomer(userRegistrationDto, StringUtils.EMPTY);
  }

  /**
   * Searches the customer from number, telephone or text with the specific affiliate.
   *
   * @param auth the user who requests
   * @param searchForm the search form submitted from UI.
   * @return the customer data with its admin username.
   * @throws TelephoneFormatException
   * @throws UserValidationException
   * @throws ErpCustomerNotFoundException
   */
  @PostMapping(value = "/search",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerDataResource search(final OAuth2Authentication auth,
      @RequestBody final CustomerSearchForm searchForm)
      throws TelephoneFormatException, UserValidationException, ErpCustomerNotFoundException {
    final CustomerDataResource custDataResource =
        new CustomerDataResource(custBusServiceV2.search(searchForm));
    custDataResource
        .add(linkTo(methodOn(CustomersController.class).search(auth, searchForm)).withSelfRel());
    return custDataResource;
  }

  /**
   * Views the latest customer info from ERP/AX.
   *
   * @return the customer data.
   * @throws ErpCustomerNotFoundException
   */
  @GetMapping(value = "/view", produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerInfoDto viewCustomerInfo(@RequestParam("affiliate") final String companyName,
      @RequestParam("custNr") final String custNr) throws ErpCustomerNotFoundException {
    return custBusServiceV2.viewCustomerByNumber(companyName, custNr);
  }

  /**
   * Returns list branch belong to customer.
   *
   * @param cusBranchRequest customer branch info
   * @return the customer data with the list of <code>CustomerBranch</code> instance.
   */
  @PostMapping(value = "/branches", produces = MediaType.APPLICATION_JSON_VALUE)
  public CustomerBranchDataResource getBranches(
      @RequestBody final CustomerBranchRequest cusBranchRequest,
      final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final String companyName = cusBranchRequest.getCompanyName();
    final String defaultBranchId = cusBranchRequest.getDefaultBranchId();
    final List<CustomerBranch> branches =
        customerExtService.getCustomerBranches(companyName, defaultBranchId, user.isSaleOnBehalf());
    final CustomerBranchDataResource resource = new CustomerBranchDataResource(branches);
    resource.add(
        linkTo(methodOn(CustomersController.class).getBranches(cusBranchRequest, authed)).withSelfRel());
    return resource;
  }

  /**
   * Check if current open basket lower than customer allowed credit.
   *
   * @return the result of {@link CustomerCreditCheckResultDto}
   */
  @GetMapping(value = "/credit/check", produces = MediaType.APPLICATION_JSON_VALUE)
  @CanUsedSubShoppingCartPreAuthorization
  public CustomerCreditCheckResultDto checkCreditLimit(final HttpServletRequest request,
      @RequestParam(value = "customerCredit") double customerCredit,
      @RequestParam(value = "customerCreditCashPayment", defaultValue = "0") double customerCreditCashPayment,
      @ShopTypeDefault ShopType shopType, final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return customerCreditService.checkCustomerCredit(user, customerCredit, customerCreditCashPayment, shopType);
  }

  /**
   * Returns the credit limit info by customer for user login if exists.
   *
   * @return the result of {@link AxCreditLimitInfo}
   */
  @GetMapping(value = "/credit/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public CreditLimitInfo getCreditLimitInfo(@RequestParam("affiliate") final String companyName,
      @RequestParam("custNr") final String custNr) throws ResultNotFoundException {
    return RestExceptionUtils
        .doSafelyReturnOptionalRecord(customerExtService.getCreditLimitInfoByCustomer(companyName,
            custNr), String.format("Not found any credit limit info with customer = %s", custNr));
  }

  @ApiOperation(value = ApiDesc.Customer.REGISTER_POTENTIAL_CUSTOMER_API_DESC,
      notes = ApiDesc.Customer.REGISTER_POTENTIAL_CUSTOMER_API_NOTE)
  @PostMapping(value = "/register/potential-customer", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.OK)
  public void registerPotentialCustomer(@RequestBody PotentialCustomerRegistrationDto request)
      throws UserValidationException {
    custBusServiceV2.registerPotentialCustomer(request);
  }
}
