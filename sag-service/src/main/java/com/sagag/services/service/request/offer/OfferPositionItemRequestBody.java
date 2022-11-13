package com.sagag.services.service.request.offer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagag.eshop.repo.entity.offer.OfferPosition;
import com.sagag.eshop.service.dto.offer.OfferPositionDto;
import com.sagag.services.common.enums.offer.OfferActionType;
import com.sagag.services.common.enums.offer.OfferPositionType;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferPositionItemRequestBody implements Serializable {

  private static final long serialVersionUID = 7792703194723985479L;

  private Long offerPositionId;

  private OfferPositionType type;

  private String umsartId;

  private String shopArticleId;

  private String articleNumber;

  private String articleDescription;

  private String vehicleId;

  private String vehicleDescription;

  private Integer sequence;

  private String context;

  private Double quantity;

  private String uomIso;

  private Double grossPrice;

  private Double netPrice;

  private Double totalGrossPrice;

  private String remark;

  private OfferActionType actionType; // NONE for no article without discount

  private Integer deliveryTypeId;

  private String makeId;

  private String modelId;

  private Double actionValue;

  private String vehicleBomDescription;

  private String connectVehicleId;

  @JsonProperty("pimId")
  private String sagsysId;

  private DisplayedPriceDto displayedPrice;

  private String basketItemSourceId;

  private String basketItemSourceDesc;

  //@formatter:off
  public OfferPosition toOfferPosition() {
    return OfferPosition.builder()
        .id(this.offerPositionId)
        .type(this.type.getValue())
        .umsartId(this.umsartId)
        .shopArticleId(!StringUtils.isBlank(shopArticleId) ? Long.parseLong(shopArticleId) : NumberUtils.LONG_ZERO)
        .articleNumber(this.articleNumber)
        .articleDescription(this.articleDescription)
        .vehicleId(this.vehicleId)
        .vehicleDescription(this.vehicleDescription)
        .sequence(!Objects.isNull(this.sequence) ? this.sequence : NumberUtils.INTEGER_ZERO)
        .context(this.context)
        .quantity(this.quantity)
        .uomIso(this.uomIso)
        .grossPrice(this.grossPrice)
        .netPrice(this.netPrice)
        .totalGrossPrice(this.totalGrossPrice)
        .remark(this.remark)
        .actionType(
            Objects.isNull(this.actionType) ? OfferActionType.NONE.name() : this.actionType.name())
        .actionValue(this.actionValue)
        .deliveryTypeId(this.deliveryTypeId)
        .makeId(this.makeId)
        .modelId(this.modelId)
        .vehicleBomId(this.vehicleId)
        .vehicleBomDescription(this.vehicleBomDescription)
        .connectVehicleId(this.connectVehicleId)
        .sagsysId(this.sagsysId)
        .basketItemSourceId(this.basketItemSourceId)
        .basketItemSourceDesc(this.basketItemSourceDesc)
        .displayedPriceType(Optional.ofNullable(displayedPrice).map(DisplayedPriceDto::getType).orElse(null))
        .displayedPriceBrand(Optional.ofNullable(displayedPrice).map(DisplayedPriceDto::getBrand).orElse(null))
        .displayedPriceBrandId(Optional.ofNullable(displayedPrice).map(DisplayedPriceDto::getBrandId).orElse(null))
        .build();
  }
  //@formatter:on

  public OfferPositionItemRequestBody(final OfferPositionDto offerPositionDto) {
    this.offerPositionId = offerPositionDto.getId();
    this.type = OfferPositionType.valueOf(offerPositionDto.getType());
    this.umsartId = offerPositionDto.getUmsartId();
    this.shopArticleId = offerPositionDto.getShopArticleId();
    this.articleNumber = offerPositionDto.getArticleNumber();
    this.articleDescription = offerPositionDto.getArticleDescription();
    this.vehicleId = offerPositionDto.getVehicleId();
    this.vehicleDescription = offerPositionDto.getVehicleDescription();
    this.sequence = offerPositionDto.getSequence();
    this.context = offerPositionDto.getContext();
    this.quantity = offerPositionDto.getQuantity();
    this.uomIso = offerPositionDto.getUomIso();
    this.grossPrice = offerPositionDto.getGrossPrice();
    this.netPrice = offerPositionDto.getNetPrice();
    this.totalGrossPrice = offerPositionDto.getTotalGrossPrice();
    this.remark = offerPositionDto.getRemark();
    this.actionType = OfferActionType.valueOf(offerPositionDto.getActionType());
    this.actionValue = offerPositionDto.getActionValue();
    this.deliveryTypeId = offerPositionDto.getDeliveryTypeId();
    this.makeId = offerPositionDto.getMakeId();
    this.modelId = offerPositionDto.getModelId();
    this.vehicleBomDescription = offerPositionDto.getVehicleBomDescription();
    this.connectVehicleId = offerPositionDto.getConnectVehicleId();
    this.sagsysId = offerPositionDto.getSagsysId();
    this.basketItemSourceId = offerPositionDto.getBasketItemSourceId();
    this.basketItemSourceDesc = offerPositionDto.getBasketItemSourceDesc();
  }

  public OfferPositionDto toOfferPositionDto() {
    return OfferPositionDto.builder().id(this.offerPositionId).type(this.type.name())
        .umsartId(this.umsartId).shopArticleId(this.shopArticleId).articleNumber(this.articleNumber)
        .articleDescription(this.articleDescription).vehicleId(this.vehicleId)
        .vehicleDescription(this.vehicleDescription).sequence(this.sequence).context(this.context)
        .quantity(this.quantity).uomIso(this.uomIso)
        .grossPrice(
            Optional.ofNullable(displayedPrice).map(DisplayedPriceDto::getPrice).orElse(grossPrice))
        .netPrice(this.netPrice)
        .totalGrossPrice(Optional.ofNullable(displayedPrice)
            .map(price -> price.reCalculateTotalPrice(quantity)).orElse(totalGrossPrice))
        .remark(this.remark)
        .actionType(
            Objects.isNull(this.actionType) ? OfferActionType.NONE.name() : this.actionType.name())
        .actionValue(this.actionValue).deliveryTypeId(this.deliveryTypeId).makeId(this.makeId)
        .modelId(this.modelId).vehicleBomDescription(this.vehicleBomDescription)
        .connectVehicleId(this.connectVehicleId).sagsysId(this.sagsysId)
        .basketItemSourceId(this.basketItemSourceId)
        .basketItemSourceDesc(this.basketItemSourceDesc)
        .displayedPrice(this.getDisplayedPrice())
        .build();
  }

  public boolean isNew() {
    return Objects.isNull(offerPositionId);
  }

  @JsonIgnore
  public String getVehicleId() {
    return StringUtils.defaultIfBlank(getConnectVehicleId(), StringUtils.EMPTY);
  }

  @JsonIgnore
  public boolean hasDisplayedPriceBrand() {
    return Objects.nonNull(displayedPrice);
  }

  @JsonIgnore
  public boolean hasSameDisplayedPriceBrand(DisplayedPriceDto displayedPrice) {
    return hasDisplayedPriceBrand() && Objects.nonNull(displayedPrice)
        && this.displayedPrice.getBrand().equals(displayedPrice.getBrand())
        && this.displayedPrice.getBrandId().equals(displayedPrice.getBrandId());
  }
}
