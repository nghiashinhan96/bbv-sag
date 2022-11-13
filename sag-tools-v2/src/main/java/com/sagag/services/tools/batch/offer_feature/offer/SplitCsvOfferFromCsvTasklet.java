package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractTasklet;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvOffer;
import com.sagag.services.tools.domain.csv.CsvStringOffer;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;
import com.sagag.services.tools.utils.DateUtils;
import com.sagag.services.tools.utils.DefaultUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@OracleProfile
public class SplitCsvOfferFromCsvTasklet extends AbstractTasklet {

  @Value("${file.csv.offer:/csv/import/OFFER.csv}")
  private String offerFilePath;

  @Value("${csv.separtor:;}")
  private char separator;

  @Value("${file.dir.offer:${user.dir}/csv/offer/offer_}")
  private String offerDirPath;

  @Override
  public RepeatStatus execute(StepContribution contribution,
    ChunkContext chunkContext) throws BatchJobException {

    // Load original offer file
    log.info("File path = {}", offerFilePath);
    log.info("System separator = {}", separator);
    log.info("Export offer directory path = {}", offerDirPath);

    File file = new File(SystemUtils.getUserDir() + offerFilePath);
    List<CsvStringOffer> content = CsvUtils.read(file, separator, StandardCharsets.UTF_8,
        CsvStringOffer.class, false);
    if (CollectionUtils.isEmpty(content)) {
      log.warn("No any splitters is executed");
      return finish(contribution);
    }
    List<CsvOffer> splitContent =
      content.stream()
        .peek(source -> log.debug("Source = {}", source))
        .map(item -> mapSourceToTarget(item))
        .peek(target -> log.debug("Target = {}", target))
        .collect(Collectors.toList());
    CsvUtils.split(offerDirPath, splitContent);
    return finish(contribution);
  }

  private static CsvOffer mapSourceToTarget(CsvStringOffer source) {
    final Long id = NumberUtils.createLong(DefaultUtils.handleNullStr(source.getId()));
    final String offerNumber = DefaultUtils.handleNullStr(source.getOfferNumber());
    final Long vendorId = NumberUtils.createLong(DefaultUtils.handleNullStr(source.getVendorId()));
    final Long clientId = NumberUtils.createLong(DefaultUtils.handleNullStr(source.getClientId()));
    final Long ownerId = NumberUtils.createLong(DefaultUtils.handleNullStr(source.getOwnerId()));
    final String status = DefaultUtils.handleNullStr(source.getStatus());
    final Date offerDate =
      DateUtils.toDateByDefaultPattern(DefaultUtils.handleNullStr(source.getOfferDate()));
    final Long recipientId =
      NumberUtils.createLong(DefaultUtils.handleNullStr(source.getRecipientId()));
    final Long recipientAddressId =
      NumberUtils.createLong(DefaultUtils.handleNullStr(source.getRecipientAddressId()));
    final Double totalLongPrice =
      NumberUtils.createDouble(DefaultUtils.handleNullStr(source.getTotalLongPrice()));
    final Date deliveryDate =
      DateUtils.toDateTimeByDefaultPattern(DefaultUtils.handleNullStr(source.getDeliveryDate()));
    final String remark = DefaultUtils.handleNullStr(source.getRemark());
    final String deliveryLocation = DefaultUtils.handleNullStr(source.getDeliveryLocation());
    final String currencyIso = DefaultUtils.handleNullStr(source.getCurrencyIso());
    final Double vat = NumberUtils.createDouble(DefaultUtils.handleNullStr(source.getVat()));
    final String offerType = source.getOfferType();
    final Boolean altToOfferPriceUsed =
      DefaultUtils.handleBooleanStr(source.getAltToOfferPriceUsed());
    final Long createdUserId =
      NumberUtils.createLong(DefaultUtils.handleNullStr(source.getUserCreatedId()));
    final Date createdDate =
      DateUtils.toDateTimeByDefaultPattern(DefaultUtils.handleNullStr(source.getDateCreated()));
    final Long modifiedUserId =
      NumberUtils.createLong(DefaultUtils.handleNullStr(source.getUserModifiedId()));
    final Date modifiedDate =
      DateUtils.toDateTimeByDefaultPattern(DefaultUtils.handleNullStr(source.getDateModified()));
    final Integer version = NumberUtils.createInteger(DefaultUtils.handleNullStr(source.getVersion()));
    final String tecState = DefaultUtils.handleNullStr(source.getTecstate());

    CsvOffer target = new CsvOffer();
    target.setId(id);
    target.setOfferNumber(offerNumber);
    target.setVendorId(vendorId);
    target.setClientId(clientId);
    target.setOwnerId(ownerId);
    target.setStatus(status);
    target.setOfferDate(offerDate);
    target.setRecipientId(recipientId);
    target.setRecipientAddressId(recipientAddressId);
    target.setTotalLongPrice(totalLongPrice);
    target.setDeliveryDate(deliveryDate);
    target.setRemark(remark);
    target.setDeliveryLocation(deliveryLocation);
    target.setCurrencyIso(currencyIso);
    target.setVat(vat);
    target.setOfferType(offerType);
    target.setAltToOfferPriceUsed(altToOfferPriceUsed);
    target.setUserCreatedId(createdUserId);
    target.setDateCreated(createdDate);
    target.setUserModifiedId(modifiedUserId);
    target.setDateModified(modifiedDate);
    target.setVersion(version);
    target.setTecstate(tecState);
    return target;
  }

}
