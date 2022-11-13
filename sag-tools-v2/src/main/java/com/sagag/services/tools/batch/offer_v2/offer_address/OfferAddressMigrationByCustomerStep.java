package com.sagag.services.tools.batch.offer_v2.offer_address;

import com.sagag.services.tools.batch.offer_feature.offer_person.OfferPersonType;
import com.sagag.services.tools.batch.offer_v2.AbstractOfferMigrationSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceAddress;
import com.sagag.services.tools.domain.target.OfferAddress;
import com.sagag.services.tools.query.SqlOfferPersonQueryBuilder;
import com.sagag.services.tools.repository.source.SourceAddressRepository;
import com.sagag.services.tools.repository.target.OfferAddressRepository;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER_ADDRESS)
@Slf4j
public class OfferAddressMigrationByCustomerStep
  extends AbstractOfferMigrationSimpleStep<Long, Set<OfferAddress>> {

  @Autowired(required=false)
  private SourceAddressRepository sourceAddressRepository;

  @Autowired
  private OfferAddressRepository offerAddressRepo;

  @Override
  public SimpleStepBuilder<Long, Set<OfferAddress>> stepBuilder() {
    return stepBuilder("offerAddressMigrationByCustomerStep");
  }

  @Bean("offerAddressMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<Long> itemReader() throws Exception {
    final List<Long> vendorIds =
        mappingUserIdEblConnectService.searchVendorIds(sysVars.getCustNrs());
    final OfferJpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

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

  @Bean("offerAddressMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<Long, Set<OfferAddress>> itemProcessor() {
    return personId -> {
      log.debug("Offer Person ID = {}", personId);
      List<SourceAddress> sourceAddressList =
          sourceAddressRepository.findAddressByByPersonId(personId);
      log.debug("Source address list = {}", sourceAddressList);
      if (CollectionUtils.isEmpty(sourceAddressList)) {
        return Collections.emptySet();
      }

      final List<OfferAddress> offerAddresses = new ArrayList<>();
      offerAddresses.addAll(sourceAddressList.stream()
          .map(source -> mapSourceToTarget(personId, source))
          .collect(Collectors.toSet()));
      return sourceAddressList.stream()
            .map(source -> mapSourceToTarget(personId, source))
            .collect(Collectors.toSet());
    };
  }

  private static OfferAddress mapSourceToTarget(Long personId, SourceAddress source) {
    OfferAddress target = new OfferAddress();
    target.setId(source.getId());
    target.setPersonId(personId);
    target.setLine1(DefaultUtils.toUtf8Value(source.getLine1()));
    target.setLine2(DefaultUtils.toUtf8Value(source.getLine2()));
    target.setLine3(DefaultUtils.toUtf8Value(source.getLine3()));
    target.setCountryIso(source.getCountryIso());
    target.setState(DefaultUtils.toUtf8Value(source.getState()));
    target.setCity(DefaultUtils.toUtf8Value(source.getCity()));
    target.setZipCode(source.getZipCode());
    target.setErpId(source.getErpId() != null ? String.valueOf(source.getErpId()) : null);
    target.setType(source.getType());
    target.setPoBox(DefaultUtils.toUtf8Value(source.getPoBox()));
    target.setCreatedUserId(source.getUserCreatedId());
    target.setModifiedUserId(DefaultUtils.defaultModifiedUserId(source.getUserModifiedId()));
    target.setCreatedDate(source.getDateCreated());
    target.setModifiedDate(source.getDateModified());
    target.setTecstate(source.getTecstate());
    target.setVersion(source.getVersion());
    return target;
  }

  @Bean("offerAddressMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<Set<OfferAddress>> itemWriter() {
    return items -> {
      // Update custom config to set exactly id
      SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
      EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER_ADDRESS.getTableName());
      final List<OfferAddress> addresses = items.stream()
          .flatMap(item -> item.stream())
          .filter(Objects::nonNull)
          .collect(Collectors.toList());

      offerAddressRepo.saveAll(addresses);
    };
  }
}
