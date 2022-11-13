package com.sagag.services.tools.batch.migration.organation_property;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvOrganisationProperty;
import com.sagag.services.tools.domain.target.TargetOrganisationProperty;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.target.OrganisationRepository;
import com.sagag.services.tools.repository.target.TargetOrganisationPropertyRepository;
import com.sagag.services.tools.service.OrganisationService;
import com.sagag.services.tools.support.CommonInitialResource;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.DefaultUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@OracleProfile
public class MigrateOrganisationPropertyFromCsvTasklet extends AbstractTasklet {

  private static final String NOT_MIGRATED_DATA_CSV =
    SystemUtils.getUserDir() + "/csv/offer_organisation_propery_not_migrated_data.csv";

  @Value("${dirPath:/csv/property}")
  private String dirPath;

  @Autowired
  private TargetOrganisationPropertyRepository targetOrganisationPropertyRepo;

  @Autowired
  private OrganisationRepository organisationRepository;

  @Autowired
  private OrganisationService organisationService;

  @Autowired
  protected CommonInitialResource commonInitialResource;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {
    final File orgPropertiesDir = new File(SystemUtils.getUserDir() + dirPath);
    if (!orgPropertiesDir.isDirectory()) {
      throw new IllegalArgumentException("The current path is not directory");
    }

	final List<Long> orgIdList = Collections.unmodifiableList(organisationRepository.findOrganisationIdList())
		.stream().map(orgId -> orgId.longValue()).collect(Collectors.toList());

    log.debug("The size of organisation id list = {} records", orgIdList.size());

    List<CsvOrganisationProperty> canNotMigrated = new ArrayList<>();
    List<CsvOrganisationProperty> orgPropList;
    List<TargetOrganisationProperty> targetList;

    // List all csv files
    File[] csvFiles = orgPropertiesDir.listFiles();
    for (File file : csvFiles) {
      orgPropList = CsvUtils.read(file, CsvOrganisationProperty.class);
      if (CollectionUtils.isEmpty(orgPropList)) {
        continue;
      }

      targetList = orgPropList.stream()
        .peek(item -> log.debug("Source organisation property = {}", item))
        .map(property -> {
            final Integer connectOrgId = commonInitialResource.getOrgId(property.getOrganisationId());
            if(Objects.isNull(connectOrgId) || !organisationService.isBelongsToDatMatikAtMatikChAffiliate(connectOrgId)) {
              canNotMigrated.add(property);
              return null;
            }
            property.setOrganisationId(connectOrgId.longValue());
          return mapSourceToTarget(property);
        })
        .filter(Objects::nonNull)
        .peek(target -> log.debug("Target organisation property = {}", target))
        .collect(Collectors.toList());

      targetOrganisationPropertyRepo.saveAll(targetList);
    }

    CsvUtils.write(NOT_MIGRATED_DATA_CSV, canNotMigrated);

    return finish(contribution);
  }

  private TargetOrganisationProperty mapSourceToTarget(CsvOrganisationProperty source) {
    TargetOrganisationProperty target = new TargetOrganisationProperty();
    target.setOrganisationId(source.getOrganisationId());
    target.setType(source.getType());
    final String value = StringUtils.defaultString(source.getValue());
    target.setValue(DefaultUtils.toUtf8Value(value));
    return target;
  }

}
