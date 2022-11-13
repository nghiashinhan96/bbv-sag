package com.sagag.services.tools.batch.offer_feature.offer_person;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourcePersonProperty;
import com.sagag.services.tools.domain.target.TargetOfferPersonProperty;
import com.sagag.services.tools.domain.target.TargetOfferPersonPropertyId;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourcePersonPropertyRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonPropertyRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonRepository;
import com.sagag.services.tools.utils.DefaultUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@OracleProfile
@Slf4j
public class MigratePersonPropertyFromOrclTasklet extends AbstractTasklet {

  @Autowired(required = false)
  private SourcePersonPropertyRepository sourcePersonPropertyRepo;

  @Autowired
  private TargetOfferPersonPropertyRepository targetOfferPersonPropertyRepo;

  @Autowired
  private TargetOfferPersonRepository targetOfferPersonRepo;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {
    final List<Long> personIdList =
      Collections.unmodifiableList(targetOfferPersonRepo.findPersonIdList());
    log.debug("The size of person id list = {} records", personIdList.size());

    Set<Long> personIdSet = personIdList.stream().collect(Collectors.toSet());
    List<TargetOfferPersonProperty> targetList;
    for (Long personId : personIdSet) {
      List<SourcePersonProperty> personProperties = sourcePersonPropertyRepo.findByPersonId(personId);
      if (CollectionUtils.isEmpty(personProperties)) {
        continue;
      }
      targetList = personProperties.stream()
        .peek(item -> log.debug("Source offer person property = {}", item))
        .map(property -> mapSourceToTarget(property))
        .filter(Objects::nonNull)
        .peek(target -> log.debug("Target offer person property = {}", target))
        .collect(Collectors.toList());
      targetOfferPersonPropertyRepo.saveAll(targetList);
    }

    return finish(contribution);
  }

  private TargetOfferPersonProperty mapSourceToTarget(SourcePersonProperty source) {
    TargetOfferPersonProperty target = new TargetOfferPersonProperty();
    TargetOfferPersonPropertyId targetOfferPersonPropertyId = new TargetOfferPersonPropertyId();
    targetOfferPersonPropertyId.setPersonId(source.getSourcePersonPropertyId().getPersonId());
    targetOfferPersonPropertyId.setType(source.getSourcePersonPropertyId().getType());
    target.setTargetOfferPersonPropertyId(targetOfferPersonPropertyId);
    final String value = StringUtils.defaultString(source.getValue());
    target.setValue(DefaultUtils.toUtf8Value(value));
    return target;
  }

}
