package com.sagag.eshop.service.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sagag.eshop.repo.entity.order.FinalCustomerOrderItem;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleImageDto;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinalCustomerOrderItemDto implements Serializable {

  private static final long serialVersionUID = -4464289197775708844L;

  @JsonIgnore
  private String vehicleId;

  private String vehicleDesc;

  private String articleId;

  private String articleDesc;

  private Integer quantity;

  private String genArtDescription;

  private String supplier;

  private String brand;

  private List<ArticleImageDto> images;

  private String reference;

  private Double grossPrice;

  private Double netPrice;

  private Integer salesQuantity;

  private String productAddon;

  private String type;

  private String displayedPriceType;

  private String displayedPriceBrand;

  private String displayedPriceBrandId;

  private List<Availability> availabilities;

  private Double finalCustomerNetPrice;

  private Double grossPriceWithVat;

  private Double finalCustomerNetPriceWithVat;

  private List<FinalCustomerOrderItemDto> attachedItems;

  private boolean recycle;

  private boolean depot;

  private boolean pfand;

  private boolean voc;

  private boolean vrg;

  public FinalCustomerOrderItemDto(FinalCustomerOrderItem finalCustomerOrderItem) {
    vehicleId = StringUtils.defaultString(finalCustomerOrderItem.getVehicleId(),
        SagConstants.KEY_NO_VEHICLE);
    vehicleDesc = finalCustomerOrderItem.getVehicleDesc();
    articleId = finalCustomerOrderItem.getArticleId();
    articleDesc = finalCustomerOrderItem.getArticleDesc();
    quantity = finalCustomerOrderItem.getQuantity();
    genArtDescription = finalCustomerOrderItem.getGenArtDescription();
    supplier = finalCustomerOrderItem.getSupplier();
    brand = finalCustomerOrderItem.getBrand();
    reference = finalCustomerOrderItem.getReference();
    grossPrice = finalCustomerOrderItem.getGrossPrice();
    netPrice = finalCustomerOrderItem.getNetPrice();
    salesQuantity = finalCustomerOrderItem.getSalesQuantity();
    productAddon = finalCustomerOrderItem.getProductAddon();
    type = finalCustomerOrderItem.getType();

    images = SagJSONUtil.convertArrayJsonToList(finalCustomerOrderItem.getImages(), ArticleImageDto.class);

    displayedPriceType = finalCustomerOrderItem.getDisplayedPriceType();
    displayedPriceBrand = finalCustomerOrderItem.getDisplayedPriceBrand();
    displayedPriceBrandId = finalCustomerOrderItem.getDisplayedPriceBrandId();
    availabilities = SagJSONUtil.convertArrayJsonToList(finalCustomerOrderItem.getAvailabilities(), Availability.class);
    finalCustomerNetPrice = finalCustomerOrderItem.getFinalCustomerNetPrice();
    finalCustomerNetPriceWithVat = finalCustomerOrderItem.getFinalCustomerNetPriceWithVat();
    grossPriceWithVat = finalCustomerOrderItem.getGrossPriceWithVat();
    if (StringUtils.isNotBlank(finalCustomerOrderItem.getAttachItems())) {
      attachedItems = SagJSONUtil.convertArrayJsonToList(
              finalCustomerOrderItem.getAttachItems(), FinalCustomerOrderItemDto.class);
    }
  }

  @JsonIgnore
  public boolean hasDisplayedPrice() {
    return StringUtils.isNoneBlank(displayedPriceType)
        && StringUtils.isNoneBlank(displayedPriceBrand) && Objects.nonNull(displayedPriceBrandId);
  }
}
