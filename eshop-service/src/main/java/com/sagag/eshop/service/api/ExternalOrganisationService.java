package com.sagag.eshop.service.api;

import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.domain.eshop.dto.ExternalOrganisationDto;

import java.util.Optional;

public interface ExternalOrganisationService {

  void addExternalOrganisation(ExternalOrganisationDto externalOrganisation);

  Optional<ExternalOrganisationDto> getExternalOrganisation(String externalCustomerName,
      ExternalApp app);

  /**
   * Checks if external customer name is existed.
   *
   * @param externalCustomerName the customer name to check
   * @return <code>true</code> if customer name is existed
   */
  boolean isCustomerNameExisted(String externalCustomerName);

}
