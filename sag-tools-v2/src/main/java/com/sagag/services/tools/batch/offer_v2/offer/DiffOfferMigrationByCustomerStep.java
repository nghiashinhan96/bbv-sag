package com.sagag.services.tools.batch.offer_v2.offer;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.batch.offer_v2.AbstractOfferTaskletStep;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OracleProfile
@Slf4j
public class DiffOfferMigrationByCustomerStep extends AbstractOfferTaskletStep {

  @Autowired
  private MissingOfferFromEblProcessor processor;

  @Override
  public TaskletStepBuilder taskletStepBuilder() {
    final String customerNr = sysVars.getDiffCustomerNr();
    final Tasklet tasklet = new AbstractTasklet() {

      @Override
      public RepeatStatus execute(StepContribution contribution,
        ChunkContext chunkContext) throws Exception {
        final List<SourceOffer> diffOffers = processor.process(customerNr);
        if (CollectionUtils.isEmpty(diffOffers)) {
          log.warn("Not found different offers at Connect site with EBL offers");
          return noOp(contribution);
        }

        log.info("============== START Not found Offer Id at Connect ==============");
        for (SourceOffer offer : diffOffers) {
          System.out.println(offer.getId());
        }
        log.info("============== END Not found Offer Id at Connect ==============");
        return finish(contribution);
      }
    };
    return taskletStepBuilder(tasklet);
  }

}
