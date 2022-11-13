package com.sagag.services.tools.service;


import com.sagag.services.tools.domain.target.ExternalOrganisationDto;
import com.sagag.services.tools.support.ExternalApp;

import java.util.Optional;

public interface ExternalOrganisationService {

  Optional<ExternalOrganisationDto> getExternalOrganisationByOrgId(Integer orgId);

  void addExternalOrganisation(ExternalOrganisationDto externalOrganisation);

  Optional<ExternalOrganisationDto> getExternalOrganisation(String externalCustomerName, ExternalApp app);

  /**
   * Checks if external customer name is existed.
   *
   * @param externalCustomerName the customer name to check
   * @return <code>true</code> if customer name is existed
   */
  boolean isCustomerNameExisted(String externalCustomerName);

}
