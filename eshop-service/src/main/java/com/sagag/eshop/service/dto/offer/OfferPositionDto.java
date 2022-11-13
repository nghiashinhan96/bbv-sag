package com.sagag.eshop.service.dto.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.eshop.service.helper.OfferCalculationHelper;
import com.sagag.services.common.enums.offer.OfferActionType;
import com.sagag.services.common.enums.offer.OfferPositionType;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class OfferPositionDto implements Serializable {

  private static final long serialVersionUID = -966987334627211277L;

  private Long id;

  @JsonIgnore
  private Long offerId;

  private String type;

  private String umsartId;

  @JsonIgnore
  private String shopArticleId;

  private String articleNumber;

  private String articleDescription;

  private String vehicleId;

  private String vehicleDescription;

  @JsonIgnore
  private Date calculated;

  private Integer sequence;

  private String context;

  private Double quantity;

  private String uomIso;

  @JsonIgnore
  private Integer currencyId;

  private Double grossPrice;

  private Double netPrice;

  private Double totalGrossPrice;

  private String remark;

  private String actionType;

  private Integer deliveryTypeId;

  private Date deliveryDate;

  @JsonIgnore
  private Long createdUserId;

  @JsonIgnore
  private Date createdDate;

  @JsonIgnore
  private Long modifiedUserId;

  @JsonIgnore
  private Date modifiedDate;

  private String makeId;

  private String modelId;

  private Double actionValue;

  @JsonIgnore
  private int version;

  private String vehicleBomDescription;

  private String catalogPath;

  @JsonIgnore
  private String additionalArticleDescription;

  private String connectVehicleId;

  @JsonProperty("pimId")
  private String sagsysId;

  private String awNumber;

  private DisplayedPriceDto displayedPrice;

  private String basketItemSourceId;

  private String basketItemSourceDesc;

  public OfferPositionDto(final OfferPosition offerPosition) {
    this.id = offerPosition.getId();
    if (offerPosition.getOffer() != null) {
      this.offerId = offerPosition.getOffer().getId();
    }
    this.type = OfferPositionType.getByValue(offerPosition.getType()).name();
    this.umsartId = offerPosition.getUmsartId();
    this.shopArticleId = Objects.isNull(offerPosition.getShopArticleId()) ? null
        : offerPosition.getShopArticleId().toString();
    this.articleNumber = offerPosition.getArticleNumber();
    this.vehicleId = offerPosition.getVehicleId();
    this.vehicleDescription = offerPosition.getVehicleDescription();
    this.context = offerPosition.getContext();
    this.quantity = offerPosition.getQuantity();
    this.uomIso = offerPosition.getUomIso();
    this.grossPrice =
        Objects.isNull(offerPosition.getGrossPrice()) ? 0 : offerPosition.getGrossPrice();
    this.netPrice = Objects.isNull(offerPosition.getNetPrice()) ? 0 : offerPosition.getNetPrice();
    this.totalGrossPrice =
        Objects.isNull(offerPosition.getTotalGrossPrice()) ? 0 : offerPosition.getTotalGrossPrice();
    this.deliveryTypeId = offerPosition.getDeliveryTypeId();
    this.deliveryDate = offerPosition.getDeliveryDate();
    this.makeId = offerPosition.getMakeId();
    this.modelId = offerPosition.getModelId();
    this.actionType = offerPosition.getActionType();
    this.remark = offerPosition.getRemark();
    this.vehicleBomDescription = offerPosition.getVehicleBomDescription();
    this.articleDescription = offerPosition.getArticleDescription();
    this.actionValue = offerPosition.getActionValue();

    this.connectVehicleId = offerPosition.getConnectVehicleId();
    this.sagsysId = offerPosition.getSagsysId();
    this.awNumber = offerPosition.getAwNumber();
    this.basketItemSourceId = offerPosition.getBasketItemSourceId();
    this.basketItemSourceDesc = offerPosition.getBasketItemSourceDesc();
    fillDisplayedPrice(offerPosition);
  }

  private void fillDisplayedPrice(final OfferPosition offerPosition) {
    final String savedDisplayedPriceType = offerPosition.getDisplayedPriceType();
    final String savedDisplayedPriceBrand = offerPosition.getDisplayedPriceBrand();
    final Long savedDisplayedPriceBrandId = offerPosition.getDisplayedPriceBrandId();
    if (StringUtils.isNoneEmpty(savedDisplayedPriceType, savedDisplayedPriceBrand)
        && Objects.nonNull(savedDisplayedPriceBrandId)) {
      this.displayedPrice = DisplayedPriceDto.builder().type(savedDisplayedPriceType)
          .brand(savedDisplayedPriceBrand).brandId(savedDisplayedPriceBrandId)
          .price(this.grossPrice).totalPrice(this.totalGrossPrice).build();
    }
  }

  @JsonIgnore
  public OfferPositionType getPositionType() {
    return OfferPositionType.valueOf(type);
  }

  @JsonIgnore
  public OfferActionType getOfferActionType() {
    return OfferActionType.valueOf(actionType);
  }

  public Double getTotalActionAmount() {
    if (getOfferActionType().isNone()) {
      return null;
    }
    return calcActionAmount().doubleValue();
  }

  @JsonIgnore
  public BigDecimal calcActionAmount() {
    return OfferCalculationHelper.calcActionAmount(this);
  }

  public Double getGrossPrice() {
    return Optional.ofNullable(displayedPrice).map(DisplayedPriceDto::getPrice).orElse(grossPrice);
  }

  public Double getTotalGrossPrice() {
    return Optional.ofNullable(displayedPrice).map(price -> price.reCalculateTotalPrice(quantity))
        .orElse(totalGrossPrice);
  }
}
