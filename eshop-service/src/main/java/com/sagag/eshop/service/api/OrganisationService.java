package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.repo.entity.Organisation;
import com.sagag.eshop.repo.entity.OrganisationAddress;
import com.sagag.services.common.enums.PermissionEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.backoffice.dto.CustomerSearchResultItemDto;
import com.sagag.services.domain.eshop.criteria.CustomerProfileSearchCriteria;
import com.sagag.services.domain.eshop.dto.OrgPropertyOfferDto;
import com.sagag.services.domain.sag.erp.Address;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrganisationService {

  /**
   * Returns a {@link Organisation}.
   *
   * @return an organisation.
   */
  Optional<Organisation> findOrganisationByOrgCode(String orgCode);

  /**
   * Search customer by CustomerProfileSearchCriteria.
   *
   * @param searchCriteria that the CustomerProfileSearchCriteria
   * @return Page of CustomerSearchResultItemDto
   */

  Page<CustomerSearchResultItemDto> searchCustomerProfile(
      CustomerProfileSearchCriteria searchCriteria);

  /**
   * Returns first organisation of the specific user with basic information.
   *
   * @param userId the user id
   * @return the first organisation with basic information.
   */
  Optional<Organisation> getFirstByUserId(Long userId);

  /**
   * Returns the {@link Organisation} by <code>id</code>.
   *
   * @param id the identification of that organisation.
   * @return the found {@link Organisation}.
   */
  Optional<Organisation> getByOrgId(int id);

  /**
   * Finds organisation properties object dto by organisation id.
   *
   * @param id the identification of that organisation.
   * @return organisation properties object.
   */
  OrgPropertyOfferDto findOrganisationPropertiesById(Long id);

  /**
   * Returns List of {@link Organisation}.
   *
   * @return list of organisation.
   */
  List<Organisation> findOrganisationByTypeName(String orgTypeName);

  /**
   * Returns the company name by short name.
   *
   * @param shortname the short name
   * @return the <code>Optional</code> of company name
   */
  Optional<String> searchCompanyName(String shortname);

  /**
   * Creates the organisation customer.
   *
   */
  Organisation createCustomer(SupportedAffiliate affiliate, String customerNr, String companyName,
      CustomerSettings custSettings);

  /**
   * Assigns customer group and default permission.
   */
  void assignCustomerGroupAndDefaultPermission(Organisation customer,
      List<PermissionEnum> defaultPermission);

  /**
   * Creates customer addresses.
   */
  List<OrganisationAddress> createCustomerAddresses(Organisation customer, List<Address> addresses);

  /**
   * Finds the affiliate shortname for specified orgId.
   *
   * @param orgId the organisation id
   * @return the affiliate shortname for specified orgId
   */
  String findAffiliateByOrgId(int orgId);

  /**
   * Finds the orgCode for specified orgId.
   *
   * @param orgId the organisation id
   * @return the orgCode for specified orgId
   */
  String findOrgCodeById(int orgId);

  /**
   * Returns the organisation settings by org name.
   *
   * @param orgId the selected organisation id
   * @return the map of org settings.
   */
  Map<String, String> findOrgSettingsByShortName(String shortName);

  /**
   * Returns organisation settings by key and short name.
   *
   * @param shortname
   * @param key
   * @return the optional of setting value.
   */
  Optional<String> findOrgSettingByKey(String shortname, String key);

  /**
   * Finds the Id for specific affiliate
   *
   * @param affiliateShortName
   * @return the id of the organisation
   */
  Optional<Integer> findIdByShortName(String affiliateShortName);

  /**
   * Checks whether customer is wholesaler or not.
   *
   * @param orgId id of customer
   * @return
   */
  boolean isWholesalerCustomer(Integer orgId);

  /**
   * Checks whether customer is DVSE or not.
   *
   * @param orgId id of customer
   * @return
   */
  boolean isDvseCustomer(Integer orgId);
}
