package com.sagag.services.ax.availability.filter.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.AvailabilitiesFilterContext;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.callable.StockAsyncCallableCreator;
import com.sagag.services.common.profiles.WintProfile;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.LocationAvailability;
import com.sagag.services.domain.sag.erp.LocationAvailabilityItem;
import com.sagag.services.domain.sag.external.GrantedBranch;

import lombok.extern.slf4j.Slf4j;

@Component
@WintProfile
@Slf4j
public class WtAvailabilitiesFilterContext implements AvailabilitiesFilterContext {

  @Autowired
  @Qualifier("axStockAsyncCallableCreatorImpl")
  private StockAsyncCallableCreator stockAsyncCallableCreator;

  @Autowired
  private WtAvailabilityBuilder wtAvailabilityBuilder;

  @Override
  public List<ArticleDocDto> doFilterAvailabilities(List<ArticleDocDto> originalArticles,
      ArticleSearchCriteria criteria) {
    if (CollectionUtils.isEmpty(originalArticles) || criteria == null) {
      return Collections.emptyList();
    }

    final Map<String, List<ArticleDocDto>> articleGroups =
        originalArticles.stream().collect(Collectors.groupingBy(ArticleDocDto::getIdSagsys));

    final List<ArticleDocDto> articles = articleGroups.values().stream()
        .map(WtAvailabilitiesFilterContext::combineGroupArticles)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    if (articles.stream().allMatch(art -> CollectionUtils.isEmpty(art.getWtStocks()))) {
      log.debug("Retrying get stock from ERP one more time.");
      final ArticleSearchCriteria clonedCriteria = new ArticleSearchCriteria();
      SagBeanUtils.copyProperties(criteria, clonedCriteria);
      clonedCriteria.setArticles(articles);
      try {
        stockAsyncCallableCreator.create(clonedCriteria).call();
      } catch (Exception ex) {
        log.error("Update article stock from ERP has exception: ", ex);
        return articles;
      }
    }

    articles.forEach(art -> calculateLocalAvail(art, criteria));
    return ListUtils.emptyIfNull(articles);
  }

  private void calculateLocalAvail(ArticleDocDto article, ArticleSearchCriteria criteria) {
    List<GrantedBranch> grantedBranches = ListUtils.emptyIfNull(criteria.getGrantedBranches());

    List<GrantedBranch> sortedgrantedBranches =
        grantedBranches.stream().sorted(wtAvailabilityBuilder.prioBranchComparator())
        .collect(Collectors.toList());

    List<String> sortedgrantedBranchIds =
        sortedgrantedBranches.stream().map(GrantedBranch::getBranchId).collect(Collectors.toList());

    final List<LocationAvailabilityItem> localAvailItems = wtAvailabilityBuilder
        .buildLocalAvailItems(article, sortedgrantedBranches, criteria.getCompanyBranches());

    final boolean isAllInGrantedBranches = wtAvailabilityBuilder
        .isAllInGrantedBranches(localAvailItems, sortedgrantedBranchIds, article.getAmountNumber());

    final LocationAvailability locationAvailability = LocationAvailability.builder()
        .state(wtAvailabilityBuilder.buildAvailState(article, grantedBranches))
        .name(wtAvailabilityBuilder.buildAvailName(article, grantedBranches))
        .allInPrioLocations(isAllInGrantedBranches)
        .items(localAvailItems)
        .build();

    final Availability availability = Availability.builder()
        .articleId(article.getIdSagsys())
        .quantity(article.getAmountNumber())
        .location(locationAvailability)
        .build();

    final List<Availability> availabilities = new ArrayList<>();
    availabilities.add(availability);
    article.setAvailabilities(availabilities);
  }

  private static ArticleDocDto combineGroupArticles(List<ArticleDocDto> articles) {
    if (CollectionUtils.isEmpty(articles)) {
      return null;
    }
    Integer totalQuantity =
        articles.stream().collect(Collectors.summingInt(ArticleDocDto::getAmountNumber));
    ArticleDocDto article = articles.get(0);
    article.setAmountNumber(totalQuantity);
    return article;
  }
}
