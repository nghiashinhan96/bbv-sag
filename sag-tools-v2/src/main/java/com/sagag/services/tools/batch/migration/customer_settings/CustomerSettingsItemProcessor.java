package com.sagag.services.tools.batch.migration.customer_settings;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOrganisationProperty;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.domain.target.Organisation;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.repository.target.OrganisationRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
@StepScope
@Slf4j
@OracleProfile
public class CustomerSettingsItemProcessor implements ItemProcessor<SourceOrganisationProperty, CustomerSettings> {

  @Autowired
  private MappingUserIdEblConnectRepository mappingRepo;

  @Autowired
  private OrganisationRepository orgRepo;

  @Override
  @Transactional(readOnly = true)
  public CustomerSettings process(SourceOrganisationProperty sourceOrganisationProperty) throws Exception {

    Optional<Integer> targetOrgIdOpt = mappingRepo.findConnectOrgIdByEblOrgId(sourceOrganisationProperty.getSourceOrganisationPropertyId().getOrganisationId());
    if (!targetOrgIdOpt.isPresent()) {
      return null;
    }

    Integer targetOrgId = targetOrgIdOpt.get();
    log.debug("Processing item with target_org_id = {}", targetOrgId);
    Optional<Organisation> targetOrganisation = orgRepo.findOneById(targetOrgId);
    if (!targetOrganisation.isPresent()) {
      return null;
    }

    CustomerSettings targetCustomerSettings = targetOrganisation.get().getCustomerSettings();
    if (Objects.isNull(targetCustomerSettings)) {
      return null;
    }

    return SourceOrganisationPropertyTypeEnum.fromType(sourceOrganisationProperty.getSourceOrganisationPropertyId().getType())
        .map(item -> item.updateSetting(targetCustomerSettings, sourceOrganisationProperty.getValue())).orElse(null);
  }
}
