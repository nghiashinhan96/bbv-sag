package com.sagag.services.service.exporter.wss;

import com.sagag.eshop.service.dto.order.FinalCustomerOrderItemDto;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagPriceUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WssFinalCustomerOrderExportItemDto {
  private static final String PFLAND_ARTICLE_PREFIX = "PFA";
  private static final String VRG_ARTICLE_PREFIX = "VRG";
  private static final String VOC_ARTICLE_PREFIX = "VOC";
  private static final String DEPOT_ARTICLE_PREFIX = "DEP";
  private String articleDesc;
  private String itemDesc;
  private Integer quantity;
  private Double grossPrice;
  private Double finalCustomerNetPrice;
  private boolean isDisplayPriceWithVat;

  private Locale locale;

  private Double grossPriceWithVat;

  private Double finalCustomerNetPriceWithVat;

  private List<WssFinalCustomerOrderExportItemDto> attachedItems;

  public WssFinalCustomerOrderExportItemDto(FinalCustomerOrderItemDto finalCustomerOrderItem,
      boolean isDisplayPriceWithVat, Locale locale) {
    this.articleDesc = finalCustomerOrderItem.getArticleDesc();
    this.itemDesc = finalCustomerOrderItem.getGenArtDescription();
    this.quantity = finalCustomerOrderItem.getQuantity();
    this.grossPrice = finalCustomerOrderItem.getGrossPrice();
    this.finalCustomerNetPrice = finalCustomerOrderItem.getFinalCustomerNetPrice();
    this.isDisplayPriceWithVat = isDisplayPriceWithVat;
    this.locale = locale;
    this.grossPriceWithVat = finalCustomerOrderItem.getGrossPriceWithVat();
    this.finalCustomerNetPriceWithVat = finalCustomerOrderItem.getFinalCustomerNetPriceWithVat();
    this.attachedItems = CollectionUtils.emptyIfNull(finalCustomerOrderItem.getAttachedItems())
        .stream().map(item -> new WssFinalCustomerOrderExportItemDto(item, isDisplayPriceWithVat,
            locale, articleDesc))
        .collect(Collectors.toList());
  }

  public WssFinalCustomerOrderExportItemDto(FinalCustomerOrderItemDto finalCustomerOrderItem,
      boolean isDisplayPriceWithVat, Locale locale, String parentArticleNr) {
    this(finalCustomerOrderItem, isDisplayPriceWithVat, locale);
    StringBuilder articleDescBuilder = new StringBuilder();
    if (finalCustomerOrderItem.isDepot()) {
      articleDescBuilder.append(DEPOT_ARTICLE_PREFIX);
    }
    if (finalCustomerOrderItem.isVoc()) {
      articleDescBuilder.append(VOC_ARTICLE_PREFIX);
    }
    if (finalCustomerOrderItem.isVrg()) {
      articleDescBuilder.append(VRG_ARTICLE_PREFIX);
    }
    if (finalCustomerOrderItem.isPfand()) {
      articleDescBuilder.append(PFLAND_ARTICLE_PREFIX);
    }
    articleDescBuilder.append(SagConstants.SPACE).append(SagConstants.HYPHEN)
        .append(SagConstants.SPACE).append(parentArticleNr);
    this.articleDesc = articleDescBuilder.toString();
  }

  public String getDisplayGrossPrice() {
    return getDisplayPrice(grossPrice, grossPriceWithVat);
  }

  public String getDisplayFinalCustomerNetPrice() {
    return getDisplayPrice(finalCustomerNetPrice, finalCustomerNetPriceWithVat);
  }

  public String getDisplayTotalPrice() {
    Double totalPrice;
    Double totalPriceWithVat;
    if (Objects.isNull(finalCustomerNetPrice)) {
      totalPrice = grossPrice;
      totalPriceWithVat = grossPriceWithVat;
    } else {
      totalPrice = finalCustomerNetPrice;
      totalPriceWithVat = finalCustomerNetPriceWithVat;
    }
    totalPrice = Objects.isNull(totalPrice) ? null : totalPrice * quantity;
    totalPriceWithVat = Objects.isNull(totalPriceWithVat) ? null : totalPriceWithVat * quantity;
    return getDisplayPrice(totalPrice, totalPriceWithVat);
  }

  private String getDisplayPrice(Double price, Double priceWithVat) {
    if (Objects.isNull(price)) {
      return StringUtils.EMPTY;
    }
    StringBuilder displayPriceBuilder = new StringBuilder(
        Objects.toString(SagPriceUtils.roundHalfEvenTo2digits(price), StringUtils.EMPTY));
    if (isDisplayPriceWithVat && Objects.nonNull(priceWithVat)) {
      displayPriceBuilder.append(SagConstants.SPACE).append(SagConstants.OPEN_BRACKET)
          .append(SagPriceUtils.roundHalfEvenTo2digits(priceWithVat))
          .append(SagConstants.CLOSE_BRACKET);
    }
    return displayPriceBuilder.toString();
  }
}
