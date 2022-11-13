package com.sagag.services.tools.service;

import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.domain.target.OrganisationAddress;
import com.sagag.services.tools.support.PermissionEnum;
import com.sagag.services.tools.support.SupportedAffiliate;

import java.util.List;
import java.util.Optional;

public interface OrganisationService {

  boolean isBelongsToDatMatikAtMatikChAffiliate(final Integer orgId);

  Optional<Organisation> findOrganisationById(int id);

  /**
   * Creates the organisation customer.
   *
   */
  Organisation createCustomer(SupportedAffiliate affiliate, String customerNr, String companyName, CustomerSettings custSettings);

  /**
   * Creates customer addresses.
   */
  List<OrganisationAddress> createCustomerAddresses(Organisation customer, List<Address> addresses);

  /**
   * Assigns customer group and default permission.
   */
  void assignCustomerGroupAndDefaultPermission(Organisation customer);

  void assignCustomerGroupAndDefaultPermission(Organisation customer, List<PermissionEnum> defaultPermission);
}
