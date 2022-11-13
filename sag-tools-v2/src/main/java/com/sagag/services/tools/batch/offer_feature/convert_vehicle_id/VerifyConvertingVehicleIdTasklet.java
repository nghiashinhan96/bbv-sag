package com.sagag.services.tools.batch.offer_feature.convert_vehicle_id;

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
public class VerifyConvertingVehicleIdTasklet extends AbstractTasklet {

  @Autowired
  private TargetOfferPositionRepository targetOfferPositionRepository;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    final long total = targetOfferPositionRepository.countOfferPositionsNotConvertVehicleId();
    log.debug("Total is not convert vehicle id = {} records", total);
    return finish(contribution);
  }
}
