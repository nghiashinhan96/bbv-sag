package com.sagag.services.stakis.erp.converter.impl.article;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.utils.SagPriceUtils;
import static com.sagag.services.common.utils.XmlUtils.getValueOpt;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Price;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@CzProfile
public class TmPriceConverterV2 {

  public static final int GROSS_PRICE = 5;

  public static final int NET_PRICE = 4;

  private static final int DISCOUNT_PRICE = 12;

  public PriceWithArticle apply(List<Price> prices, Integer quantity, double vatRate) {
    if (CollectionUtils.isEmpty(prices)) {
      return PriceWithArticle.empty();
    }

    final int quantityVal = defaultQuantity(quantity);
    final PriceWithArticlePrice artPrice = new PriceWithArticlePrice();

    artPrice.setVatInPercent(vatRate); // Set default VAT for CZ

    final List<Price> grossPrices = findPricesByType(prices, GROSS_PRICE);
    grossPrices.forEach(p -> {
      final double grossPriceUnit = defaultPriceUnit(p.getPriceUnit());
      if (!hasVatPrice(p)) {
        artPrice.setGrossPrice(defaultPrice(p.getValue()));
        artPrice.setTotalGrossPrice(
          calculateTotalPrice(artPrice.getGrossPrice(), quantityVal, grossPriceUnit));
      } else {
        artPrice.setGrossPriceWithVat(	
            SagPriceUtils.calculateVATPrice(artPrice.getGrossPrice(), artPrice.getVatInPercent()));
        artPrice.setTotalGrossPriceWithVat(
                calculateTotalPrice(artPrice.getGrossPriceWithVat(), quantityVal, grossPriceUnit));
      }
    });

    final List<Price> netPrices = findPricesByType(prices, NET_PRICE);
    netPrices.forEach(p -> {
      final double netPriceUnit = defaultPriceUnit(p.getPriceUnit());
      if (!hasVatPrice(p)) {
        artPrice.setNetPrice(defaultPrice(p.getValue()));
        artPrice.setTotalNetPrice(calculateTotalPrice(artPrice.getNetPrice(), quantityVal, netPriceUnit));
      } else {
        artPrice.setNetPriceWithVat(
            SagPriceUtils.calculateVATPrice(artPrice.getNetPrice(), artPrice.getVatInPercent()));
        artPrice.setTotalNetPriceWithVat(
          calculateTotalPrice(artPrice.getNetPriceWithVat(), quantityVal, netPriceUnit));
      }
    });

    final List<Price> discountPrices = findPricesByType(prices, DISCOUNT_PRICE);
    discountPrices.forEach(p -> {
      artPrice.setDiscountPrice(defaultPrice(p.getValue()));
      final double discountPriceUnit = defaultPriceUnit(p.getPriceUnit());
      artPrice.setTotalDiscountPrice(
        calculateTotalPrice(artPrice.getDiscountPrice(), quantityVal, discountPriceUnit));
    });

    prices.stream().findFirst()
    .ifPresent(price -> artPrice.setCurrency(getValueOpt(price.getCurrencyCodeIso4217())
        .orElse(StringUtils.EMPTY)));

    final PriceWithArticle price = new PriceWithArticle();
    price.setPrice(artPrice);
    return price;
  }

  private static List<Price> findPricesByType(List<Price> prices, int type) {
    return prices.stream().filter(p -> p.getType() == type).collect(Collectors.toList());
  }

  private static double defaultPrice(BigDecimal priceVal) {
    return priceVal == null ? 0d : priceVal.doubleValue();
  }

  private static double defaultPriceUnit(BigDecimal priceVal) {
    return priceVal == null ? 1d : priceVal.doubleValue();
  }

  private static boolean hasVatPrice(Price p) {
    return p != null && p.getVAT() != null && p.getVAT().doubleValue() != 0;
  }

  private static int defaultQuantity(Integer quantity) {
    return quantity == null ? 0 : quantity.intValue();
  }

  private static double calculateTotalPrice(Double price, int quantity, double priceUnit) {
    return price * (quantity / priceUnit);
  }
}
