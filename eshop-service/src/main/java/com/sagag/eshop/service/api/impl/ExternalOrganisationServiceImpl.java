package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.ExternalOrganisationRepository;
import com.sagag.eshop.repo.entity.ExternalOrganisation;
import com.sagag.eshop.service.api.ExternalOrganisationService;
import com.sagag.services.common.enums.ExternalApp;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.ExternalOrganisationDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class ExternalOrganisationServiceImpl implements ExternalOrganisationService {

  @Autowired
  private ExternalOrganisationRepository externalOrganisationRepo;

  @Override
  @Transactional
  public void addExternalOrganisation(ExternalOrganisationDto externalOrganisation) {

    Assert.notNull(externalOrganisation, "The given external organisation must not be null");

    final ExternalOrganisation exOrg =
        SagBeanUtils.map(externalOrganisation, ExternalOrganisation.class);

    externalOrganisationRepo.save(exOrg);
  }

  @Override
  public Optional<ExternalOrganisationDto> getExternalOrganisation(String externalCustomerName,
      ExternalApp app) {

    return externalOrganisationRepo.findFirstByExternalCustomerNameAndExternalApp(
        externalCustomerName, app).map(item -> SagBeanUtils.map(item, ExternalOrganisationDto.class));
  }

  @Override
  public boolean isCustomerNameExisted(final String externalCustomerName) {
    if (StringUtils.isBlank(externalCustomerName)) {
      throw new IllegalArgumentException("Customer name should not be blank");
    }
    return externalOrganisationRepo.isCustomerNameExisted(externalCustomerName);
  }

}
