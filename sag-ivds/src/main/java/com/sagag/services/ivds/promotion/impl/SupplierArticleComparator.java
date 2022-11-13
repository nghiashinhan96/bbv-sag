package com.sagag.services.ivds.promotion.impl;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.promotion.ArticleComparator;

import org.apache.commons.lang3.StringUtils;

public class SupplierArticleComparator implements ArticleComparator {

  public static ArticleComparator getInstance() {
    return new SupplierArticleComparator();
  }

  @Override
  public int compare(ArticleDocDto article1, ArticleDocDto article2) {
    return defaultSupplier(article1).compareTo(defaultSupplier(article2));
  }

  private static String defaultSupplier(ArticleDocDto article) {
    return StringUtils.lowerCase(StringUtils.defaultString(article.getSupplier()));
  }

}
