package com.sagag.services.tools.batch.offer_v2.offer_position;

import com.sagag.services.tools.batch.offer_v2.AbstractMissingOfferSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import com.sagag.services.tools.domain.source.SourceOfferPosition;
import com.sagag.services.tools.domain.target.TargetOfferPosition;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER_POSITION)
public class MissingOfferPositionMigrationByCustomerStep
  extends AbstractMissingOfferSimpleStep<SourceOfferPosition, TargetOfferPosition> {

  @Autowired
  private OfferPositionMigrationByCustomerStep step;

  @Override
  public SimpleStepBuilder<SourceOfferPosition, TargetOfferPosition> stepBuilder() {
    return stepBuilder("MissingOfferPositionMigrationByCustomerStep");
  }

  @Bean("MissingOfferPositionMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<SourceOfferPosition> itemReader() throws Exception {
    final List<SourceOffer> sourceOffers =
        missingOfferFromEblProcessor.process(sysVars.getDiffCustomerNr());
    List<Long> offerIds = sourceOffers.stream().map(SourceOffer::getId)
        .collect(Collectors.toList());
    if (CollectionUtils.isEmpty(offerIds)) {
      offerIds.add(NumberUtils.LONG_ZERO);
    }
    OfferJpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

      @Override
      public Query createQuery() {
        final String queryStr = "select op from SourceOfferPosition op "
            + "inner join op.offer o where o.id IN :offerIds";
        return this.getEntityManager().createQuery(queryStr, SourceOfferPosition.class)
            .setParameter("offerIds", offerIds);
      }
    };
    return jpaPagingItemReader(sourceEntityManagerFactory, queryProvider);
  }

  @Bean("MissingOfferPositionMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<SourceOfferPosition, TargetOfferPosition> itemProcessor() {
    return step.itemProcessor();
  }

  @Bean("MissingOfferPositionMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetOfferPosition> itemWriter() {
    return step.itemWriter();
  }
}
