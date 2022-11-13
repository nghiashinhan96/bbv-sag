package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvOffer;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.repository.target.TargetOfferRepository;
import com.sagag.services.tools.support.CommonInitialResource;
import com.sagag.services.tools.utils.CsvUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Migrates offers data from CSV format task.
 */
@Component
@OracleProfile
public class MigrateOfferFromCsvTasklet extends AbstractTasklet {

  @Value("${file.dir.offer:${user.dir}/csv/offer/}")
  private String offerDirPath;

  @Autowired
  private TargetOfferRepository targetOfferRepo;

  @Autowired
  protected CommonInitialResource commonInitialResource;
  
  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {
    final File offerDir = new File(offerDirPath);
    if (!offerDir.isDirectory()) {
      throw new IllegalArgumentException("Not found any directories with this path");
    }
    final File[] files = offerDir.listFiles();
    if (ArrayUtils.isEmpty(files)) {
      throw new IllegalArgumentException("Not found any files in this directory");
    }

    final List<CsvOffer> notMigratedList = new ArrayList<>();

    List<CsvOffer> offers;
    List<TargetOffer> targetOfferList;
    for (File file : files) {
      offers = CsvUtils.read(file, CsvOffer.class);
      if (CollectionUtils.isEmpty(offers)) {
        continue;
      }

      targetOfferList = offers.stream()
        .map(source -> {
          Long vendorId = source.getVendorId();
          Integer orgId = commonInitialResource.getOrgId(vendorId);
          if (orgId == null) {
            notMigratedList.add(source);
            return null;
          }
          Integer currencyId = commonInitialResource.getCurrencyId(source.getCurrencyIso());
          return mapSourceToTarget(source, orgId, currencyId);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
      targetOfferRepo.saveAll(targetOfferList);
    }

    CsvUtils.write(SystemUtils.getUserDir() + "/csv/offer_not_migrated_data.csv", notMigratedList);

    return finish(contribution);
  }

  private static TargetOffer mapSourceToTarget(CsvOffer source, Integer orgId, Integer currencyId) {
    Assert.notNull(source, "The given source offer must not be null");

    TargetOffer targetOffer = new TargetOffer();
    targetOffer.setId(source.getId());
    targetOffer.setOfferNumber(source.getOfferNumber());
    targetOffer.setOrganisationId(orgId);
    targetOffer.setOwnerUserId(source.getOwnerId());
    targetOffer.setType(source.getOfferType());
    targetOffer.setStatus(source.getStatus());
    targetOffer.setOfferDate(source.getOfferDate());
    targetOffer.setRecipientId(source.getRecipientId());
    targetOffer.setRecipientAddressId(source.getRecipientAddressId());
    targetOffer.setTotalGrossPrice(source.getTotalLongPrice());
    targetOffer.setDeliveryDate(source.getDeliveryDate());
    targetOffer.setRemark(source.getRemark());
    targetOffer.setDeliveryLocation(source.getDeliveryLocation());
    targetOffer.setCreatedUserId(source.getUserCreatedId());
    targetOffer.setCreatedDate(source.getDateCreated());
    targetOffer.setModifiedUserId(source.getUserModifiedId());
    targetOffer.setModifiedDate(source.getDateModified());
    targetOffer.setTecstate(source.getTecstate());
    targetOffer.setCurrencyId(currencyId);
    targetOffer.setVat(source.getVat());
    targetOffer.setAltOfferPriceUsed(source.getAltToOfferPriceUsed());
    targetOffer.setVersion(source.getVersion());

    return targetOffer;
  }

}
