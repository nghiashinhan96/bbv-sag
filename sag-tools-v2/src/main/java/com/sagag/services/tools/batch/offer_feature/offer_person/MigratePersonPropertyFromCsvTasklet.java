package com.sagag.services.tools.batch.offer_feature.offer_person;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvPersonProperty;
import com.sagag.services.tools.domain.target.TargetOfferPersonProperty;
import com.sagag.services.tools.domain.target.TargetOfferPersonPropertyId;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.target.TargetOfferPersonPropertyRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonRepository;
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
public class MigratePersonPropertyFromCsvTasklet extends AbstractTasklet {

  private static final String NOT_MIGRATED_DATA_CSV =
    SystemUtils.getUserDir() + "/csv/offer_person_propery_not_migrated_data.csv";

  @Value("${dirPath:/csv/property}")
  private String dirPath;

  @Autowired
  private TargetOfferPersonPropertyRepository targetOfferPersonPropertyRepo;

  @Autowired
  private TargetOfferPersonRepository targetOfferPersonRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {
    final File personPropertiesDir = new File(SystemUtils.getUserDir() + dirPath);
    if (!personPropertiesDir.isDirectory()) {
      throw new IllegalArgumentException("The current path is not directory");
    }

    final List<Long> personIdList =
      Collections.unmodifiableList(targetOfferPersonRepo.findPersonIdList());
    log.debug("The size of person id list = {} records", personIdList.size());

    List<CsvPersonProperty> canNotMigrated = new ArrayList<>();
    List<CsvPersonProperty> personPropList;
    List<TargetOfferPersonProperty> targetList;

    // List all csv files
    File[] csvFiles = personPropertiesDir.listFiles();
    for (File file : csvFiles) {
      personPropList = CsvUtils.read(file, CsvPersonProperty.class);
      if (CollectionUtils.isEmpty(personPropList)) {
        continue;
      }

      targetList = personPropList.stream()
        .peek(item -> log.debug("Source offer person property = {}", item))
        .map(property -> {
          if (!personIdList.contains(property.getPersonId())) {
            canNotMigrated.add(property);
            return null;
          }
          return mapSourceToTarget(property);
        })
        .filter(Objects::nonNull)
        .peek(target -> log.debug("Target offer person property = {}", target))
        .collect(Collectors.toList());

      targetOfferPersonPropertyRepo.saveAll(targetList);
    }

    CsvUtils.write(NOT_MIGRATED_DATA_CSV, canNotMigrated);

    return finish(contribution);
  }

  private TargetOfferPersonProperty mapSourceToTarget(CsvPersonProperty source) {
    TargetOfferPersonProperty target = new TargetOfferPersonProperty();
    TargetOfferPersonPropertyId targetOfferPersonPropertyId = new TargetOfferPersonPropertyId();
    targetOfferPersonPropertyId.setPersonId(source.getPersonId());
    targetOfferPersonPropertyId.setType(source.getType());
    target.setTargetOfferPersonPropertyId(targetOfferPersonPropertyId);
    final String value = StringUtils.defaultString(source.getValue());
    target.setValue(DefaultUtils.toUtf8Value(value));
    return target;
  }

}
