package com.sagag.services.ivds.promotion.impl;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class BrandPromotionArticleComparatorTest {

  @Test
  public void test() {
    List<String> pBrands = Arrays.asList("SEMPERIT", "ENEOS");
    List<ArticleDocDto> articles = Arrays.asList(article("1", "SEMPERIT", "P"),
        article("2", "TESTTTTTTTTT", "R"),
        article("3", "BRIDGESTONE", "P"),
        article("3", "BRIDGESTONE", "I"),
        article("4", "ENEOS", "R"),
        article("5", "ENEOS", "I"));

    Collections.sort(articles, BrandPromotionArticleComparator.getInstance(pBrands));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articles));
    Collections.sort(articles, BrandPromotionArticleComparator.getInstance(Collections.emptyList()));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articles));
  }

  private static ArticleDocDto article(String id, String productBrand, String productGroup) {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys(id);
    article.setProductBrand(productBrand);
    article.setSagProductGroup(productGroup);
    article.setSupplier(productBrand);
    return article;
  }
}
