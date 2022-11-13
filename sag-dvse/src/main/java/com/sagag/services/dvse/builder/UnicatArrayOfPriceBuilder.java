package com.sagag.services.dvse.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.dvse.enums.DvsePriceLabels;
import com.sagag.services.dvse.enums.DvsePriceType;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfPrice;
import com.sagag.services.dvse.wsdl.dvse.Price;

/**
 * Implementation class of DVSE array of price builder.
 *
 */
@Component
@CzProfile
public class UnicatArrayOfPriceBuilder implements IArrayOfPriceBuilder {

  @Override
  public ArrayOfPrice buildArrayOfPrice(Optional<ArticleDocDto> articleOpt) {
    if (!articleOpt.isPresent()) {
      return new ArrayOfPrice();
    }

    final List<Price> prices = new ArrayList<>();
    final ArticleDocDto article = articleOpt.get();

    article.getNetPrice()
        .map(price -> createPriceItem(price, DvsePriceType.NETTO, DvsePriceLabels.NETTO_LABEL_DE))
        .ifPresent(prices::add);

    article.getGrossPriceForDvse()
        .map(price -> createPriceItem(price, DvsePriceType.BRUTTO, DvsePriceLabels.BRUTTO_LABEL_DE))
        .ifPresent(prices::add);

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
