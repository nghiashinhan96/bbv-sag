package com.sagag.services.hazelcast.domain.cart;

import com.sagag.services.common.contants.SagConstants;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class AttachedCartItemHelper {

  public static double getAttachedArticleVatTotalOnGross(List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleVatTotalOnGross).sum();
  }

  public static double getAttachedArticleVatTotalOnNet(List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleVatTotalOnNet).sum();
  }
  
  public static double getAttachedArticleVatTotalOnNet1(List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleVatTotalOnNet1).sum();
  }

  public static double getAttachedArticleTotalNetPriceInclVat(
      List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleTotalNetPriceInclVat).sum();
  }

  public static double getAttachedArticleTotalNetPrice(List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleTotalNetPrice).sum();
  }

  public static double getAttachedArticleTotalNet1Price(List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleTotalNet1Price).sum();
  }

  public static double getAttachedArticleTotalGrossPriceInclVat(
      List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleTotalGrossPriceInclVat).sum();
  }

  public static double getAttachedArticleTotalGrossPrice(List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleTotalGrossPrice).sum();
  }

  public static double getAttachedArticleTotalFinalCustomerNetPrice(
      List<ShoppingCartItem> attachedCartItems) {
    if (CollectionUtils.isEmpty(attachedCartItems)) {
      return NumberUtils.DOUBLE_ZERO;
    }
    return attachedCartItems.stream()
        .mapToDouble(AttachedCartItemHelper::getAttachedArticleTotalFinalCustomerNetPrice).sum();
  }

  private static double getAttachedArticleTotalGrossPrice(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalGrossPrice())) {
      return attachedArticle.getPriceItem().getTotalGrossPrice();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

  private static double getAttachedArticleTotalGrossPriceInclVat(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getVatInPercent())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalGrossPrice())) {
      return (1 + SagConstants.PERCENT * attachedArticle.getPriceItem().getVatInPercent())
          * attachedArticle.getPriceItem().getTotalGrossPrice();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

  private static double getAttachedArticleTotalFinalCustomerNetPrice(
      ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle)
        && Objects.nonNull(attachedArticle.getFinalCustomerNetPrice())) {
      return attachedArticle.getTotalFinalCustomerNetPrice();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

  private static double getAttachedArticleTotalNetPrice(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalNetPrice())) {
      return attachedArticle.getPriceItem().getTotalNetPrice();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

  private static double getAttachedArticleTotalNet1Price(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalNet1Price())) {
      return attachedArticle.getPriceItem().getTotalNet1Price();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

  private static double getAttachedArticleTotalNetPriceInclVat(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getVatInPercent())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalNetPrice())) {
      return (1 + SagConstants.PERCENT * attachedArticle.getPriceItem().getVatInPercent())
          * attachedArticle.getPriceItem().getTotalNetPrice();
    }

    return NumberUtils.DOUBLE_ZERO;
  }
  
  private static double getAttachedArticleVatTotalOnNet1(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getVatInPercent())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalNet1Price())) {
      return (SagConstants.PERCENT * attachedArticle.getPriceItem().getVatInPercent())
          * attachedArticle.getPriceItem().getTotalNet1Price();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

  private static double getAttachedArticleVatTotalOnNet(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getVatInPercent())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalNetPrice())) {
      return (SagConstants.PERCENT * attachedArticle.getPriceItem().getVatInPercent())
          * attachedArticle.getPriceItem().getTotalNetPrice();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

  private double getAttachedArticleVatTotalOnGross(ShoppingCartItem attachedArticle) {
    if (Objects.nonNull(attachedArticle) && Objects.nonNull(attachedArticle.getPriceItem())
        && Objects.nonNull(attachedArticle.getPriceItem().getVatInPercent())
        && Objects.nonNull(attachedArticle.getPriceItem().getTotalGrossPrice())) {
      return (SagConstants.PERCENT * attachedArticle.getPriceItem().getVatInPercent())
          * attachedArticle.getPriceItem().getTotalGrossPrice();
    }

    return NumberUtils.DOUBLE_ZERO;
  }

}
