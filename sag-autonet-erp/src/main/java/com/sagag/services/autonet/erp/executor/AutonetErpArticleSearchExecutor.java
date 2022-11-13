package com.sagag.services.autonet.erp.executor;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerDeliveryTimeCalculator;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.autonet.erp.api.AutonetErpArticleExternalService;
import com.sagag.services.autonet.erp.utils.AutonetErpConstants;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.article.ArticleDocDto;

@Component
@AutonetProfile
public class AutonetErpArticleSearchExecutor implements ArticleSearchExternalExecutor {

  @Autowired
  private AutonetErpArticleExternalService autonetArticleExternalService;

  @Override
  public List<ArticleDocDto> execute(ArticleSearchCriteria criteria) {
    final String username = criteria.getExtUsername();
    final String customerId = criteria.getExtCustomerId();
    final String securityToken = criteria.getExtSecurityToken();
    final String language = criteria.getExtLanguage();
    final double vatRate = criteria.getVatRate();
    final List<ArticleDocDto> articles = criteria.getArticles();
    final AdditionalSearchCriteria additional = criteria.getAdditional();
    final List<List<ArticleDocDto>> bundles =
        ListUtils.partition(articles, AutonetErpConstants.DEFAULT_REQUEST_SIZE);

    return bundles.stream().flatMap(items -> autonetArticleExternalService
        .searchArticlePricesAndAvailabilities(username, customerId, securityToken, language, items,
            vatRate, additional).stream()).collect(Collectors.toList());
  }

  @Override
  public FinalCustomerPriceCalculator finalCustomerPriceCalculator() {
    throw new UnsupportedOperationException(
        "Unsupported calculate price final customer for Autonet");
  }

  @Override
  public FinalCustomerDeliveryTimeCalculator finalCustomerDeliveryCalculator() {
    throw new UnsupportedOperationException(
        "Unsupported calculate availabilities final customer for Autonet");
  }

}
