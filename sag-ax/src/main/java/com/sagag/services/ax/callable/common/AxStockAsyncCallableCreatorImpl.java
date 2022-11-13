package com.sagag.services.ax.callable.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.executor.callable.ChunkedRequestProcessor;
import com.sagag.services.ax.callable.StockAsyncCallableCreator;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.ArticleStock;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AxProfile
public class AxStockAsyncCallableCreatorImpl implements StockAsyncCallableCreator,
  ChunkedRequestProcessor<ArticleDocDto> {

  @Autowired
  private ArticleExternalService articleExtService;

  @Autowired
  private AppProperties appProps;

  @Override
  public Callable<List<ArticleDocDto>> create(ArticleSearchCriteria criteria, Object... objects) {
    return () -> {
      CallableCreator.setupThreadContext(objects);
      return updateStock(criteria);
    };
  }

  private List<ArticleDocDto> updateStock(final ArticleSearchCriteria criteria) {
    final List<ArticleDocDto> articles = criteria.getArticles();
    return processPartitionsByList(articles, updateStockProcessor(criteria));
  }

  private UnaryOperator<List<ArticleDocDto>> updateStockProcessor(ArticleSearchCriteria criteria) {
    return articles -> {
      final List<String> articleIds =
          articles.stream().map(ArticleDocDto::getIdSagsys).collect(Collectors.toList());
      if (CollectionUtils.isEmpty(articleIds)) {
        log.warn("Articles is empty !");
        return new ArrayList<>(articles);
      }

      final Map<String, List<ArticleStock>> articleStockRes = new HashMap<>();
      final List<List<String>> articleIdPartitions =
          ListUtils.partition(articleIds, appProps.getErpConfig().getMaxRequestSize());
      for (final List<String> articleIdPartition : articleIdPartitions) {
        try {
          articleStockRes.putAll(articleExtService.searchStocks(
              criteria.getAffiliate().getCompanyName(), articleIdPartition, StringUtils.EMPTY));
        } catch (final RestClientException e) {
          log.error("Issue occurs on article stocks request for articleIds: ", e);
        }
      }

      if (MapUtils.isEmpty(articleStockRes)) {
        log.warn("articleStockRes is empty !");
        return new ArrayList<>(articles);
      }

      // Update stock info to article
      final List<ArticleDocDto> result = new ArrayList<>(articles);
      result.forEach(article -> {
        List<ArticleStock> stockes = articleStockRes.get(article.getIdSagsys());
        if (criteria.getAffiliate().isSbAffiliate()) {
          article.setWtStocks(stockes);
        }
        final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.valueOf(criteria.getDeliveryType());
        final String branchId = AxBranchUtils.defaultBranchId(sendMethod,
            criteria.getPickupBranchId(), criteria.getDefaultBrandId());

        Optional<ArticleStock> localStock =
            stockes.stream().filter(item -> branchId.equals(item.getBranchId())).findFirst();
        localStock.ifPresent(article::setStock);

        final Double totalAxStock = stockes.stream().filter(stock -> stock.getStockAmount() > 0)
            .map(ArticleStock::getStockAmount).collect(Collectors.summingDouble(Double::doubleValue));
        article.setTotalAxStock(totalAxStock);
      });

      return result;
    };
  }

  @Override
  public AsyncUpdateMode asyncUpdateMode() {
    return AsyncUpdateMode.STOCK;
  }

  @Override
  public int maxRequestSize() {
    return appProps.getErpConfig().getMaxRequestSize();
  }
}
