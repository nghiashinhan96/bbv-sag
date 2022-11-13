package com.sagag.services.ivds.promotion.impl;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.promotion.ArticleComparator;

import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class BrandPromotionArticleComparator implements ArticleComparator {

  private ArticleComparator productGroupTyreArticleComparator = ProductGroupTyreArticleComparator
      .getInstance();

  private final List<String> promotionBrands;

  public static ArticleComparator getInstance(final List<String> promotionBrands) {
    return new BrandPromotionArticleComparator(promotionBrands);
  }

  @Override
  public int compare(ArticleDocDto article1, ArticleDocDto article2) {
    if (CollectionUtils.isEmpty(promotionBrands)) {
      return productGroupTyreArticleComparator.compare(article1, article2);
    }

    // #5403:
    Comparator<ArticleDocDto> promotionBrandComparator = (a1, a2) -> BooleanUtils.compare(
        filterByPromotionBrands().test(a2), filterByPromotionBrands().test(a1));

    return Comparator.comparing(Function.identity(), promotionBrandComparator)
        .thenComparing(productGroupTyreArticleComparator).compare(article1, article2);
  }

  private Predicate<ArticleDocDto> filterByPromotionBrands() {
    return article -> promotionBrands.contains(StringUtils.upperCase(article.getProductBrand()));
  }
}
