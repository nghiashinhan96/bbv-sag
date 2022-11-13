package com.sagag.services.tools.batch.offer_feature.convert_id_sagsys;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.repository.target.TargetOfferPositionRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@OracleProfile
public class VerifyConvertingIdSagsysTasklet extends AbstractTasklet {

  @Autowired
  private TargetOfferPositionRepository targetOfferPositionRepository;

  @Override
  public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
    final long total = targetOfferPositionRepository.countOfferPositionsNotConvertToIdSagsys();
    log.debug("Total is not convert to id sagsys = {} records", total);
    return finish(contribution);
  }
}
