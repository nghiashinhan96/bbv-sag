package com.sagag.services.tools.batch.offer_v2.offer;

import com.sagag.services.tools.batch.offer_v2.AbstractOfferMigrationSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.repository.target.CurrencyRepository;
import com.sagag.services.tools.repository.target.MappingUserIdEblConnectRepository;
import com.sagag.services.tools.repository.target.TargetOfferRepository;
import com.sagag.services.tools.service.MappingUserIdEblConnectService;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER)
@Slf4j
public class OfferMigrationByCustomerStep extends AbstractOfferMigrationSimpleStep<SourceOffer, TargetOffer> {

  @Value("${default.userId:}")
  private Long defaultUserId;

  @Value("${default.organisationId:}")
  private Integer defaultOrgId;

  @Autowired
  private CurrencyRepository currencyRepo;

  @Autowired
  private MappingUserIdEblConnectService mappingService;

  @Autowired
  private MappingUserIdEblConnectRepository mappingUserIdEblConnectRepo;

  @Autowired
  private TargetOfferRepository targetOfferRepo;

  @Override
  public SimpleStepBuilder<SourceOffer, TargetOffer> stepBuilder() {
    return stepBuilder("offerMigrationByCustomerStep");
  }

  @Bean("offerMigrationByCustomerStepItemReader")
  @StepScope
  @Override
  public ItemStreamReader<SourceOffer> itemReader() throws Exception {
    final List<Long> vendorIds =
        mappingUserIdEblConnectService.searchVendorIds(sysVars.getCustNrs());
    final OfferJpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

      @Override
      public Query createQuery() {
        final String queryStr = "select op from SourceOffer op where op.vendorId IN :vendorIds";
        return this.getEntityManager().createQuery(queryStr, SourceOffer.class)
            .setParameter("vendorIds", vendorIds);
      }

    };
    return jpaPagingItemReader(sourceEntityManagerFactory, queryProvider);
  };

  @Bean("offerMigrationByCustomerStepItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<SourceOffer, TargetOffer> itemProcessor() {
    return source -> {
      log.debug("Source Offer ID = {}", source.getId());
      // Find org id
      Integer orgId = defaultOrgId;
      Long connectOwnerUserId = defaultUserId;
      Long createdUserId = defaultUserId;
      Long modifiedUserId = null;
      Long vendorId = source.getVendorId();
      orgId = DefaultUtils.defaultInt(mappingUserIdEblConnectRepo.findOrgIdByEbl(vendorId), defaultOrgId);

      // Find owner id
      Long eblOwnerId = source.getOwnerId() == null ? defaultUserId : source.getOwnerId();
      connectOwnerUserId = DefaultUtils.defaultLong(mappingService.searchUserIdByEbl(eblOwnerId), defaultUserId);

      createdUserId = DefaultUtils.defaultLong(mappingService.searchUserIdByEbl(source.getUserCreateId()), defaultUserId);
      modifiedUserId = mappingService.searchUserIdByEbl(source.getUserModifyId());

      Integer currencyId = currencyRepo.findIdByIso(source.getCurrencyIso());
      return mapSourceToTarget(source, connectOwnerUserId, orgId, currencyId, createdUserId, modifiedUserId);
    };
  }

  private static TargetOffer mapSourceToTarget(SourceOffer source, Long connectOwnerUserId, Integer orgId, Integer currencyId, Long createdUserId,
      Long modifiedUserId) {
    Assert.notNull(source, "The given source offer must not be null");
    TargetOffer targetOffer = new TargetOffer();
    targetOffer.setId(source.getId());
    targetOffer.setOfferNumber(source.getOfferNumber());
    targetOffer.setOrganisationId(orgId);
    targetOffer.setOwnerUserId(connectOwnerUserId);
    targetOffer.setType(source.getOfferType());
    targetOffer.setStatus(source.getStatus());
    targetOffer.setOfferDate(source.getOfferDate());
    targetOffer.setRecipientId(source.getRecipientId());
    targetOffer.setRecipientAddressId(source.getRecipientAddressId());
    targetOffer.setTotalGrossPrice(source.getTotalLongPrice());
    targetOffer.setDeliveryDate(source.getDeliveryDate());
    targetOffer.setRemark(source.getRemark());
    targetOffer.setDeliveryLocation(source.getDeliveryLocation());
    targetOffer.setCreatedUserId(createdUserId);
    targetOffer.setCreatedDate(source.getDateCreate());
    targetOffer.setModifiedUserId(modifiedUserId);
    targetOffer.setModifiedDate(source.getDateModify());
    targetOffer.setTecstate(source.getTecState());
    targetOffer.setCurrencyId(currencyId);
    targetOffer.setVat(source.getVat());
    targetOffer.setAltOfferPriceUsed(source.getAltOfferPriceUsed());
    targetOffer.setVersion(source.getVersion().intValue());
    return targetOffer;
  }

  @Bean("offerMigrationByCustomerStepItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetOffer> itemWriter() {
    return items -> {
      // Update custom config to set exactly id
      SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
      EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER.getTableName());
      targetOfferRepo.saveAll(items.stream().filter(item -> Objects.nonNull(item)).collect(Collectors.toList()));
    };
  }

}
