package com.sagag.services.tools.batch.offer_v2;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.offer_v2.offer.DiffOfferMigrationByCustomerStep;
import com.sagag.services.tools.batch.offer_v2.offer.OfferStatusConvertingStep;
import com.sagag.services.tools.config.OracleProfile;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@OracleProfile
@Slf4j
public class OfferConfiguration extends AbstractJobConfig {

  @Autowired
  private OfferStatusConvertingStep offerStatusConvertingStep;

  @Autowired
  private DiffOfferMigrationByCustomerStep diffOfferMigrationByCustomerStep;

  @Autowired
  private List<AbstractOfferMigrationSimpleStep<?, ?>> offerMigrationSteps;

  @Autowired
  private List<AbstractMissingOfferSimpleStep<?, ?>> missingOfferSteps;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  @Bean
  @Transactional
  public Job diffOfferMigrationByCustomerJob() throws Exception {
    return jobBuilder("DiffOfferMigrationByCustomer")
      .start(diffOfferMigrationByCustomerStep.toStep())
      .next(restoreDbConfigStep()).build();
  }

  @Bean
  @Transactional
  public Job migrateOfferByCustomerV2() throws Exception {
    final SimpleJobBuilder jobBuilder = jobBuilder("MigrateFullOfferByCustomer")
        .start(restoreDbConfigStep());

    for (AbstractOfferMigrationSimpleStep<?, ?> step : offerMigrationSteps) {
      log.debug("Step = {}", step.toString());
      jobBuilder.next(step.toStep())
      .next(restoreDbConfigStep());
    }
    jobBuilder.next(offerStatusConvertingStep.toStep()).next(restoreDbConfigStep());

    return jobBuilder.build();
  }

  @Bean
  @Transactional
  public Job migrateMissingOfferByCustomerJob() throws Exception {
    final SimpleJobBuilder jobBuilder = jobBuilder("MigrateFullMissingOfferByCustomer")
        .start(restoreDbConfigStep());

    for (AbstractMissingOfferSimpleStep<?, ?> step : missingOfferSteps) {
      log.debug("Step = {}", step.toString());
      jobBuilder.next(step.toStep())
      .next(restoreDbConfigStep());
    }
    jobBuilder.next(offerStatusConvertingStep.toStep()).next(restoreDbConfigStep());

    return jobBuilder.build();
  }
}
