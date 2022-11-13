package com.sagag.services.ax.utils;

import com.sagag.services.common.enums.PriceEnum;
import com.sagag.services.common.utils.SagPriceUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import java.util.Objects;
import java.util.function.DoubleFunction;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Ax price Utilities.
 *
 */
@UtilityClass
public final class AxPriceUtils {

  public static void updateVatRatePrice(PriceWithArticle price, Double vat) {
    PriceWithArticlePrice articlePrice = price.getPrice();
    price.getPrice().setVatInPercent(vat);

    articlePrice.setNet1PriceWithVat(calculatePriceValueWithVat(articlePrice.getNet1Price(), vat));
    articlePrice.setTotalNet1PriceWithVat(
        calculatePriceValueWithVat(articlePrice.getTotalNet1Price(), vat));
    articlePrice.setStandardGrossPriceWithVat(
        calculatePriceValueWithVat(articlePrice.getStandardGrossPrice(), vat));

    articlePrice.setNetPriceWithVat(calculatePriceValueWithVat(articlePrice.getNetPrice(), vat));
    articlePrice
        .setTotalNetPriceWithVat(calculatePriceValueWithVat(articlePrice.getTotalNetPrice(), vat));

    articlePrice
        .setGrossPriceWithVat(calculatePriceValueWithVat(articlePrice.getGrossPrice(), vat));
    articlePrice.setTotalGrossPriceWithVat(
        calculatePriceValueWithVat(articlePrice.getTotalGrossPrice(), vat));

    articlePrice.setUvpePriceWithVat(calculatePriceValueWithVat(articlePrice.getUvpePrice(), vat));
    articlePrice.setOepPriceWithVat(calculatePriceValueWithVat(articlePrice.getOepPrice(), vat));
    articlePrice.setKbPriceWithVat(calculatePriceValueWithVat(articlePrice.getKbPrice(), vat));
    articlePrice.setUvpPriceWithVat(calculatePriceValueWithVat(articlePrice.getUvpPrice(), vat));
    articlePrice.setDpcPriceWithVat(calculatePriceValueWithVat(articlePrice.getDpcPrice(), vat));
  }

  public static void updateDisplayedPrice(ArticleDocDto article, PriceWithArticle price) {
    DisplayedPriceDto displayedPrice = article.getDisplayedPrice();
    if (Objects.nonNull(displayedPrice) && Objects.nonNull(displayedPrice.getBrandId())) {
      displayedPrice.setPrice(price.getOepPrice());
    }
  }

  public static boolean isValidPrice(PriceWithArticle price) {
    return Objects.nonNull(price) && Objects.nonNull(price.getArticleId());
  }

  public static double defaultPriceValue(Double priceValue) {
    return priceValue != null ? priceValue : NumberUtils.DOUBLE_ZERO;
  }

  public static double calculatePriceValueWithVat(Double priceValue, double vatRate) {
    return priceValue != null ? SagPriceUtils.calculateVATPrice(priceValue, vatRate)
        : NumberUtils.DOUBLE_ZERO;
  }

  public static DisplayedPriceDto calculateOepDisplayedPrice(PriceWithArticle price, double vatRate,
      Long brandId, String brand, int amountNumber) {
    final Double oepPrice = price.getOepPrice();
    final double oepPriceWithVat = AxPriceUtils.calculatePriceValueWithVat(oepPrice, vatRate);

    final DisplayedPriceDto oepPriceDto = DisplayedPriceDto.builder().brand(brand).brandId(brandId)
        .type(PriceEnum.OEP.toString()).price(oepPrice).priceWithVat(oepPriceWithVat).build();

    final DoubleFunction<Double> totalPriceCalculator = val -> val * amountNumber;
    oepPriceDto.setTotalPrice(totalPriceCalculator.apply(
      oepPrice != null ? oepPrice.doubleValue() : 0));
    oepPriceDto.setTotalPriceWithVat(totalPriceCalculator.apply(oepPriceWithVat));
    return oepPriceDto;
  }

  public static Double defaultVatRate(ArticleDocDto article, double defVatRate) {
    final double artVatRate =
        article != null && article.getVatRate() != null ? article.getVatRate().doubleValue()
            : NumberUtils.DOUBLE_MINUS_ONE.doubleValue();
    return artVatRate != NumberUtils.DOUBLE_MINUS_ONE.doubleValue() ? artVatRate : defVatRate;
  }
}
