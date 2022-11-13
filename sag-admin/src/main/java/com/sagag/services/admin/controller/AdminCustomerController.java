package com.sagag.services.admin.controller;

import com.sagag.eshop.repo.entity.SettingsKeys.Affiliate;
import com.sagag.eshop.service.api.LicenseService;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.api.OrganisationService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.LicenseNotFoundException;
import com.sagag.eshop.service.exception.OrganisationCollectionException;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.services.admin.business.service.BoCustomerService;
import com.sagag.services.admin.swagger.docs.ApiDesc;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.bo.dto.CustomerBODto;
import com.sagag.services.domain.bo.dto.CustomerSettingsBODto;
import com.sagag.services.domain.bo.dto.PaymentSettingBODto;
import com.sagag.services.domain.eshop.backoffice.dto.BackOfficeLicenseDto;
import com.sagag.services.domain.eshop.backoffice.dto.CustomerSearchResultItemDto;
import com.sagag.services.domain.eshop.criteria.CustomerProfileSearchCriteria;
import com.sagag.services.domain.eshop.dto.CustomerLicenseDto;
import com.sagag.services.domain.eshop.dto.LicenseSettingsDto;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;
import com.sagag.services.domain.eshop.dto.collection.OrganisationCollectionDto;
import com.sagag.services.domain.eshop.dto.organisation.EshopCustomerDto;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.mdm.exception.MdmCustomerNotFoundException;
import com.sagag.services.service.api.CustomerBusinessService;
import com.sagag.services.service.api.UserBusinessService;
import com.sagag.services.service.exception.ErpCustomerNotFoundException;
import com.sagag.services.service.exception.customer.CustomerValidationException;
import com.sagag.services.service.request.registration.RetrievedCustomerRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "admin")
public class AdminCustomerController {

  @Autowired
  private BoCustomerService boCustomerService;

  @Autowired
  private UserBusinessService userBusinessService;

  @Autowired
  private LicenseService licenseService;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Autowired
  private CustomerBusinessService custBusServiceV2;

  /**
   * Searches the customers by search criteria.
   *
   * @param searchCriteria the search criteria
   * @return Page<CustomerSearchResultItemDto>
   */
  @ApiOperation(value = "Search customer for back office",
      notes = "Search by orgcode and affiliate and organisation name")
  @PostMapping(value = "/search")
  public Page<CustomerSearchResultItemDto> searchCustomerProfile(
      @RequestBody final CustomerProfileSearchCriteria searchCriteria) {
    return organisationService.searchCustomerProfile(searchCriteria);
  }

  /**
   * Returns the customer information from ERP.
   *
   * @param criteria the search criteria
   * @return a {@link CustomerBODto}
   * @throws ErpCustomerNotFoundException
   */
  @ApiOperation(value = ApiDesc.Customer.GET_CUSTOMER_ERP_INFO_DESC,
      notes = ApiDesc.Customer.GET_CUSTOMER_ERP_INFO_NOTE)
  @PostMapping(value = "/info")
  public CustomerBODto getCustomerInfo(@RequestBody final CustomerProfileSearchCriteria criteria)
      throws ErpCustomerNotFoundException {
    return custBusServiceV2.getCustomerErpInfo(criteria);
  }

  /**
   * Gets Customer Setting from DB.
   *
   * @param customerNr the customer number
   * @return a {@link PaymentSettingBODto}
   */
  @ApiOperation(value = ApiDesc.Customer.GET_CUSTOMER_SETTINGS_DESC,
      notes = ApiDesc.Customer.GET_CUSTOMER_SETTINGS_NOTE)
  @GetMapping(value = "/settings")
  public PaymentSettingBODto getCustomerSetting(@RequestParam String customerNr) {
    return boCustomerService.getCustomerSettingByOrgCodeByAdmin(customerNr);
  }

  /**
   * Gets org collection permission.
   *
   * @param collectionShortName the collection short name
   * @return the org collection permission
   * @throws OrganisationCollectionException the organisation collection exception
   */
  @ApiOperation(value = ApiDesc.Customer.GET_ORG_COLLECTION_PERMISSION_DESC,
      notes = ApiDesc.Customer.GET_ORG_COLLECTION_PERMISSION_NOTE)
  @GetMapping(value = "/collection/settings")
  public OrganisationCollectionDto getOrgCollectionPermission(@RequestParam String collectionShortName)
      throws OrganisationCollectionException {
    return orgCollectionService.getCollectionPermissionForCustomer(collectionShortName);
  }

  /**
   * Updates customer setting to DB.
   *
   * @param customerSettingsBODto the {@link CustomerSettingsBODto}
   * @throws MdmCustomerNotFoundException 
   * @throws UserValidationException 
   */
  @ApiOperation(value = ApiDesc.Customer.UPDATE_CUSTOMER_SETTINGS_DESC,
      notes = ApiDesc.Customer.UPDATE_CUSTOMER_SETTINGS_NOTE)
  @PostMapping(value = "/settings/update")
  @ResponseStatus(value = HttpStatus.OK)
  public void updateCustomerSetting(@RequestBody CustomerSettingsBODto customerSettingsBODto)
      throws UserValidationException, MdmCustomerNotFoundException {
    boCustomerService.updateCustomerSettingByOrgCode(customerSettingsBODto);
  }

