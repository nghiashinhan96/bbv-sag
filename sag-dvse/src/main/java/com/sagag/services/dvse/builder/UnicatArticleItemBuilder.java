package com.sagag.services.dvse.builder;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.dvse.enums.UnicatAvailabilityState;
import com.sagag.services.dvse.wsdl.unicat.ArrayOfPrice;
import com.sagag.services.dvse.wsdl.unicat.AvailableState;
import com.sagag.services.dvse.wsdl.unicat.Item;
import com.sagag.services.dvse.wsdl.unicat.Price;
import com.sagag.services.stakis.erp.converter.impl.article.TmPriceConverterV2;

/**
 * Builder class to build article item response for UNICAT request.
 */
@Component
@CzProfile
public class UnicatArticleItemBuilder {

  private static final Double PRICE_VALUE_IN_CASE_ERROR = 0.0;

  public Item buildArticeItem(final Optional<ArticleDocDto> articleOpt,
      final SupportedAffiliate affiliate, final NextWorkingDates nextWorkingDate) {
    final Item newItem = new Item();
    ArticleDocDto article = articleOpt.orElse(null);
    newItem.setPrices(buildArrayOfPrice(articleOpt));

    newItem.setAvailState(
        buildAvailableState(Optional.ofNullable(article), affiliate, nextWorkingDate));
    if (!Objects.isNull(article)) {
      newItem.setArticleId(article.getArtid());
    }
    return newItem;
  }

  public Item buildEmptyArticeItem(String articleId, final SupportedAffiliate affiliate,
      final NextWorkingDates nextWorkingDate) {
    final Item newItem = buildArticeItem(Optional.empty(), affiliate, nextWorkingDate);

    newItem.setArticleId(articleId);
    return newItem;
  }

  private ArrayOfPrice buildArrayOfPrice(Optional<ArticleDocDto> updatedPriceArticles) {
    final List<Price> prices = new LinkedList<>();
    if (!updatedPriceArticles.isPresent()) {
      prices.add(
          createPriceItem(null, TmPriceConverterV2.NET_PRICE, PRICE_VALUE_IN_CASE_ERROR, false));
      prices.add(
          createPriceItem(null, TmPriceConverterV2.GROSS_PRICE, PRICE_VALUE_IN_CASE_ERROR, false));
    } else {

      final ArticleDocDto article = updatedPriceArticles.get();

      Optional<PriceWithArticle> priceOpt = Optional.ofNullable(article.getPrice());
      if (priceOpt.isPresent()) {
        PriceWithArticlePrice price = Optional.ofNullable(priceOpt.get().getPrice()).orElse(null);

        if (Objects.nonNull(price)) {
          final String currency = price.getCurrency();
          prices.add(
              createPriceItem(currency, TmPriceConverterV2.NET_PRICE, price.getNetPrice(), false));
          prices.add(createPriceItem(currency, TmPriceConverterV2.GROSS_PRICE,
              price.getGrossPrice(), false));
        }
      }
    }
    final ArrayOfPrice priceArray = new ArrayOfPrice();
    priceArray.getPrice().addAll(prices);

    return priceArray;
  }

  public AvailableState buildAvailableState(Optional<ArticleDocDto> articleOpt,
      final SupportedAffiliate affiliate, NextWorkingDates nextWorkingDate) {
    final AvailableState availState = new AvailableState();
    if (!articleOpt.isPresent()) {
      availState.setAvailState(UnicatAvailabilityState.NOT_AVAILABLE.getCode());
    } else {
      articleOpt.filter(article -> !CollectionUtils.isEmpty(article.getAvailabilities()))
          .map(article -> article.getAvailabilities())
          .ifPresent(availList -> availList.stream().findFirst()
              .ifPresent(avail -> availState.setAvailState(UnicatAvailabilityState
                  .fromStakisAvailStateCode(avail.getAvailState()).getCode())));
    }

    return availState;
  }

  private static Price createPriceItem(final String currency, final int priceType,
      final Double value, boolean taxIncluded) {
    Price priceItem = new Price();
    priceItem.setValue(BigDecimal.valueOf(value));
    priceItem.setPriceType(String.valueOf(priceType));
    priceItem.setCurrencyCode(StringUtils.defaultString(currency));
    priceItem.setTaxIncluded(taxIncluded);
    return priceItem;
  }

}
