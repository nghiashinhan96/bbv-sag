package com.sagag.services.service.utils;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArticleDocDtoDataTestProvider {

  public static ArticleDocDto createArticleDoc() {
    final ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1234567");
    article.setArtnr("test");
    article.setGaId("7");
    PriceWithArticlePrice priceWithArticlePrice = new PriceWithArticlePrice();
    PriceWithArticle priceWithArticle = new PriceWithArticle();
    priceWithArticle.setPrice(priceWithArticlePrice);
    article.setPrice(priceWithArticle);
    return article;
  }

}
