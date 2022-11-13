package com.sagag.services.dvse.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.sagag.services.article.api.utils.DefaultAmountHelper;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.dvse.wsdl.dvse.Item;
import com.sagag.services.dvse.wsdl.dvse.TecDocType;

import lombok.experimental.UtilityClass;

/**
 * Utilities for DVSE components.
 */
@UtilityClass
public class DvseUtils {

  public static Map<Item, ArticleDocDto> convertItemsToArticles(final List<Item> items) {
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyMap();
    }
    return items.stream()
            .collect(Collectors.toMap(item -> item, articleConverter()));
  }

  public static Function<Item, ArticleDocDto> articleConverter() {
    return item -> {
      final ArticleDocDto article = new ArticleDocDto();
      article.setIdSagsys(item.getWholesalerArticleNumber());
      if (Objects.nonNull(item.getGenericArticles())
        && CollectionUtils.isNotEmpty(item.getGenericArticles().getGenericArticle())) {

        article.setGaId(String.valueOf(
          item.getGenericArticles().getGenericArticle().get(0).getGenericArticleId()));
      }

      if (Objects.nonNull(item.getRequestedQuantity())
        && Objects.nonNull(item.getRequestedQuantity().getValue())) {
        article.setAmountNumber(item.getRequestedQuantity().getValue().intValue());
      } else {
        article.setAmountNumber(DefaultAmountHelper.getArticleSalesQuantity(article.getGaId(),
          Optional.empty())); // Set default value
      }
      article.setSupplierId(item.getSupplierId());
      article.setSupplier(item.getSupplierName());
      article.setSupplierArticleNumber(item.getSupplierArticleNumber());
      // for non ref , they need to show article number which map to xml SupplierArticleNumber
      // #2434 finding 2
      article.setArtnrDisplay(item.getSupplierArticleNumber());
      return article;
    };
  }

  public static Optional<Integer> getKtypeNr(List<Item> items) {
    if (CollectionUtils.isEmpty(items)) {
      return Optional.empty();
    }
    return getTecDocTypeFromItem(items.get(0))
      .map(TecDocType::getTecDocTypeId);
  }

  private static Optional<TecDocType> getTecDocTypeFromItem(Item item) {
    if (Objects.isNull(item.getTecDocTypes())
      || CollectionUtils.isEmpty(item.getTecDocTypes().getTecDocType())) {
      return Optional.empty();
    }
    return item.getTecDocTypes().getTecDocType().stream().findFirst();
  }

  public static Map<String, Optional<Integer>> getPimIdAndQuantities(List<Item> items) {
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyMap();
    }
    final Map<String, Optional<Integer>> quantities = new HashMap<>();
    String idPim;
    for (Item item : items) {
      idPim = item.getWholesalerArticleNumber();
      if (NumberUtils.isDigits(idPim)) {
        quantities.putIfAbsent(idPim, findRequestedQuantity(item));
      }
    }
    return quantities;
  }

  private static Optional<Integer> findRequestedQuantity(Item item) {
    if (item == null || item.getRequestedQuantity() == null
      || item.getRequestedQuantity().getValue() == null) {
      return Optional.empty();
    }
    return Optional.of(item.getRequestedQuantity().getValue().intValue());
  }

  public static Function<Item, ArticleDocDto> nonReferenceArticleConverter() {
    return item -> {
      ArticleDocDto article = new ArticleDocDto();
      article.setId(item.getWholesalerArticleNumber());
      article.setIdSagsys(item.getWholesalerArticleNumber());
      article.setIdUmsart(item.getWholesalerArticleNumber());
      article.setSupplierId(item.getSupplierId());
      article.setSupplierArticleNumber(item.getSupplierArticleNumber());
      article.setArtnrDisplay(item.getSupplierArticleNumber());
      article.setSupplier(item.getSupplierName());

      if (Objects.nonNull(item.getRequestedQuantity())
        && Objects.nonNull(item.getRequestedQuantity().getValue())) {
        article.setAmountNumber(item.getRequestedQuantity().getValue().intValue());
      }
      return article;
    };
  }
}
