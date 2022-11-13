package com.sagag.services.article.api.executor;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.article.api.executor.callable.ExternalVendorCallableCreator;
import com.sagag.services.article.api.price.DisplayedPriceRequestCriteria;
import com.sagag.services.article.api.request.ExternalVendorRequestBody;
import com.sagag.services.common.executor.CallableCreator;
import com.sagag.services.common.executor.ThreadManager;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ArticleErpExternalExecutorBuilders {

  private static final int ASYNC_BATCH_SIZE = 3;

  @Autowired
  private ThreadManager threadManager;

  @Autowired(required = false)
  private List<ErpCallableCreator> erpAsyncCallableCreators;

  @Autowired(required = false)
  private ExternalVendorCallableCreator extVendorAsyncCallableCreator;

  @Autowired(required = false)
  private CallableCreator<DisplayedPriceRequestCriteria, Void> displayedPriceAsyncCallableCreator;

  /**
   * Builds ERP asynchronous by criteria.
   *
   * @param criteria
   * @param articleIds
   * @param requestSize
   * @param attributes
   *
   * @return the result of ERP asynchronous request
   */
  public CompletableFuture<Void> buildAsyncCallableCreator(
      final ArticleSearchCriteria criteria, final List<String> articleIds, final int requestSize,
      final ServletRequestAttributes attributes) {
    final List<CompletableFuture<?>> completableFutures = new LinkedList<>();
    completableFutures.addAll(buildErpAsyncCallableCreator(criteria, attributes));

    if (isGetVendorsErpPredicate().test(criteria) && !CollectionUtils.isEmpty(articleIds)) {
      completableFutures.addAll(buildGetVendorsErpAsyncCallableCreator(
          criteria, articleIds, requestSize, attributes));
    }

    if (CollectionUtils.isEmpty(completableFutures)) {
      return null;
    }

    log.info("The number of async callables are = {}", completableFutures.size());
    return CompletableFuture.allOf(completableFutures.stream().toArray(CompletableFuture[]::new));
  }

  private static Predicate<ArticleSearchCriteria> isGetVendorsErpPredicate() {
    return criteria -> AsyncUpdateMode.AVAILABILITY.isValid(criteria) || criteria.isUpdateVendors();
  }


  /**
   * Executes the ERP asynchronous request by criteria.
   *
   * @param criteria
   * @param mainThreadAttrs
   *
   * @return the ERP asynchronous callable creator.
   */
  private List<CompletableFuture<List<ArticleDocDto>>> buildErpAsyncCallableCreator(
      final ArticleSearchCriteria criteria, final ServletRequestAttributes mainThreadAttrs) {
    if (CollectionUtils.isEmpty(erpAsyncCallableCreators)) {
      return Lists.newArrayList();
    }

    final Predicate<ErpCallableCreator> erpCallableCreatorPredicate =
        creator -> Objects.nonNull(creator.asyncUpdateMode())
            && creator.asyncUpdateMode().isValid(criteria);

    return erpAsyncCallableCreators.stream().filter(erpCallableCreatorPredicate)
        .map(creator -> creator.create(criteria, mainThreadAttrs))
        .map(task -> threadManager.supplyAsync(task, Collections.emptyList()))
        .collect(Collectors.toList());
  }

  /**
   * Executes the ERP get list of vendors asynchronous request by criteria.
   *
   * @param criteria
   * @param articleIds
   * @param requestSize
   * @param attributes
   *
   * @return the ERP asynchronous callable creator.
   */
  private List<CompletableFuture<List<VendorDto>>> buildGetVendorsErpAsyncCallableCreator(
      final ArticleSearchCriteria criteria,
      final List<String> articleIds, final int requestSize,
      final ServletRequestAttributes attributes) {
    if (extVendorAsyncCallableCreator == null || CollectionUtils.isEmpty(articleIds)
        || StringUtils.isBlank(criteria.getCompanyName())) {
      return Lists.newArrayList();
    }

    // Split list of article ids by default size of AX ERP.
    final List<List<String>> articleIdPartitions = ListUtils.partition(articleIds, requestSize);

    // Split list of partitions, just send a bundle by setting.
    final List<List<List<String>>> articleIdPartitionBatches =
        ListUtils.partition(articleIdPartitions, ASYNC_BATCH_SIZE);

    final Function<List<List<String>>, List<CompletableFuture<List<VendorDto>>>> asyncFunction =
        articleIdPartition -> {
          final List<ExternalVendorRequestBody> requests = articleIdPartition.stream()
              .map(ids -> ExternalVendorRequestBody.builder().articleIds(ids)
                  .companyName(criteria.getCompanyName()).build()).collect(Collectors.toList());
          return requests.stream()
              .map(req -> extVendorAsyncCallableCreator.create(req, attributes, criteria))
              .map(task -> threadManager.supplyAsync(task, Collections.emptyList()))
              .collect(Collectors.toList());
        };

    return articleIdPartitionBatches.stream().map(asyncFunction).flatMap(List::stream)
        .collect(Collectors.toList());
  }

  public CompletableFuture<Void> buildGetErpDisplayedPriceList(
      List<DisplayedPriceRequestCriteria> displayedPriceRequestList,
      ServletRequestAttributes mainThreadAttrs) {
    final List<CompletableFuture<Void>> futureJobs = new ArrayList<>();
    if (displayedPriceAsyncCallableCreator == null) {
      return CompletableFuture.allOf(futureJobs.toArray(new CompletableFuture[futureJobs.size()]));
    }

    displayedPriceRequestList.forEach(request -> futureJobs.add(
        createErpDisplayedPrice(request, mainThreadAttrs)));
    return CompletableFuture.allOf(futureJobs.toArray(new CompletableFuture[futureJobs.size()]));
  }

  private CompletableFuture<Void> createErpDisplayedPrice(DisplayedPriceRequestCriteria criteria,
      ServletRequestAttributes mainThreadAttrs) {
    Callable<Void> callable =
        displayedPriceAsyncCallableCreator.create(criteria, mainThreadAttrs);
    return threadManager.supplyAsyncVoid(callable);
  }

}
