package com.sagag.services.autonet.erp.executor.callable;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.autonet.erp.api.AutonetErpArticleExternalService;
import com.sagag.services.autonet.erp.utils.AutonetErpConstants;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AutonetProfile
public class AutonetErpArticleCallableCreatorImpl implements ErpCallableCreator {

  @Autowired
  private AutonetErpArticleExternalService autonetArticleExternalService;

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria criteria, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updateErpArticles(criteria);
    };
  }

  private List<ArticleDocDto> updateErpArticles(ArticleSearchCriteria criteria) {
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
  public AsyncUpdateMode asyncUpdateMode() {
    return AsyncUpdateMode.PRICE;
  }
}
