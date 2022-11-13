package com.sagag.services.dvse.builder;

import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.dvse.enums.DvsePriceLabels;
import com.sagag.services.dvse.enums.DvsePriceType;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfPrice;
import com.sagag.services.dvse.wsdl.dvse.Price;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation class of DVSE array of price builder.
 *
 */
@Component
@AxProfile
public class DvseArrayOfPriceBuilder implements IArrayOfPriceBuilder {

  @Override
  public ArrayOfPrice buildArrayOfPrice(Optional<ArticleDocDto> articleOpt) {
    if (!articleOpt.isPresent()) {
      return new ArrayOfPrice();
    }

    final List<Price> prices = new ArrayList<>();
    final ArticleDocDto article = articleOpt.get();

    // #3087: Customer [AX-AT]: Re-order sent prices and to hide net price in TecCat
    // First: Net Price
    article.getNetPrice()
        .map(price -> createPriceItem(price, DvsePriceType.NETTO, DvsePriceLabels.NETTO_LABEL_DE))
        .ifPresent(prices::add);

    // Second: Gross Price
    article.getGrossPriceForDvse()
        .map(price -> createPriceItem(price, DvsePriceType.BRUTTO, DvsePriceLabels.BRUTTO_LABEL_DE))
        .ifPresent(prices::add);

    // Third: OE Price
    article.getOepPrice()
        .map(price -> createPriceItem(price, DvsePriceType.OEP_PRICE, DvsePriceLabels.OEP_LABEL_DE))
        .ifPresent(prices::add);

    final ArrayOfPrice priceArray = new ArrayOfPrice();
    priceArray.getPrice().addAll(prices);
    return priceArray;
  }

  private static Price createPriceItem(final Double price, final DvsePriceType priceType,
      final String label) {

    Price priceItem = new Price();
    priceItem.setValue(BigDecimal.valueOf(price));
    priceItem.setDescription(StringUtils.defaultIfBlank(label, StringUtils.EMPTY));
    priceItem.setPriceCode(priceType.getCode());

    return priceItem;
  }

}
