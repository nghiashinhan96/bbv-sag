package com.sagag.services.ax.availability.stock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.availability.AvailabilityFilter;
import com.sagag.services.article.api.availability.StockAvailabilitiesFilter;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.ax.availability.externalvendor.VenExternalVendorAvailabilityFilter;
import com.sagag.services.ax.availability.externalvendor.ConExternalVendorAvailabilityFilter;
import com.sagag.services.ax.availability.filter.AxAvailabilityFilter;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

@Component
@DynamicAxProfile
@Slf4j
@Order(2)
public class ArticleIsNotOnStockAvailabilitiesFilter implements StockAvailabilitiesFilter {

  @Autowired
  @Qualifier("articleIsOnStockAvailabilitiesFilter")
  private StockAvailabilitiesFilter articleIsOnStockAvailabilitiesFilter;

  @Autowired
  private VenExternalVendorAvailabilityFilter venExternalVendorAvailabilityFilter;

  @Autowired
  private ConExternalVendorAvailabilityFilter conExternalVendorAvailabilityFilter;

  @Autowired
  @Qualifier("axAvailabilityAsyncCallableCreatorImpl")
  private ErpCallableCreator axAvailabilityAsyncCallableCreator;

  @Autowired
  private AxAvailabilityFilter availabilityFilter;

  @Override
  @LogExecutionTime
  public List<ArticleDocDto> doFilterAvailabilities(List<ArticleDocDto> articles,
      String countryName, ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, List<VendorDto> axVendors) {
    // #4870
    log.info("Filter artiles has no stock: articles: {}", articles.size());

    if (CollectionUtils.isEmpty(articles)) {
      log.debug("No articles with no stock");
      return Collections.emptyList();
    }

    Map<String, List<Availability>> backupMap = buildBackupAvailabilities(articles);
    List<ArticleDocDto> articlesWithExternalAvail = venExternalVendorAvailabilityFilter
      .filter(articles, artCriteria, availCriteria, axVendors, countryName);

    articlesWithExternalAvail = calculateAvailabilityBaseOnAxAndExternalVendors(
        articlesWithExternalAvail, artCriteria, availCriteria, countryName, backupMap);

    AxArticleUtils.updateArticlesAvailability(articles, articlesWithExternalAvail);

    final List<ArticleDocDto> articlesWithoutVen =
        articles.stream().filter(a -> !a.hasVenAvailabilities()).collect(Collectors.toList());
    filterAvailabilitiesForArticles(articlesWithoutVen, countryName, artCriteria, availCriteria);

    if (artCriteria.isDropShipment()) {
      // #3426 Merge avail AX, VEN, CON
      List<ArticleDocDto> articlesWithConVendorAvail = conExternalVendorAvailabilityFilter
          .filter(articles, artCriteria, availCriteria, axVendors, countryName);

      articlesWithExternalAvail =
          getArticlesWithExternalAvail(articlesWithExternalAvail, articlesWithConVendorAvail);
    }

    AxArticleUtils.updateArticlesAvailability(articles, articlesWithExternalAvail);
    
    return articles;
  }

  private Map<String, List<Availability>> buildBackupAvailabilities(List<ArticleDocDto> articles) {
    return articles.stream().collect(Collectors.toMap(ArticleDocDto::getArtid,
        article -> CollectionUtils.emptyIfNull(article.getAvailabilities()).stream()
          .map(SerializationUtils::clone).collect(Collectors.toList())));
  }

  private List<ArticleDocDto> getArticlesWithExternalAvail(
      List<ArticleDocDto> filteredArticlesWithVenAvail,
      List<ArticleDocDto> articlesWithConVendorAvail) {

    List<ArticleDocDto> articlesWithExternalAvail = new ArrayList<>();
    articlesWithExternalAvail.addAll(articlesWithConVendorAvail);
    List<String> articleIdsWithConVendorAvail =
      articlesWithConVendorAvail.stream().map(ArticleDocDto::getArtid).collect(Collectors.toList());
    filteredArticlesWithVenAvail.forEach(art -> {
      if (!articleIdsWithConVendorAvail.contains(art.getArtid()) && art.hasExternalAvail()) {
        articlesWithExternalAvail.add(art);
      }
    });
    return articlesWithExternalAvail;
  }

