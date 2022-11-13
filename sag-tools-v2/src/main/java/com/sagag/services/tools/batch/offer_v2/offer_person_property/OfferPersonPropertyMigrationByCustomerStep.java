package com.sagag.services.tools.batch.offer_v2.offer_person_property;

import com.sagag.services.tools.batch.offer_feature.offer_person.OfferPersonType;
import com.sagag.services.tools.batch.offer_v2.AbstractOfferMigrationSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourcePersonProperty;
import com.sagag.services.tools.domain.target.TargetOfferPersonProperty;
import com.sagag.services.tools.domain.target.TargetOfferPersonPropertyId;
import com.sagag.services.tools.query.SqlOfferPersonQueryBuilder;
import com.sagag.services.tools.repository.source.SourcePersonPropertyRepository;
import com.sagag.services.tools.repository.target.TargetOfferPersonPropertyRepository;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER_PERSON_PROPERTY)
@Slf4j
public class OfferPersonPropertyMigrationByCustomerStep
    extends AbstractOfferMigrationSimpleStep<Long, List<TargetOfferPersonProperty>> {

  @Autowired(required=false)
  private SourcePersonPropertyRepository sourcePersonPropertyRepo;

  @Autowired
  private TargetOfferPersonPropertyRepository targetOfferPersonPropertyRepo;

  @Override
  public SimpleStepBuilder<Long, List<TargetOfferPersonProperty>> stepBuilder() {
    return stepBuilder("offerPersonProperyMigrationByCustomerStep");
  }

  @Bean("offerPersonProperyMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<Long> itemReader() throws Exception {
    final List<Long> vendorIds =
        mappingUserIdEblConnectService.searchVendorIds(sysVars.getCustNrs());
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

  @Bean("offerPersonProperyMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<Long, List<TargetOfferPersonProperty>> itemProcessor() {
    return personId -> {
      log.debug("Offer Person ID = {}", personId);
      List<SourcePersonProperty> personProperties = sourcePersonPropertyRepo.findByPersonId(personId);
      if (CollectionUtils.isEmpty(personProperties)) {
        return Collections.emptyList();
      }
      return personProperties.stream()
        .peek(item -> log.debug("Source offer person property = {}", item))
        .map(property -> mapSourceToTarget(property))
        .filter(Objects::nonNull)
        .peek(target -> log.debug("Target offer person property = {}", target))
        .collect(Collectors.toList());
    };
  }

  private static TargetOfferPersonProperty mapSourceToTarget(SourcePersonProperty source) {
    TargetOfferPersonProperty target = new TargetOfferPersonProperty();
    TargetOfferPersonPropertyId targetOfferPersonPropertyId = new TargetOfferPersonPropertyId();
    targetOfferPersonPropertyId.setPersonId(source.getSourcePersonPropertyId().getPersonId());
    targetOfferPersonPropertyId.setType(source.getSourcePersonPropertyId().getType());
    target.setTargetOfferPersonPropertyId(targetOfferPersonPropertyId);
    final String value = StringUtils.defaultString(source.getValue());
    target.setValue(DefaultUtils.toUtf8Value(value));
    return target;
  }

  @Bean("offerPersonProperyMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<List<TargetOfferPersonProperty>> itemWriter() {
    return items -> {
      final SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
      EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER_PERSON.getTableName());
      final List<TargetOfferPersonProperty> properties = items.stream()
          .flatMap(item -> item.stream())
          .collect(Collectors.toList());
      targetOfferPersonPropertyRepo.saveAll(properties);
    };
  }

}
