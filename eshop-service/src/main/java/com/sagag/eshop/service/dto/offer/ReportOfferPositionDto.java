package com.sagag.eshop.service.dto.offer;

import com.sagag.services.common.enums.offer.OfferPositionType;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

@Data
public class ReportOfferPositionDto implements Serializable {

  private static final long serialVersionUID = 7611821393751624363L;

  private String articleNumber;

  private String articleDescription;

  private String additionalArticleDescription;

  private BigInteger quantity;

  private String quantityForWorkArticle;

  private String longPrice;

  private String totalLongPrice;

  private BigDecimal offerActionValue;

  private OfferPositionType type;

  private OfferPositionDto offerPosition;

  private String actionAmount;

  public ReportOfferPositionDto(final OfferPositionDto offerPosition, NumberFormat numberFormat) {
    this.offerPosition = offerPosition;
    this.articleNumber = offerPosition.getArticleNumber();
    this.articleDescription = offerPosition.getArticleDescription();
    this.additionalArticleDescription = offerPosition.getAdditionalArticleDescription();
    final OfferPositionType positionType = offerPosition.getPositionType();
    this.type = positionType;
    if (positionType.isWork() || positionType.isClientArticle()) {
      this.quantityForWorkArticle =
          numberFormat.format(BigDecimal.valueOf(offerPosition.getQuantity()));
    } else {
      this.quantity = BigInteger.valueOf(offerPosition.getQuantity().longValue());
    }
    if (!this.type.isRemark()) {
      this.longPrice = numberFormat.format(BigDecimal.valueOf(offerPosition.getGrossPrice()));
    }
    this.totalLongPrice =
        numberFormat.format(BigDecimal.valueOf(offerPosition.getTotalGrossPrice()));
    if (offerPosition.getActionValue() != null) {
      this.offerActionValue = BigDecimal.valueOf(offerPosition.getActionValue());
    }
    this.actionAmount = numberFormat.format(offerPosition.calcActionAmount());
  }
}
