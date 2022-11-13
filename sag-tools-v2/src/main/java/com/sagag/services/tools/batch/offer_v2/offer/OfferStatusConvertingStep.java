package com.sagag.services.tools.batch.offer_v2.offer;

import com.sagag.services.tools.batch.offer_feature.offer_status.ConnectOfferStatus;
import com.sagag.services.tools.batch.offer_feature.offer_status.EblOfferStatus;
import com.sagag.services.tools.batch.offer_v2.AbstractOfferSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.repository.target.TargetOfferRepository;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Slf4j
public class OfferStatusConvertingStep extends AbstractOfferSimpleStep<TargetOffer, TargetOffer> {

  @Autowired
  private TargetOfferRepository targetOfferRepo;

  @Override
  public SimpleStepBuilder<TargetOffer, TargetOffer> stepBuilder() {
    return stepBuilder("offerStatusConvertingStep");
  }

  @Bean("offerStatusConvertingStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<TargetOffer> itemReader() throws Exception {
    final JpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

      @Override
      public Query createQuery() {
        final String query = "select o from TargetOffer o where o.status in :statuses";
        final List<String> statuses = Stream.of(EblOfferStatus.values())
          .map(EblOfferStatus::name)
          .collect(Collectors.toList());
        return getEntityManager().createQuery(query, TargetOffer.class)
          .setParameter("statuses", statuses);
      }
    };
    return jpaPagingItemReader(targetEntityManagerFactory, queryProvider);
  }

  @Bean("offerStatusConvertingStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<TargetOffer, TargetOffer> itemProcessor() {
    return source -> {
      Assert.notNull(source, "The given source offer must not be null");
      final String eblOfferStatus = source.getStatus();
      log.debug("Source offer status of id = {} is {}", source.getId(), eblOfferStatus);
      final ConnectOfferStatus connectOfferStatus = convertStatus(eblOfferStatus);
      log.debug("Target offer status is = {}", connectOfferStatus);
      source.setStatus(connectOfferStatus.name());
      return source;
    };
  }

  private static ConnectOfferStatus convertStatus(String status) {
    Assert.hasText(status, "The given status must not be empty");
    return valueOf(EblOfferStatus.valueOf(status));
  }

  private static ConnectOfferStatus valueOf(EblOfferStatus eblOfferStatus) {
    Assert.notNull(eblOfferStatus, "The EBL offer status must not be null");
    switch (eblOfferStatus) {
      case OPEN:
      case NEW:
      case INPROCESS:
        return ConnectOfferStatus.OPEN;

      case ORDERED:
      case PAID:
      case INDELIVERY:
      case DELIVERED:
        return ConnectOfferStatus.ORDERED;

      case MANUALLY_ORDERED:
          return ConnectOfferStatus.MANUALLY_ORDERRED;

      default:
          return ConnectOfferStatus.OPEN;
    }
  }

  @Bean("offerStatusConvertingStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetOffer> itemWriter() {
    return offers -> {
      if (CollectionUtils.isEmpty(offers)) {
        return;
      }
      targetOfferRepo.saveAll(offers.stream().filter(Objects::nonNull)
          .collect(Collectors.toList()));
    };
  }

}