  /**
   * Calculates availabilities by AX ERP and external vendors.
   *
   * @param articlesWithVenAvail
   * @param artCriteria
   * @param availCriteria
   * @param countryName
   * @param backupMap
   * @return the calculated of list of articles
   */
  private List<ArticleDocDto> calculateAvailabilityBaseOnAxAndExternalVendors(
      List<ArticleDocDto> articlesWithVenAvail, ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, String countryName,
      Map<String, List<Availability>> backupMap) {

    Map<ArticleDocDto, List<Availability>> map = new HashMap<>();
    articlesWithVenAvail.forEach(article -> map.put(article, article.getAvailabilities()));

    log.debug("External vendor map: {}", map);
    articlesWithVenAvail.stream().forEach(art -> {
      if (Objects.nonNull(backupMap.containsKey(art.getIdSagsys()))) {
        art.setAvailabilities(backupMap.get(art.getIdSagsys()));
      }
    });
    articlesWithVenAvail = filterBackOrderFalseAvailabilities(articlesWithVenAvail);
    combineAxAvailabilitiesWithExternalVendorAvailabilities(articlesWithVenAvail, map);

    return map.entrySet().stream().map(item -> calculateAvailabilityBaseOnAxAndExternalVendors(
        item.getKey(), item.getValue(), artCriteria, availCriteria, countryName))
        .collect(Collectors.toList());
  }

  private ArticleDocDto calculateAvailabilityBaseOnAxAndExternalVendors(ArticleDocDto article,
      List<Availability> avais, ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, String countryName) {

    if (CollectionUtils.isEmpty(avais) || Objects.isNull(artCriteria)
        || Objects.isNull(availCriteria) || StringUtils.isEmpty(countryName)) {
      return article;
    }

    ArticleDocDto filterAvaiArticle =
        AxArticleUtils.findArticleContainsAvailabilities(article, avais, artCriteria);

    filterAvaiArticle.setAvailabilities(availabilityFilter.filterAvailabilities(filterAvaiArticle,
        artCriteria, availCriteria.getTourTimeList(), availCriteria.getOpeningHours(),
        countryName));
    return filterAvaiArticle;
  }

  private static void combineAxAvailabilitiesWithExternalVendorAvailabilities(
      List<ArticleDocDto> articlesContainAxAvailabilities,
      Map<ArticleDocDto, List<Availability>> articleAvailMap) {
    articleAvailMap.forEach((article, avails) -> {
      List<Availability> availabilities =
          findAvailabilities(article.getIdSagsys(), articlesContainAxAvailabilities);
      CollectionUtils.emptyIfNull(availabilities).forEach(avail -> {
        if (!avails.contains(avail)) {
          avails.add(avail);
        }
      });
    });
  }

  private static List<Availability> findAvailabilities(String artId, List<ArticleDocDto> articles) {
    return articles.stream().filter(item -> StringUtils.equals(artId, item.getArtid())).findFirst()
      .map(ArticleDocDto::getAvailabilities).orElse(Collections.emptyList());
  }

  private static List<ArticleDocDto> filterBackOrderFalseAvailabilities(
      List<ArticleDocDto> articles) {
    return articles.stream()
        .map(ArticleIsNotOnStockAvailabilitiesFilter::filterBackOrderFalseAvailability)
        .collect(Collectors.toList());
  }

  private static ArticleDocDto filterBackOrderFalseAvailability(ArticleDocDto article) {
    List<Availability> avails = CollectionUtils.emptyIfNull(article.getAvailabilities()).stream()
        .filter(avail -> !avail.isBackOrderTrue()).collect(Collectors.toList());
    article.setAvailabilities(avails);
    return article;
  }

  @Override
  public Predicate<ArticleDocDto> extractFilter() {
    return ArticleDocDto::isNotOnStock;
  }

  @Override
  public AvailabilityFilter availabilityFilter() {
    return availabilityFilter;
  }
}
