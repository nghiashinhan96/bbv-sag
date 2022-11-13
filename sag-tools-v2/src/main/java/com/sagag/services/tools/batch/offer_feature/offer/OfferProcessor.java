package com.sagag.services.tools.batch.offer_feature.offer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.support.CommonInitialResource;

@StepScope
@Component
@OracleProfile
public class OfferProcessor implements ItemProcessor<SourceOffer, TargetOffer> {

  @Value("${default.userId:}")
  private Long defaultUserId;

  @Value("${default.organisationId:}")
  private Integer defaultOrgId;

  @Autowired
  private CommonInitialResource commonInitialResource;

  @Override
  public TargetOffer process(SourceOffer source) {
    // Find org id
    Integer orgId = defaultOrgId;
    Long connectOwnerUserId = defaultUserId;
    Long createdUserId = defaultUserId;
    Long modifiedUserId = null;
    Long vendorId = source.getVendorId();
    orgId = commonInitialResource.getOrgId(vendorId);

    // Find owner id
    Long eblOwnerId = source.getOwnerId() == null ? defaultUserId : source.getOwnerId();
    connectOwnerUserId = commonInitialResource.getDefaultUserId(eblOwnerId, defaultUserId);

    createdUserId =
      commonInitialResource.getDefaultUserId(source.getUserCreateId(), defaultUserId);
    modifiedUserId =
      commonInitialResource.getDefaultModifiedUserId(source.getUserModifyId(), defaultUserId);

    Integer currencyId = commonInitialResource.getCurrencyId(source.getCurrencyIso());
    return mapSourceToTarget(source, connectOwnerUserId, orgId, currencyId,
      createdUserId, modifiedUserId);
  }

  private static TargetOffer mapSourceToTarget(SourceOffer source, Long connectOwnerUserId,
    Integer orgId, Integer currencyId, Long createdUserId, Long modifiedUserId) {
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
}
