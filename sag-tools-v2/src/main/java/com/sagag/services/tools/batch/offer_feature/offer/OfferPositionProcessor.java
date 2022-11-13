package com.sagag.services.tools.batch.offer_feature.offer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOfferPosition;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.support.CommonInitialResource;

@StepScope
@Component
@OracleProfile
public class OfferPositionProcessor implements ItemProcessor<SourceOfferPosition, TargetOfferPosition> {

  @Value("${spring.profiles.active}")
  protected String activeProfile;

  @Value("${default.userId:}")
  protected Long defaultUserId;

  @Value("${default.organisationId:}")
  protected Integer defaultOrgId;

  @Autowired
  protected CommonInitialResource commonInitialResource;

  @Override
  public TargetOfferPosition process(SourceOfferPosition source) {
    Integer currencyId = commonInitialResource.getCurrencyId(source.getCurrencyIso());
    Integer deliveryTypeId = 
        commonInitialResource.getDeliveryTypeId(source.getDeliveryType());
    Long createdUserId =
        commonInitialResource.getDefaultCreatedUserId(source.getUserCreatedId(), defaultUserId);
    Long modifiedUserId =
        commonInitialResource.getDefaultModifiedUserId(source.getUserModifiedId(), defaultUserId);
    return mapSourceToTarget(source, deliveryTypeId, currencyId, createdUserId, modifiedUserId);
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
}
