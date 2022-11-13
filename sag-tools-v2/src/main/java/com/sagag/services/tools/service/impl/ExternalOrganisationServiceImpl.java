package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.ExternalOrganisation;
import com.sagag.services.tools.domain.target.ExternalOrganisationDto;
import com.sagag.services.tools.repository.target.ExternalOrganisationRepository;
import com.sagag.services.tools.service.ExternalOrganisationService;
import com.sagag.services.tools.support.ExternalApp;
import com.sagag.services.tools.utils.SagBeanUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@OracleProfile
public class ExternalOrganisationServiceImpl implements ExternalOrganisationService {

  @Autowired
  private ExternalOrganisationRepository externalOrganisationRepo;

  @Override
  public Optional<ExternalOrganisationDto> getExternalOrganisationByOrgId(Integer orgId) {

    Assert.notNull(orgId, "The given org id must not be null");

    return externalOrganisationRepo.findFirstByOrgId(orgId).map(item -> SagBeanUtils.map(item, ExternalOrganisationDto.class));
  }

  @Override
  public void addExternalOrganisation(ExternalOrganisationDto externalOrganisation) {

    Assert.notNull(externalOrganisation, "The given external organisation must not be null");

    final ExternalOrganisation exOrg = SagBeanUtils.map(externalOrganisation, ExternalOrganisation.class);

    externalOrganisationRepo.save(exOrg);
  }

  @Override
  public Optional<ExternalOrganisationDto> getExternalOrganisation(String externalCustomerName, ExternalApp app) {

    return externalOrganisationRepo.findFirstByExternalCustomerNameAndExternalApp(externalCustomerName, app)
        .map(item -> SagBeanUtils.map(item, ExternalOrganisationDto.class));
  }

  @Override
  public boolean isCustomerNameExisted(final String externalCustomerName) {
    if (StringUtils.isBlank(externalCustomerName)) {
      throw new IllegalArgumentException("Customer name should not be blank");
    }
    return externalOrganisationRepo.isCustomerNameExisted(externalCustomerName);
  }

}
