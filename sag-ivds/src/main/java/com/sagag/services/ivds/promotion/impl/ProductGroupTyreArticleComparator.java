package com.sagag.services.ivds.promotion.impl;

import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.promotion.ArticleComparator;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;
import java.util.function.Function;

public class ProductGroupTyreArticleComparator implements ArticleComparator {

  private ArticleComparator supplierArticleComparator = SupplierArticleComparator.getInstance();

  public static ArticleComparator getInstance() {
    return new ProductGroupTyreArticleComparator();
  }

  @Override
  public int compare(ArticleDocDto article1, ArticleDocDto article2) {
    return Comparator.comparing(Function.identity(), productGroupComparator())
        .thenComparing(supplierArticleComparator).compare(article1, article2);
  }

  private static Comparator<ArticleDocDto> productGroupComparator() {
    return (art1, art2) -> {
      boolean article1ContainProductGroupP =
          equalProductGroup(TyreConstants.PRIORITY_PRODUCT_GROUP_P, art1);
      boolean article1ContainProductGroupR =
          equalProductGroup(TyreConstants.PRIORITY_PRODUCT_GROUP_R, art1);
      boolean article1ContainProductGroupI =
          equalProductGroup(TyreConstants.PRIORITY_PRODUCT_GROUP_I, art1);

      boolean article2ContainProductGroupP =
          equalProductGroup(TyreConstants.PRIORITY_PRODUCT_GROUP_P, art2);
      boolean article2ContainProductGroupR =
          equalProductGroup(TyreConstants.PRIORITY_PRODUCT_GROUP_R, art2);
      boolean article2ContainProductGroupI =
          equalProductGroup(TyreConstants.PRIORITY_PRODUCT_GROUP_I, art2);

      // #1137: Keep this order to get sag_product_group is R first after I group
      if (!article1ContainProductGroupP
          && !article2ContainProductGroupP
          && !article1ContainProductGroupR
          && !article2ContainProductGroupR
          && !article1ContainProductGroupI
          && !article2ContainProductGroupI) {
        return 0;
      }
      int productGroupP = BooleanUtils.compare(
          article2ContainProductGroupP, article1ContainProductGroupP);
      int productGroupR = BooleanUtils.compare(
          article2ContainProductGroupR, article1ContainProductGroupR);
      int productGroupI = BooleanUtils.compare(
          article2ContainProductGroupI, article1ContainProductGroupI);
      return NumberUtils.max(productGroupP, productGroupR, productGroupI);
    };
  }

  private static boolean equalProductGroup(String productGroup, ArticleDocDto article) {
    return StringUtils.equals(productGroup, article.getSagProductGroup());
  }
}
