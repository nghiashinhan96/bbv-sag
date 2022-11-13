package com.sagag.services.ivds.utils;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.response.ArticleInformationResponseItem;

public class AbstractArticleInformationExtractorImpl implements ArticleInformationExtractor{

  @Override
  public ArticleInformationResponseItem buildErpInformation(ArticleDocDto art) {
    return ArticleInformationResponseItem.builder().availabilities(art.getAvailabilities())
        .price(art.getPrice()).stock(art.getStock())
        .finalCustomerNetPrice(art.getFinalCustomerNetPrice())
        .totalFinalCustomerNetPrice(art.getTotalFinalCustomerNetPrice())
        .finalCustomerNetPriceWithVat(art.getFinalCustomerNetPriceWithVat())
        .totalFinalCustomerNetPriceWithVat(art.getTotalFinalCustomerNetPriceWithVat())
        .allowedAddToShoppingCart(art.isAllowedAddToShoppingCart())
        .totalAxStock(art.getTotalAxStock())
        .deliverableStock(art.getDeliverableStock())
        .vocArticle(art.getVocArticle())
        .depositArticle(art.getDepositArticle())
        .vrgArticle(art.getVrgArticle())
        .pfandArticle(art.getPfandArticle())
        .memos(art.getMemos())
        .build();
  }

}
