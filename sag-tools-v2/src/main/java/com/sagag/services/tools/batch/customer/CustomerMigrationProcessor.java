package com.sagag.services.tools.batch.customer;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.ax.AxCustomerInfo;
import com.sagag.services.tools.domain.csv.CustInfo;
import com.sagag.services.tools.domain.external.Customer;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.domain.target.ExternalOrganisationDto;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.exception.AxCustomerException;
import com.sagag.services.tools.exception.MdmResponseException;
import com.sagag.services.tools.exception.UserValidationException;
import com.sagag.services.tools.exception.UserValidationException.UserErrorCase;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.service.AxService;
import com.sagag.services.tools.service.CustomerSettingsService;
import com.sagag.services.tools.service.DvseUserService;
import com.sagag.services.tools.service.ExternalOrganisationService;
import com.sagag.services.tools.service.OrganisationService;
import com.sagag.services.tools.support.ExternalApp;
import com.sagag.services.tools.support.SupportedAffiliate;
import com.sagag.services.tools.utils.DvseUserUtils;

import lombok.extern.slf4j.Slf4j;

@StepScope
@Slf4j
@Component
@OracleProfile
public class CustomerMigrationProcessor implements ItemProcessor<CustInfo, String> {

  @Autowired
  private OrganisationRepository orgRepo;

  @Autowired
  private AxService axService;

  @Autowired
  private CustomerSettingsService custSettingsService;

  @Autowired
  private OrganisationService orgService;

  @Autowired
  private ExternalOrganisationService extOrgService;

  @Autowired
  private DvseUserService dvseUserService;

  @Override
  public String process(final CustInfo custInfo) throws UserValidationException {
    final String customerNr = custInfo.getCustomerNr();
    if (StringUtils.isBlank(customerNr)) {
      return StringUtils.EMPTY;
    }

    final Optional<Organisation> existingCustomerOrg = orgRepo.findOneByOrgCode(customerNr);

    if (existingCustomerOrg.isPresent()) {
      log.info("The customer {} is existed in the past" + customerNr);
      final Optional<ExternalOrganisationDto> exOrganisationOpt = extOrgService.getExternalOrganisationByOrgId(Integer.valueOf(customerNr));
      if (exOrganisationOpt.isPresent()) {
        return StringUtils.EMPTY;
      }
      createDvseCustomer(custInfo.getAff(), existingCustomerOrg.get());
      return StringUtils.EMPTY;
    }

    try {
      // Create customer organisation
      final SupportedAffiliate affiliate = custInfo.getAff();
      final Optional<AxCustomerInfo> axCustomerInfo = axService.getActiveCustomerInfo(affiliate.getCompanyName(), customerNr);

      // Validate state and affiliate
      if (!axCustomerInfo.isPresent()) {
        return StringUtils.EMPTY;
      }

      final AxCustomerInfo customerInfo = axCustomerInfo.get();
      final Customer customer = customerInfo.getCustomer();
      final List<Address> addresses = customerInfo.getAddresses();

      final String custCompanyName = customer.getCompanyName();

      final CustomerSettings custSettings = custSettingsService.createCustomerSettings(customer, addresses);

      createCustomerOrgAndDvseCustomer(affiliate, customerNr, custCompanyName, custSettings, addresses);
    } catch (AxCustomerException ex) {
      log.info("------------------------------------AX customer not found:" + customerNr);
    } catch (Exception ex) {
      log.info("------------------------------------Customer cannot migrated:" + customerNr);
    }
    return customerNr;
  }

  private void createCustomerOrgAndDvseCustomer(SupportedAffiliate affiliate, String customerNr, String companyName, CustomerSettings custSettings,
      List<Address> addresses) throws UserValidationException {
    final Organisation customer = orgService.createCustomer(affiliate, customerNr, companyName, custSettings);
    orgService.createCustomerAddresses(customer, addresses);
    orgService.assignCustomerGroupAndDefaultPermission(customer);
    createDvseCustomer(affiliate, customer);
  }

  private void createDvseCustomer(SupportedAffiliate affiliate, final Organisation org) throws UserValidationException {
    // Create new DVSE customer for DDAT at MDM service
    if (!affiliate.isDvseAffiliate()) {
      return;
    }

    // Create new DVSE customer
    try {
      final String customerName = generateUniqueCustomerName();
      final String customerId = dvseUserService.createCustomer(customerName, affiliate);
      final ExternalOrganisationDto externalOrg = new ExternalOrganisationDto();
      externalOrg.setOrgId(org.getId());
      externalOrg.setExternalCustomerId(customerId);
      externalOrg.setExternalCustomerName(customerName);
      externalOrg.setExternalApp(ExternalApp.DVSE);
      extOrgService.addExternalOrganisation(externalOrg);
    } catch (final NoSuchElementException | MdmResponseException ex) {
      log.error("Create new MDM customer for affiliate has error: ", ex);
      throw new UserValidationException(UserErrorCase.UE_MCC_001, "Create new MDM customer failed");
    }
  }

  private String generateUniqueCustomerName() throws UserValidationException {
    String customerName = null;
    boolean isValidCustomerName = false;
    for (int i = 0; i < DvseUserUtils.MAX_UNIQUE_NAME_RETRY; i++) {
      customerName = DvseUserUtils.generateRandomCustomerName();
      isValidCustomerName = !extOrgService.isCustomerNameExisted(customerName);
      if (isValidCustomerName) {
        break;
      }
    }
    if (!isValidCustomerName) {
      throw new UserValidationException(UserErrorCase.UE_MCC_001, "Failed to generate unique customer name");
    }
    return customerName;
  }

}
