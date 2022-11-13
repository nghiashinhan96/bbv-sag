package com.sagag.services.tools.batch.offer_v2.offer_position;

import com.sagag.services.tools.batch.offer_v2.AbstractOfferMigrationSimpleStep;
import com.sagag.services.tools.batch.offer_v2.OfferJpaQueryProvider;
import com.sagag.services.tools.batch.offer_v2.OfferUtils;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOfferPosition;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.repository.target.CurrencyRepository;
import com.sagag.services.tools.repository.target.DeliveryTypesRepository;
import com.sagag.services.tools.repository.target.TargetOfferPositionRepository;
import com.sagag.services.tools.service.MappingUserIdEblConnectService;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.DefaultUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Query;

@Configuration
@OracleProfile
@Order(OfferUtils.ORDERED_OFFER_POSITION)
@Slf4j
public class OfferPositionMigrationByCustomerStep
  extends AbstractOfferMigrationSimpleStep<SourceOfferPosition, TargetOfferPosition> {

  @Value("${default.userId:}")
  private Long defaultUserId;

  @Autowired
  private CurrencyRepository currencyRepo;

  @Autowired
  private DeliveryTypesRepository deliveryTypeRepo;

  @Autowired
  private MappingUserIdEblConnectService mappingService;

  @Autowired
  private TargetOfferPositionRepository targetOfferPositionRepo;

  @Override
  public SimpleStepBuilder<SourceOfferPosition, TargetOfferPosition> stepBuilder() {
    return stepBuilder("MigrateOfferPositionByCustomer");
  }

  @Bean("MigrateOfferPositionByCustomerItemReader")
  @StepScope
  @Override
  public JpaPagingItemReader<SourceOfferPosition> itemReader() throws Exception {
    final List<Long> vendorIds =
        mappingUserIdEblConnectService.searchVendorIds(sysVars.getCustNrs());
    OfferJpaQueryProvider queryProvider = new OfferJpaQueryProvider() {

      @Override
      public Query createQuery() {
        final String queryStr = "select op from SourceOfferPosition op "
            + "inner join op.offer o where o.vendorId IN :vendorIds";
        return this.getEntityManager().createQuery(queryStr, SourceOfferPosition.class)
            .setParameter("vendorIds", vendorIds);
      }
    };
    return jpaPagingItemReader(sourceEntityManagerFactory, queryProvider);
  }

  @Bean("MigrateOfferPositionByCustomerItemProcessor")
  @StepScope
  @Override
  public ItemProcessor<SourceOfferPosition, TargetOfferPosition> itemProcessor() {
    return source -> {
      log.debug("Source Offer Position ID = {}", source.getId());
      Integer currencyId = currencyRepo.findIdByIso(source.getCurrencyIso());
      Integer deliveryTypeId = deliveryTypeRepo.findIdByType(source.getDeliveryType());
      Long createdUserId = DefaultUtils.defaultLong(
        mappingService.searchUserIdByEbl(source.getUserCreatedId()), defaultUserId);
      Long modifiedUserId = mappingService.searchUserIdByEbl(source.getUserModifiedId());
      return mapSourceToTarget(source, deliveryTypeId, currencyId, createdUserId, modifiedUserId);
    };
  }

  private static TargetOfferPosition mapSourceToTarget(final SourceOfferPosition source,
    final Integer deliveryTypeId, final Integer currencyId, final Long createdUserId,
    final Long modifiedUserId) {
    final TargetOfferPosition target = new TargetOfferPosition();
    target.setId(source.getId());
    target.setOfferId(source.getOfferId());
    target.setType(source.getType());
    if (source.getUmsartId() != null) {
      target.setUmsartId(source.getUmsartId().toString());
    }

    target.setShopArticleId(source.getShopArticleId());
    target.setArticleNumber(source.getArticleNumber());
    target.setArticleDescription(source.getArticleDescription());
    target.setVehicleId(source.getVehicleId());
    target.setVehicleDescription(source.getVehicleDescription());
    target.setCalculated(source.getCalculated());
    if (source.getSequence() != null) {
      target.setSequence(source.getSequence().intValue());
    }

    target.setContext(source.getContext());
    target.setQuantity(source.getQuantity());
    target.setUomIso(source.getUomIso());
    target.setCurrencyId(currencyId);
    target.setGrossPrice(source.getLongPrice());
    target.setNetPrice(source.getNetPrice());
    target.setTotalGrossPrice(source.getTotalLongPrice());
    target.setRemark(source.getRemark());
    target.setActionType(source.getOfferActionType());
    target.setActionValue(source.getOfferActionValue());
    target.setDeliveryTypeId(deliveryTypeId);
    target.setDeliveryDate(source.getDeliveryDate());
    target.setCreatedUserId(createdUserId);
    target.setCreatedDate(source.getDateCreated());
    target.setModifiedUserId(modifiedUserId);
    target.setModifiedDate(source.getDateModified());
    target.setTecstate(source.getTecstate());
    if (source.getMakeId() != null) {
      target.setMakeId(source.getMakeId().toString());
    }
    if (source.getModelId() != null) {
      target.setModelId(source.getModelId().toString());
    }
    if (source.getVehicleBomId() != null) {
      target.setVehicleBomId(source.getVehicleBomId().toString());
    }

    target.setVehicleBomDescription(source.getVehicleBomDescription());
    target.setArticleEnhancedDescription(source.getArticleEnhanceDescription());
    target.setPricedUnit(source.getPricedUnit());
    target.setCatalogPath(source.getCatalogPath());
    target.setVehicleTypeCode(source.getVehicleTypeCode());
    target.setPricedQuantity(source.getPricedQuantity());
    target.setVersion(source.getVersion());
    return target;
  }

  @Bean("MigrateOfferPositionByCustomerItemWriter")
  @StepScope
  @Override
  public ItemWriter<TargetOfferPosition> itemWriter() {
    return items -> {
      SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
      EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER_POSITION.getTableName());
      targetOfferPositionRepo.saveAll(items.stream()
        .filter(item -> Objects.nonNull(item)).collect(Collectors.toList()));
    };
  }
}