  /**
   * Gets all licenses of customer.
   *
   * @param customerNr the customer number
   * @param page the page number
   * @param size the size of the response list
   * @return a list of all licenses
   */
  @ApiOperation(value = ApiDesc.Customer.GET_CUSTOMER_LICENSE_DESC,
      notes = ApiDesc.Customer.GET_CUSTOMER_LICENSE_NOTE)
  @GetMapping(value = "/licenses")
  public Page<BackOfficeLicenseDto> getCustomerLicenses(@RequestParam String customerNr,
      @RequestParam(name = "pageNumber", defaultValue = "0") final int page,
      @RequestParam(name = "pageSize", defaultValue = "10") final int size) {
    return licenseService.getAllLicensesOfCustomer(customerNr, PageRequest.of(page, size));
  }

  /**
   * Deletes licence.
   *
   * @param id the license id
   */
  @ApiOperation(value = ApiDesc.Customer.DELETE_LICENSE_DESC,
      notes = ApiDesc.Customer.DELETE_LICENSE_NOTE)
  @DeleteMapping(value = "/license/{id}/delete")
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteLicense(@PathVariable("id") final Long id) {
    licenseService.deleteLicenseForCustomer(id);
  }

  /**
   * Assigns license to specify customer.
   *
   * @param auth the authenticated user principle.
   * @param customerLicenseDto the customerLicenseDto.
   * @throws LicenseNotFoundException
   */
  @ApiOperation(value = ApiDesc.Customer.ASSIGN_LICENSE_DESC,
      notes = ApiDesc.Customer.ASSIGN_LICENSE_NOTE)
  @PostMapping(value = "/assign/license")
  @ResponseStatus(value = HttpStatus.OK)
  public void assignLicense(final OAuth2Authentication auth,
      @RequestBody final CustomerLicenseDto customerLicenseDto) throws LicenseNotFoundException {
    final UserInfo user =
        userBusinessService.findCacheUser(Long.parseLong(auth.getPrincipal().toString()),
            StringUtils.EMPTY, StringUtils.EMPTY);
    licenseService.assignLicenseToCustomer(customerLicenseDto, user);
  }

  /**
   * Updates a license for the customer.
   *
   * @param customerLicenseDto
   * @throws LicenseNotFoundException
   */
  @ApiOperation(value = ApiDesc.Customer.UPDATE_LICENSE_DESC,
      notes = ApiDesc.Customer.UPDATE_LICENSE_NOTE)
  @PutMapping(value = "/license/{id}/update")
  @ResponseStatus(value = HttpStatus.OK)
  public void updateLicense(final OAuth2Authentication auth, @PathVariable("id") final Long id,
      @RequestBody final CustomerLicenseDto customerLicenseDto) throws LicenseNotFoundException {
    final UserInfo user =
        userBusinessService.findCacheUser(Long.parseLong(auth.getPrincipal().toString()),
            StringUtils.EMPTY, StringUtils.EMPTY);
    licenseService.updateLicenseForCustomer(id, customerLicenseDto, user);
  }

  /**
   * Gets all available license packages.
   *
   * @return list of LicenseSettingDto
   */
  @ApiOperation(value = ApiDesc.Customer.GET_LICENSE_DESC,
      notes = ApiDesc.Customer.GET_LICENSE_NOTE)
  @GetMapping(value = "/license/packages")
  public List<LicenseSettingsDto> getAllLicenses() {
    return licenseService.getAllLicensePackage();
  }

  @ApiOperation(value = ApiDesc.Customer.RETRIEVE_CUSTOMER_API_NOTE,
      notes = ApiDesc.Customer.RETRIEVE_CUSTOMER_API_DESC)
  @PostMapping(value = "/registration/info")
  public EshopCustomerDto checkCustomer(@RequestBody RetrievedCustomerRequest request)
      throws CustomerValidationException {
    return custBusServiceV2.retrieveCustomterInfo(request);
  }

  @ApiOperation(value = ApiDesc.Customer.CREATE_NEW_CUSTOMER_DESC,
      notes = ApiDesc.Customer.CREATE_NEW_CUSTOMER_NOTE)
  @PostMapping(value = "/registration/register")
  @ResponseStatus(value = HttpStatus.OK)
  public void registerCustomer(@RequestBody UserRegistrationDto userRegistrationDto)
      throws ServiceException {

    final String defaultUrlKey = Affiliate.Settings.DEFAULT_URL.toLowerName();
    String defaultUrl = orgCollectionService.findSettingValueByCollectionShortnameAndKey(
        userRegistrationDto.getAffiliate(), defaultUrlKey).orElseThrow(
            () -> new IllegalArgumentException("Can not find default url for this affiliate"));
    userRegistrationDto.setAccessUrl(defaultUrl + Customer.SUB_URL_FORGOT_PWORD_VERIFYCODE);
    custBusServiceV2.registerCustomer(userRegistrationDto, StringUtils.EMPTY);
  }

}
