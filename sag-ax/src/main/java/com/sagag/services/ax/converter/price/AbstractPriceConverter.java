package com.sagag.services.ax.converter.price;

import com.sagag.services.ax.domain.AxPriceWithArticle;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.PriceEnum;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public abstract class AbstractPriceConverter implements PriceConverter {

  protected static final double NO_PRICE_VALUE = NumberUtils.DOUBLE_ZERO;

  private static final int NEXT_INDEX = 1;

  public abstract boolean isValidConverter(boolean isFinalCustomerUser);

  public void setNoPriceIfGrossEqualsNetPrice(PriceWithArticle price) {
    if (!price.hasNetPrice()) {
      return;
    }
    // #1673 If [UVPE > OEP] <= netPrice then show no price
    final Double netPrice = price.getPrice().getNetPrice();
    if (Double.compare(price.getGrossPrice(), netPrice) <= 0) {
      price.getPrice().setStandardGrossPrice(NO_PRICE_VALUE);
      price.getPrice().setGrossPrice(NO_PRICE_VALUE);
    }
  }

  public void setNoPriceIfGrossLessThanNetPriceForSubArticle(PriceWithArticle price) {
    if (!price.hasNetPrice()) {
      return;
    }
    // #1673 If [UVPE > OEP] < netPrice then show no price
    final Double netPrice = price.getPrice().getNetPrice();
    if (Double.compare(price.getGrossPrice(), netPrice) < 0) {
      price.getPrice().setStandardGrossPrice(NO_PRICE_VALUE);
      price.getPrice().setGrossPrice(NO_PRICE_VALUE);
    }
  }

  protected static Double calculateTotalGrossPrice(final Double priceValue,
      final Optional<Integer> quantity) {
    if (Objects.isNull(priceValue) || !quantity.isPresent()) {
      return null;
    }
    return priceValue * quantity.get();
  }

  protected void setPriceAndTotalGross(PriceWithArticle price, Double priceValue,
      Optional<Integer> quantity) {
    price.getPrice().setGrossPrice(priceValue);
    price.getPrice().setTotalGrossPrice(calculateTotalGrossPrice(priceValue, quantity));
  }

  protected PriceWithArticle updatePriceByCommonRule(AxPriceWithArticle articlePrice,
      Optional<Integer> quantity, PriceDisplayTypeEnum priceEnum) {
    final PriceWithArticle price = articlePrice.toPriceDto();
    if (Objects.nonNull(price.getPrice())) {
      price.getPrice().setStandardGrossPrice(price.getGrossPrice());
    }
    Map<String, Double> priceMap = getPriceMap(price);
    setGrossPriceByConfiguration(priceEnum.convertToArray(), priceMap, quantity, price);

    return price;
  }

  protected void setGrossPriceByConfiguration(String[] type, Map<String, Double> priceMap,
      Optional<Integer> quantity, PriceWithArticle price) {
    if (ArrayUtils.isEmpty(type)) {
      setPriceAndTotalGross(price, NO_PRICE_VALUE, quantity);
      return;
    }
    final String priceType = type[0];
    final Double priceValue = priceMap.get(priceType);
    final BiPredicate<PriceWithArticle, Double> isEqualOrGreaterThanNetPricePredicate =
      (priceWithArtPrice, priceVal) -> priceVal != null
        && Double.compare(NO_PRICE_VALUE, priceVal) != 0
        && !isSmallerThanNetPrice(priceVal, price.getPrice());

    if (isEqualOrGreaterThanNetPricePredicate.test(price, priceValue)) {
      setPriceAndTotalGross(price, priceValue, quantity);
      price.getPrice().setType(priceType);
      return;
    }

    final String[] remainingPriceTypes = ArrayUtils.subarray(type, NEXT_INDEX, type.length);
    if (ArrayUtils.isEmpty(remainingPriceTypes)
      && !isEqualOrGreaterThanNetPricePredicate.test(price, priceValue)) {
      Optional.ofNullable(price).map(PriceWithArticle::getPrice).ifPresent(artPrice -> {
        artPrice.setUvpePrice(NO_PRICE_VALUE);
        artPrice.setUvpePriceWithVat(NO_PRICE_VALUE);
        artPrice.setOepPrice(NO_PRICE_VALUE);
        artPrice.setOepPriceWithVat(NO_PRICE_VALUE);
        artPrice.setDpcPrice(NO_PRICE_VALUE);
        artPrice.setDpcPriceWithVat(NO_PRICE_VALUE);
      });
    }
    setGrossPriceByConfiguration(remainingPriceTypes, priceMap, quantity, price);
  }

  protected Map<String, Double> getPriceMap(PriceWithArticle price) {
    Map<String, Double> priceMap = new HashMap<>();
    priceMap.put(PriceEnum.UVPE.toString(), price.getUvpePrice());
    priceMap.put(PriceEnum.OEP.toString(), price.getOepPrice());
    priceMap.put(PriceEnum.DPC.toString(), price.getDpcPrice());
    priceMap.put(PriceEnum.GROSS.toString(), price.getGrossPrice());
    return priceMap;
  }

  private boolean isSmallerThanNetPrice(Double priceValue, PriceWithArticlePrice price) {
    if (price == null) {
      return false;
    }

    final double net1Price = price.getNet1Price() != null ?
      SagPriceUtils.roundHalfEventTo2digitsDouble(price.getNet1Price()) : 0;
    final double netPrice = price.getNetPrice() != null ?
      SagPriceUtils.roundHalfEventTo2digitsDouble(price.getNetPrice()) : 0;
    return (Double.compare(NO_PRICE_VALUE, net1Price) != 0
        && Double.compare(priceValue, net1Price) < 0)
        || (Double.compare(NO_PRICE_VALUE, netPrice) != 0
            && Double.compare(priceValue, netPrice) < 0);
  }

}
