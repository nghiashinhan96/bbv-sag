package com.sagag.services.ax.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sagag.services.article.api.attachedarticle.AttachedArticleHandler;
import com.sagag.services.article.api.availability.AvailabilitiesFilterContext;
import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerDeliveryTimeCalculator;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleErpExternalExecutorBuilders;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchExternalExecutor;
import com.sagag.services.article.api.executor.callable.AsyncUpdateMode;
import com.sagag.services.article.api.price.finalcustomer.FinalCustomerPriceCalculator;
import com.sagag.services.ax.executor.helper.AxArticleSearchHelper;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.config.AppProperties;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Class to implement to execute article info update from AX service.
 *
 */
@Component
@Slf4j
@AxProfile
public class AxArticleSearchExecutor implements ArticleSearchExternalExecutor {

  @Autowired
  private AvailabilitiesFilterContext availabilitiesFilterContext;
  @Autowired
  private ArticleErpExternalExecutorBuilders artErpExternalExecutorBuilder;
  @Autowired
  private FinalCustomerPriceCalculator finalCustomerPriceCalculator;
  @Autowired
  private FinalCustomerDeliveryTimeCalculator finalCustomerDeliveryCalculator;
  @Autowired
  private AttachedArticleHandler attachedArticleHandler;
  @Autowired
  private AxArticleSearchHelper axArticleSearchHelper;

  @Autowired
  private AppProperties appProps;

  /**
   * The main work flow to sync the stock, prices and avail to list of article DTO
   * from ERP AX API.
   * @param criteria article criteria
   * @return List<ArticleDocDto> the list of article dto
   */
  @Override
  @LogExecutionTime
  public List<ArticleDocDto> execute(ArticleSearchCriteria criteria) {
    Assert.notNull(criteria, "The given criteria must not be null");
    Assert.notNull(criteria.getAffiliate(), "The given affiliate must not be null");
    Assert.notNull(criteria.getCustNr(), "The given custNr must not be null");

    log.info("[BBV] execute: Start to get article info from external ERP: "
        + "criteria.updateAvailabilities = {}", criteria.isUpdateAvailability());

    final ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    // Get article info from ERP
    final List<ArticleDocDto> filteredArticles = defaultFilteredArticles(criteria, attributes);

    log.info("[BBV] execute: got filtered articles from ERP: {} articles", filteredArticles.size());
    if (CollectionUtils.isEmpty(filteredArticles)) {
      log.warn("Articles is empty !");
      return Collections.emptyList();
    }

    // Update filtered articles again.
    criteria.setArticles(filteredArticles);
    final List<String> articleIds =
        filteredArticles.stream().map(ArticleDocDto::getArtid).collect(Collectors.toList());

    // Initial the list of vendors.
    final List<VendorDto> vendors = new ArrayList<>();
    criteria.setVendors(vendors);

    log.info("[BBV] execute: Before createErpAsyncCallableCreator");
    Optional
        .ofNullable(artErpExternalExecutorBuilder.buildAsyncCallableCreator(criteria, articleIds,
            appProps.getErpConfig().getMaxRequestSize(), attributes))
        .ifPresent(CompletableFuture::join);
    log.info("[BBV] execute: After createErpAsyncCallableCreator");

    if (!criteria.isExcludeSubArticles()) {
      attachedArticleHandler.buildMemoList(filteredArticles, criteria);
    }

    doCalculatePriceForFinalCustomer(filteredArticles, criteria);

    if (!AsyncUpdateMode.AVAILABILITY.isValid(criteria)) {
      return filteredArticles;
    }

    // Should waiting finished all threads to get ERP info, after filtering availabilities
    final List<ArticleDocDto> finalArticles =
        availabilitiesFilterContext.doFilterAvailabilities(filteredArticles, criteria);
    log.info("[BBV] execute: doFilterAvailabilities: {} articles", filteredArticles.size());
    doCalculateAvailabilitiesForFinalCustomer(finalArticles, criteria);

    attachedArticleHandler.doFilterAvailabilityFgasCases(finalArticles, criteria);
    log.info("[BBV] execute: doFilterAvailabilityFgasCases: {} articles", filteredArticles.size());

    return finalArticles;
  }

  private List<ArticleDocDto> defaultFilteredArticles(final ArticleSearchCriteria criteria,
      final ServletRequestAttributes mainThreadAttrs) {
    final List<ArticleDocDto> articles = criteria.getArticles();
    if (CollectionUtils.isEmpty(articles)) {
      return new ArrayList<>();
    }

    // Get article info from ERP
    final List<ArticleDocDto> filteredArticles;
    final long start = System.currentTimeMillis();
    if (criteria.isFilterArticleBefore()) {
      filteredArticles = axArticleSearchHelper.filterArticles(criteria, mainThreadAttrs);
      // set filtered articles again to handle the removed the invalid pim ids
      criteria.setArticles(filteredArticles);
      log.debug("Perf:AxArticleSearchExecutor->execute->Update ERP Article {} ms",
          System.currentTimeMillis() - start);
      // handle the case of only one invalid pim id (404 return) sent to AX
      // then nothing need to process more
      if (CollectionUtils.isEmpty(filteredArticles)) {
        return new ArrayList<>();
      }
    } else {
      filteredArticles = articles;
    }
    return filteredArticles;
  }

  @Override
  public FinalCustomerPriceCalculator finalCustomerPriceCalculator() {
    return finalCustomerPriceCalculator;
  }

  @Override
  public FinalCustomerDeliveryTimeCalculator finalCustomerDeliveryCalculator() {
    return finalCustomerDeliveryCalculator;
  }

}
