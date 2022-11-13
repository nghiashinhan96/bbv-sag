package com.sagag.services.tools.batch.offer_v2.offer_person_property;

import com.sagag.services.tools.batch.offer_feature.offer_person.OfferPersonType;
import com.sagag.services.tools.batch.offer_v2.AbstractMissingOfferSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.TargetOfferPersonProperty;
import com.sagag.services.tools.query.SqlOfferPersonQueryBuilder;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER_PERSON_PROPERTY)
@Slf4j
public class MissingOfferPersonPropertyMigrationByCustomerStep
    extends AbstractMissingOfferSimpleStep<Long, List<TargetOfferPersonProperty>> {

  @Autowired
  private OfferPersonPropertyMigrationByCustomerStep step;

  @Override
  public SimpleStepBuilder<Long, List<TargetOfferPersonProperty>> stepBuilder() {
    return stepBuilder("MissingOfferPersonPropertyMigrationByCustomerStep");
  }

  @Bean("MissingOfferPersonPropertyMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<Long> itemReader() throws Exception {
    final String customerNr = sysVars.getDiffCustomerNr();
    final Optional<Long> eblOrgIdOpt = mappingUserIdEblConnectService.searchEblOrgId(customerNr);
    final List<Long> vendorIds = eblOrgIdOpt.map(Arrays::asList)
        .orElseGet(() -> Collections.emptyList());
    OfferJpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

      @Override
      public Query createQuery() {
        log.debug("Creating query for the vendorIds = {}", vendorIds);
        return SqlOfferPersonQueryBuilder.builder()
            .em(getEntityManager())
            .selector("id")
            .vendorIds(vendorIds)
            .personType(OfferPersonType.ADDRESSEE)
            .clazz(Long.class)
            .build().createQuery();
      }
    };
    return jpaPagingItemReader(sourceEntityManagerFactory, queryProvider);
  }

  @Bean("MissingOfferPersonPropertyMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<Long, List<TargetOfferPersonProperty>> itemProcessor() {
    return step.itemProcessor();
  }

  @Bean("MissingOfferPersonPropertyMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<List<TargetOfferPersonProperty>> itemWriter() {
    return step.itemWriter();
  }

}
