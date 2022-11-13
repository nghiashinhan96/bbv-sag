package com.sagag.services.service.mail.orderconfirmation;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

@Component
public class OrderConfirmationMailSenderCustomByAffDataBuilderFactory {

  @Autowired
  private DefaultOrderConfirmationMailSenderByAffDataBuilder defaultOrderConfirmDataBuilder;

  @Autowired(required = false)
  private CzOrderConfirmationMailSenderByAffDataBuilder czOrderConfirmDataBuilder;

  /**
   * Returns displayed arrival time in order confirmation mail.
   *
   * @param cartItem
   * @param affiliate
   * @param timeZone
   * @param locale
   * @param isFinalUserRole
   * @return the result of arrival time
   */
  public String getDisplayedArrivalTime(ShoppingCartItem cartItem, TimeZone timeZone,
      Locale locale, boolean isFinalUserRole) {
    if (cartItem.isVin()) {
      return StringUtils.EMPTY;
    }

    final OrderConfirmationMailSenderCustomByAffDataBuilder dataBuilder =
        findOrderConfirmDataBuilder(isFinalUserRole);
    final ArticleDocDto artDoc = cartItem.getArticle();
    final Availability availability = dataBuilder.findShowingAvail(artDoc);
    if (availability == null) {
      return StringUtils.EMPTY;
    }
    return dataBuilder.getDisplayedDeliveryTime(availability, timeZone, locale);
  }

  private OrderConfirmationMailSenderCustomByAffDataBuilder findOrderConfirmDataBuilder(
      boolean isFinalUserRole) {
    if (czOrderConfirmDataBuilder != null && !isFinalUserRole) {
      return czOrderConfirmDataBuilder;
    }
    return defaultOrderConfirmDataBuilder;
  }

  /**
   * Returns displayed price type in order confirmation mail.
   *
   * @param cartItem
   * @param isShowPriceType
   * @return the result of price type
   */
  public String getDisplayedPriceType(ShoppingCartItem cartItem, boolean isShowPriceType,
      SupportedAffiliate affiliate) {
    if (!isShowPriceType) {
      return StringUtils.EMPTY;
    }

    if (Objects.nonNull(cartItem.getArticleItem().getDisplayedPrice())
        && StringUtils.isNotEmpty(cartItem.getArticleItem().getDisplayedPrice().getType())) {
      DisplayedPriceDto displayedPriceDto = cartItem.getArticleItem().getDisplayedPrice();

      return displayedPriceDto.getType() + SagConstants.SPACE + displayedPriceDto.getBrand();
    }

    if (Objects.nonNull(cartItem.getArticleItem().getPrice())
        && Objects.nonNull(cartItem.getArticleItem().getPrice().getPrice())
        && Objects.nonNull(cartItem.getArticleItem().getPrice().getPrice().getType())
        && !cartItem.getArticleItem().getPrice().getPrice().getType()
          .equals(SagPriceUtils.GROSS_PRICE_TYPE)
        && !affiliate.isSagCzAffiliate()) {

      return cartItem.getArticleItem().getPrice().getPrice().getType();
    }
    return StringUtils.EMPTY;
  }

}
