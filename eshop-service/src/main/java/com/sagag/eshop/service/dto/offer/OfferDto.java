package com.sagag.eshop.service.dto.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.eshop.repo.entity.offer.Offer;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.eshop.service.converter.OfferPersonConverters;
import com.sagag.eshop.service.converter.OfferPositionConverters;
import com.sagag.eshop.service.helper.OfferCalculationHelper;
import com.sagag.services.common.enums.offer.OfferActionType;
import com.sagag.services.common.enums.offer.OfferPositionType;
import com.sagag.services.common.enums.offer.OfferTecStateType;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.common.utils.SagBeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferDto implements Serializable {

  private static final long serialVersionUID = -966987334627211277L;

  private Long id;

  private String offerNr; // max length 10

  private OfferPersonDto offerPerson;

  private String customerNr;

  private Date offerDate;

  private Date deliveryDate;

  private String remark;

  private String status;

  private List<OfferPositionDto> offerPositions;

  private Double vat;

  private String currencyIsoCode;

  @JsonIgnore
  private Long currentUserId;

  @JsonIgnore
  private Integer organisationId;

  @JsonIgnore
  private OfferAddressDto recipientAddress;

  public OfferDto(final Offer savedOffer) {
    Assert.notNull(savedOffer, "The given offer must not be null");
    this.id = savedOffer.getId();
    this.offerNr = savedOffer.getOfferNumber();

    this.customerNr = savedOffer.getOrganisation().getOrgCode();
    this.offerDate = savedOffer.getOfferDate();
    this.deliveryDate = savedOffer.getDeliveryDate();
    this.remark = savedOffer.getRemark();
    this.status = savedOffer.getStatus();
    this.vat = savedOffer.getVat();
    final Optional<OfferPerson> recipient = Optional.ofNullable(savedOffer.getRecipient());
    recipient.ifPresent(obj -> this.offerPerson = OfferPersonConverters.convert(savedOffer.getRecipient()));
    if (savedOffer.getCurrency() != null) {
      this.currencyIsoCode = savedOffer.getCurrency().getIsoCode();
    }
    final OfferAddress srcRecipientAddress = savedOffer.getRecipientAddress();
    if (srcRecipientAddress != null) {
      this.recipientAddress = SagBeanUtils.map(srcRecipientAddress, OfferAddressDto.class);
    }
    this.offerPositions = buildOfferPositions(savedOffer.getOfferPositions());
  }

  private List<OfferPositionDto> buildOfferPositions(final List<OfferPosition> offerPositions) {
    if (CollectionUtils.isEmpty(offerPositions)) {
      return Collections.emptyList();
    }
    return offerPositions.stream()
        .filter(offerPosition -> offerPosition.getTecstate().equals(OfferTecStateType.ACTIVE.name()))
        .map(OfferPositionConverters::convert).collect(Collectors.toList());
  }

  @JsonIgnore
  public List<OfferPositionDto> getVendorArticleList() {
    return getOfferPositionsByType(OfferPositionType.VENDOR_ARTICLE);
  }

  @JsonIgnore
  public List<OfferPositionDto> getVendorArticleWithoutVehicleList() {
    return getOfferPositionsByType(OfferPositionType.VENDOR_ARTICLE_WITHOUT_VEHICLE);
  }

  @JsonIgnore
  public List<OfferPositionDto> getClientArticleList() {
    return getOfferPositionsByType(OfferPositionType.CLIENT_ARTICLE);
  }

  @JsonIgnore
  public List<OfferPositionDto> getClientWorkList() {
    return getOfferPositionsByType(OfferPositionType.CLIENT_WORK);
  }

  @JsonIgnore
  public List<OfferPositionDto> getRemarkList() {
    return getOfferPositionsByType(OfferPositionType.REMARK);
  }

  @JsonIgnore
  public List<OfferPositionDto> getWorkList() {
      return getOfferPositionsByType(OfferPositionType.VEHICLE_INFO_PROVIDER_WORK);
  }

  private List<OfferPositionDto> getOfferPositionsByType(OfferPositionType type) {
    if (CollectionUtils.isEmpty(offerPositions) || Objects.isNull(type)) {
      return Collections.emptyList();
    }
    return offerPositions.stream().filter(position -> type.equals(position.getPositionType()))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public double calcArticleAmount() {
    if (CollectionUtils.isEmpty(offerPositions)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    final List<OfferPositionDto> positions = offerPositions.stream()
        .filter(position -> position.getPositionType().isArticle())
        .collect(Collectors.toList());
    return OfferCalculationHelper.calcTotalGrossPrice(positions);
  }

  @JsonIgnore
  public double calcClientArticleAmount() {
    return OfferCalculationHelper.calcTotalGrossPrice(getClientArticleList());
  }

  @JsonIgnore
  public double calcWorkAmount() {
    final List<OfferPositionDto> workPositions = new ArrayList<>();
    workPositions.addAll(getClientWorkList());
    workPositions.addAll(getWorkList());
    workPositions.addAll(getHaynesProList());
    return OfferCalculationHelper.calcTotalGrossPrice(workPositions);
  }

  @JsonIgnore
  public double calcRemarkAmount() {
    if (CollectionUtils.isEmpty(offerPositions)) {
      return NumberUtils.DOUBLE_ZERO;
    }

    fillRemarks();

    BigDecimal totalRemarkAmount = BigDecimal.ZERO;
    for (OfferPositionDto position : offerPositions) {
      totalRemarkAmount = totalRemarkAmount.add(position.calcActionAmount());
    }
    return totalRemarkAmount.doubleValue();
  }

  public void fillRemarks() {
    if (CollectionUtils.isEmpty(offerPositions)) {
      return;
    }
    final BigDecimal totalWorkAmount = BigDecimal.valueOf(calcWorkAmount());
    final BigDecimal total = BigDecimal.valueOf(calcArticleAmount()).add(totalWorkAmount);

    for (final OfferPositionDto position : offerPositions) {
      if (!position.getPositionType().isRemark()) {
        continue;
      }

      final OfferActionType actionType = position.getOfferActionType();
      if (actionType.isAdditionPercent() || actionType.isDiscountPercent()) {
        position.setTotalGrossPrice(total.doubleValue());
      } else if (actionType.isAdditionPercentWork() || actionType.isDiscountPercentWork()) {
        position.setTotalGrossPrice(totalWorkAmount.doubleValue());
      } else if (actionType.isAdditionPercentArticles() || actionType.isDiscountPercentArticles()) {
        position.setTotalGrossPrice(calcClientArticleAmount());
      }
    }
  }

  @JsonIgnore
  public double calcTotalExcludeVatAmount() {
    return OfferCalculationHelper.calcTotalExcludeVatAmount(calcArticleAmount(), calcWorkAmount(),
        calcRemarkAmount());
  }

  @JsonIgnore
  public double calcVatAmount() {
    if (Objects.isNull(vat)) {
      return calcTotalExcludeVatAmount();
    }
    return OfferCalculationHelper.calcTotalIncludeVatAmount(calcTotalExcludeVatAmount(), vat);
  }

  @JsonIgnore
  public double updateTotalAmount() {
    final BigDecimal total =
        BigDecimal.valueOf(calcTotalExcludeVatAmount()).add(BigDecimal.valueOf(calcVatAmount()));
    return OfferCalculationHelper.round(total, currencyIsoCode).doubleValue();
  }

  public double getTotalExcludeVat() {
    return calcTotalExcludeVatAmount();
  }

  public double getTotalIncludeVat() {
    return updateTotalAmount();
  }

  public double getTotalVat() {
    return calcVatAmount();
  }

  public double getTotalWork() {
    return calcWorkAmount();
  }

  public double getTotalArticle() {
    return calcArticleAmount();
  }

  public double getTotalOwnArticle() {
    return calcClientArticleAmount();
  }

  public double getTotalRemark() {
    return calcRemarkAmount();
  }

  @JsonIgnore
  public void fillReceipientAddress() {
    if (Objects.isNull(offerPerson)) {
      return;
    }
    recipientAddress = new OfferAddressDto();
    recipientAddress.setLine1(offerPerson.getRoad());
    recipientAddress.setLine2(offerPerson.getAdditionalAddress1());
    recipientAddress.setLine3(offerPerson.getAdditionalAddress2());
    recipientAddress.setZipcode(offerPerson.getPostCode());
    recipientAddress.setPoBox(offerPerson.getPoBox());
    recipientAddress.setCity(offerPerson.getPlace());
  }

  public String getFormattedOfferDate() {
    if (Objects.isNull(this.offerDate)) {
      return StringUtils.EMPTY;
    }
    return DateUtils.toStringDate(this.offerDate, DateUtils.DEFAULT_DATE_PATTERN);
  }

  public String getFormattedDeliveryDate() {
    if (Objects.isNull(this.deliveryDate)) {
      return StringUtils.EMPTY;
    }
    return DateUtils.toStringDate(this.deliveryDate, DateUtils.DEFAULT_DATE_PATTERN);
  }

  @JsonIgnore
  public List<OfferPositionDto> getHaynesProList() {
    return getOfferPositionsByType(OfferPositionType.HAYNESPRO_PROVIDER_WORK);
  }

}
