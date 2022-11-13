package com.sagag.services.ivds.utils;

import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.response.ArticleInformationResponseItem;

import org.springframework.stereotype.Component;

@Component
@AutonetProfile
public class AutonetArticleInformationExtractorImpl
    extends AbstractArticleInformationExtractorImpl {

  @Override
  public ArticleInformationResponseItem buildErpInformation(ArticleDocDto art) {
    return ArticleInformationResponseItem.builder().autonetInfos(art.getAutonetInfos())
        .price(art.getPrice()).stock(art.getStock())
        .finalCustomerNetPrice(art.getFinalCustomerNetPrice())
        .totalFinalCustomerNetPrice(art.getTotalFinalCustomerNetPrice())
        .allowedAddToShoppingCart(art.isAllowedAddToShoppingCart()).build();
  }

}
