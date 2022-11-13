package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.source.SourceOfferRepository;
import com.sagag.services.tools.repository.target.TargetOfferRepository;
import com.sagag.services.tools.support.CommonInitialResource;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.EnvUtils;
import com.sagag.services.tools.utils.ToolConstants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.SystemUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation class for migrating offer from oracle.
 */
@Component
@OracleProfile
@Slf4j
public class MigrateOfferFromOrclTasklet extends AbstractTasklet {

  private static final String TABLE = OfferTables.OFFER.getTableName();

  @Autowired(required = false)
  private SourceOfferRepository sourceOfferRepo;

  @Autowired
  private TargetOfferRepository targetOfferRepo;

  @Autowired
  protected CommonInitialResource commonInitialResource;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    long totalRecords = sourceOfferRepo.count();
    int size = ToolConstants.MAX_SIZE;
    int totalPages = (int) (totalRecords / size) + 1;

    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, TABLE);

    final List<SourceOffer> notMigratedList = new ArrayList<>();
    for (int curPage = 0; curPage < totalPages; curPage++) {
      final Page<SourceOffer> offers = sourceOfferRepo.findAll(PageRequest.of(curPage, size));
      if (!offers.hasContent()) {
        continue;
      }

      List<TargetOffer> targetOfferList = offers.getContent().stream()
        .peek(source -> log.debug("Source offer = {}", source))
        .map(source -> {
          // Find org id
          Integer orgId = defaultOrgId;
          Long connectOwnerUserId = defaultUserId;
          Long createdUserId = defaultUserId;
          Long modifiedUserId = null;
          if (EnvUtils.isProductionEnv(activeProfile)) {
            Long vendorId = source.getVendorId();
            orgId = commonInitialResource.getOrgId(vendorId);
            if (orgId == null) {
              notMigratedList.add(source);
              return null;
            }
          }

          Integer currencyId = commonInitialResource.getCurrencyId(source.getCurrencyIso());
          return mapSourceToTarget(source, connectOwnerUserId, orgId, currencyId,
            createdUserId, modifiedUserId);
        })
        .filter(Objects::nonNull)
        .peek(target -> log.debug("Target offer = {}", target))
        .collect(Collectors.toList());

      targetOfferRepo.saveAll(targetOfferList);
    }

    CsvUtils.write(SystemUtils.getUserDir() + "/csv/offer_not_migrated_data.csv", notMigratedList);

    return finish(contribution);
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
