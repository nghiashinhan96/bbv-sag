package com.sagag.services.ax.callable.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.ChunkedRequestProcessor;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.BulkArticleResult;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AxProfile
public class AxErpArticleFilterCallableCreatorImpl implements ErpCallableCreator,
  ChunkedRequestProcessor<ArticleDocDto> {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private AppProperties appProps;

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria criteria, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updateErpArticle(criteria);
    };
  }

  private List<ArticleDocDto> updateErpArticle(final ArticleSearchCriteria criteria) {
    List<ArticleDocDto> articles = criteria.getArticles();
    // #2434 finding 3 wholesale article is null
    final List<String> articleIds = articles.stream().map(ArticleDocDto::getIdSagsys)
        .collect(Collectors.toList());

    final Map<String, BulkArticleResult> articlesRes = new HashMap<>();
    processPartitionsByTargetMap(articleIds, updateErpArticleProcessor(criteria.getAffiliate()),
        articlesRes);

    // remove all the invalid pim id(s) here
    articles = articles.stream().filter(dto -> articlesRes.keySet().contains(dto.getIdSagsys()))
        .collect(Collectors.toList());
    // Update ERP article info to article
    final List<ArticleDocDto> result = new ArrayList<>(articles);
    result.forEach(article -> {
      final BulkArticleResult bulkArticle = articlesRes.get(article.getIdSagsys());
      if (article.hasArticle(bulkArticle)) {
        article.setArticle(bulkArticle.getArticle());
      }
    });
    return result;
  }

  private Function<List<String>, Map<String, BulkArticleResult>> updateErpArticleProcessor(
      SupportedAffiliate affiliate) {
    return articleIds -> {
      if (CollectionUtils.isEmpty(articleIds)) {
        return Collections.emptyMap();
      }
      final List<List<String>> articleIdPartitions =
          ListUtils.partition(articleIds, appProps.getErpConfig().getMaxRequestSize());
      final Map<String, BulkArticleResult> articlesRes = new HashMap<>();
      for (final List<String> articleIdPartition : articleIdPartitions) {
        try {
          articlesRes.putAll(
              articleExtService.searchByArticleIds(affiliate.getCompanyName(), articleIdPartition));
        } catch (final RestClientException e) {
          log.error(String.format("Issue occurs on article request for articleIds: %s", articleIds),
              e);
        }
      }

      return articlesRes;
    };
  }

  @Override
  public int maxRequestSize() {
    return appProps.getErpConfig().getMaxRequestSize();
  }
}
