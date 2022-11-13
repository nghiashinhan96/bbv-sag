package com.sagag.services.tools.batch.offer_v2.offer;

import com.sagag.services.tools.batch.offer_v2.AbstractMissingOfferSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import com.sagag.services.tools.domain.target.TargetOffer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER)
public class MissingOfferMigrationByCustomerStep
  extends AbstractMissingOfferSimpleStep<SourceOffer, TargetOffer> {

  @Autowired
  private OfferMigrationByCustomerStep offerMigrationByCustomerStep;

  @Override
  public SimpleStepBuilder<SourceOffer, TargetOffer> stepBuilder() {
    return stepBuilder("MissingOfferMigrationByCustomerStep");
  }

  @Bean("MissingOfferMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ListItemReader<SourceOffer> itemReader() throws Exception {
    return new ListItemReader<>(missingOfferFromEblProcessor.process(sysVars.getDiffCustomerNr()));
  }

  @Bean("MissingOfferMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<SourceOffer, TargetOffer> itemProcessor() {
    return offerMigrationByCustomerStep.itemProcessor();
  }

  @Bean("MissingOfferMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetOffer> itemWriter() {
    return offerMigrationByCustomerStep.itemWriter();
  }

}
