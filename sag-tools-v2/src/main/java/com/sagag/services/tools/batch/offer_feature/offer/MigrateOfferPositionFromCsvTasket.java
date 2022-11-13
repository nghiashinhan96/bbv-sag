package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvOfferPosition;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.repository.target.TargetOfferPositionRepository;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@OracleProfile
public class MigrateOfferPositionFromCsvTasket extends AbstractTasklet {

  @Value("${file.dir.offer:${user.dir}/csv/position/}")
  private String offerPositionDirPath;

  @Autowired
  private TargetOfferPositionRepository targetOfferPositionRepo;

  @Autowired
  protected CommonInitialResource commonInitialResource;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws Exception {

    final File positionDir = new File(offerPositionDirPath);
    if (!positionDir.isDirectory()) {
      throw new IllegalArgumentException("Not found any directories with this path");
    }
    final File[] files = positionDir.listFiles();
    if (ArrayUtils.isEmpty(files)) {
      throw new IllegalArgumentException("Not found any files in this directory");
    }

    final List<CsvOfferPosition> notMigratedList = new ArrayList<>();

    List<CsvOfferPosition> positions;
    List<TargetOfferPosition> targetOfferPositionList;
    for (File file : files) {
      positions = CsvUtils.read(file, CsvOfferPosition.class);
      if (CollectionUtils.isEmpty(positions)) {
        continue;
      }

      targetOfferPositionList = positions.stream()
        .map(source -> {
          Integer currencyId = commonInitialResource.getCurrencyId(source.getCurrencyIso());
          Integer deliveryTypeId = 1;
          return mapSourceToTarget(source, deliveryTypeId, currencyId);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

      targetOfferPositionRepo.saveAll(targetOfferPositionList);
    }

    CsvUtils.write(SystemUtils.getUserDir() + "/csv/positions_not_migrated_data.csv", notMigratedList);

    return finish(contribution);
  }

  private static TargetOfferPosition mapSourceToTarget(final CsvOfferPosition source,
    final Integer deliveryTypeId, final Integer currencyId) {
    final TargetOfferPosition target = new TargetOfferPosition();
    target.setId(source.getId());
    target.setOfferId(source.getOfferId());
    target.setType(source.getType());
    target.setUmsartId(source.getUmsartId().toString());
    target.setShopArticleId(source.getShopArticleId());
    target.setArticleNumber(source.getArticleNumber());
    target.setArticleDescription(source.getArticleDescription());
    target.setVehicleId(source.getVehicleId());
    target.setVehicleDescription(source.getVehicleDescription());
    target.setCalculated(source.getCalculated());
    target.setSequence(source.getSequence().intValue());
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
    target.setCreatedUserId(source.getUserCreatedId());
    target.setCreatedDate(source.getDateCreated());
    target.setModifiedUserId(source.getUserModifiedId());
    target.setModifiedDate(source.getDateModified());
    target.setTecstate(source.getTecstate());
    target.setMakeId(source.getMakeId().toString());
    target.setModelId(source.getModelId().toString());
    target.setVehicleBomId(source.getVehicleBomId().toString());
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
