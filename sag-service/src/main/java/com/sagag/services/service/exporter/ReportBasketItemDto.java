package com.sagag.services.service.exporter;

import com.sagag.services.common.exporter.ExportConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
public class ReportBasketItemDto implements Serializable {

  private static final long serialVersionUID = 7611821393751624363L;

  private String articleNumber;

  private String articleDescription;

  private BigInteger amount;

  private String netPrice;

  private String totalNetPrice;

  private String totalGrossPrice;

  private List<ReportPriceItemDto> priceItems;

  public ReportBasketItemDto(final ShoppingCartItem cartItem, NumberFormat numberFormat) {
    final ArticleDocDto articleItem = cartItem.getArticleItem();

    this.articleNumber =
        StringUtils.defaultIfBlank(articleItem.getArtnrDisplay(), StringUtils.EMPTY);

    this.articleDescription =
        StringUtils.defaultIfBlank(articleItem.getFreetextDisplayDesc(),StringUtils.defaultString(cartItem.getItemDesc()));

    this.amount = BigInteger.valueOf(articleItem.getAmountNumber());

    if (!articleItem.hasPrice()) {
      return;
    }

    if (isPositiveNum(cartItem.getNetPrice())) {
      this.netPrice = numberFormat.format(BigDecimal.valueOf(cartItem.getNetPrice()));
    }

    if (isPositiveNum(cartItem.getTotalNetPrice())) {
      this.totalNetPrice = numberFormat.format(BigDecimal.valueOf(cartItem.getTotalNetPrice()));
    }

    if (isPositiveNum(cartItem.getTotalGrossPrice())) {
      this.totalGrossPrice = numberFormat.format(BigDecimal.valueOf(cartItem.getTotalGrossPrice()));
    }

    this.priceItems = new ArrayList<>();

    if (cartItem.isDepot() || cartItem.isPfand() || cartItem.isRecycle() || cartItem.isVoc()
        || cartItem.isVrg() || StringUtils.isBlank(cartItem.getArticleItem().getArtnr())) {
      return;
    }
    buildGrossPriceReportItem(cartItem, numberFormat).ifPresent(this.priceItems::add);
  }

  private Optional<ReportPriceItemDto> buildGrossPriceReportItem(ShoppingCartItem cartItem,
      NumberFormat numberFormat) {
    final ArticleDocDto articleItem = cartItem.getArticleItem();
    String label;
    String brand = StringUtils.EMPTY;
    if (Objects.nonNull(articleItem.getDisplayedPrice())
        && isPositiveNum(articleItem.getDisplayedPrice().getPrice())) {
      label = articleItem.getDisplayedPrice().getType().toLowerCase();
      brand = articleItem.getDisplayedPrice().getBrand();
    } else if (Objects.nonNull(articleItem.getPrice().getPrice().getType())
        && !articleItem.getPrice().getPrice().getType().equals(ExportConstants.GROSS)) {
      label = articleItem.getPrice().getPrice().getType().toLowerCase();
    } else {
      return Optional.empty();
    }

    final BigDecimal grossPrice = BigDecimal.valueOf(cartItem.getGrossPrice());
    return Optional.of(ReportPriceItemDto.builder().label(label)
        .price(numberFormat.format(grossPrice)).brand(brand).build());
  }

  private boolean isPositiveNum(final Double num) {
    return !Objects.isNull(num) && num > 0;
  }

  public boolean hasPrice() {
    return !Objects.isNull(this.netPrice) && !Objects.isNull(this.totalNetPrice);
  }
}
