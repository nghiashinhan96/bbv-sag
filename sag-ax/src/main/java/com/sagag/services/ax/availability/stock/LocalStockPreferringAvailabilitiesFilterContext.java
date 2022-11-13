package com.sagag.services.ax.availability.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.availability.StockAvailabilitiesFilter;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.utils.ArticleApiUtils;
import com.sagag.services.common.profiles.LocalStockPreferringProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@LocalStockPreferringProfile(true)
public class LocalStockPreferringAvailabilitiesFilterContext
    extends ArticlesStockAvailabilitiesFilterContext {

  @Autowired
  @Qualifier("articleIsOnStockAvailabilitiesFilter")
  private StockAvailabilitiesFilter articleIsOnStockAvailabilitiesFilter;

  @Autowired
  @Qualifier("articleIsNotOnStockAvailabilitiesFilter")
  private StockAvailabilitiesFilter articleIsnotOnStockAvailabilitiesFilter;

  @Override
  public void doFilterStockAvailabilities(List<ArticleDocDto> filteredArticles,
      List<ArticleDocDto> articles, String countryName, ArticleSearchCriteria criteria,
      AdditionalArticleAvailabilityCriteria availCriteria, List<VendorDto> axVendors) {
    List<ArticleDocDto> inStockArticles = new ArrayList<>();
    List<ArticleDocDto> notInStockArticles = new ArrayList<>();
    CollectionUtils.emptyIfNull(articles).forEach(
        articleAvailabilitiesBindingProcessor(inStockArticles, notInStockArticles));

    List<ArticleDocDto> articlesInStock = articleIsOnStockAvailabilitiesFilter
        .doFilterAvailabilities(inStockArticles, countryName, criteria, availCriteria, axVendors);
    List<ArticleDocDto> articlesNotInStock = articleIsnotOnStockAvailabilitiesFilter
        .doFilterAvailabilities(notInStockArticles, countryName, criteria, availCriteria,
            axVendors);

    CollectionUtils.emptyIfNull(articlesInStock)
    .forEach(art -> {
      Optional<ArticleDocDto> articleOpt = articlesNotInStock.stream()
          .filter(filteredArt -> StringUtils.equalsIgnoreCase(
              filteredArt.getArtid(), art.getArtid())).findFirst();
      articleOpt.map(item -> CollectionUtils.emptyIfNull(item.getAvailabilities()))
      .ifPresent(art.getAvailabilities()::addAll);
    });

    filteredArticles.addAll(articlesInStock);

    CollectionUtils.emptyIfNull(articlesNotInStock).forEach(art -> {
      Optional<ArticleDocDto> findFirst = filteredArticles.stream()
          .filter(
              filteredArt -> StringUtils.equalsIgnoreCase(filteredArt.getArtid(), art.getArtid()))
          .findFirst();
      if (!findFirst.isPresent()) {
        filteredArticles.add(art);
      }
    });
  }

  private static Consumer<ArticleDocDto> articleAvailabilitiesBindingProcessor(
      List<ArticleDocDto> inStockArticles, List<ArticleDocDto> notInStockArticles) {
    return art -> {
      final Predicate<Availability> validAvailabilityArrivalTimePredicate =
          avail -> avail != null && !avail.isBackOrderTrue()
          && StringUtils.isNotEmpty(avail.getArrivalTime())
          && ArticleApiUtils.isValidArrivalTime(avail);

      List<Availability> notInStockAvail = CollectionUtils.emptyIfNull(art.getAvailabilities())
          .stream().filter(validAvailabilityArrivalTimePredicate.negate())
          .collect(Collectors.toList());
      List<Availability> inStockAvail = CollectionUtils.emptyIfNull(art.getAvailabilities())
          .stream().filter(validAvailabilityArrivalTimePredicate).collect(Collectors.toList());

      if (art.getAmountNumber() <= 0) {
        return;
      }

      if (art.getTotalAxStock() == null || art.getTotalAxStock() == 0) {
        notInStockArticles.add(art);
        return;
      }

      ArticleDocDto cloneInStock = SerializationUtils.clone(art);
      ArticleDocDto cloneNotInStock = SerializationUtils.clone(art);
      int inStockQuantity = CollectionUtils.emptyIfNull(inStockAvail).stream()
          .mapToInt(Availability::getQuantity).sum();
      cloneInStock.setAmountNumber(inStockQuantity);
      cloneInStock.setAvailabilities(inStockAvail);
      int notInStockQuantity = CollectionUtils.emptyIfNull(notInStockAvail).stream()
          .mapToInt(Availability::getQuantity).sum();
      cloneNotInStock.setAmountNumber(notInStockQuantity);
      cloneNotInStock.setAvailabilities(notInStockAvail);
      if (inStockQuantity > 0) {
        inStockArticles.add(cloneInStock);
      }
      if (inStockQuantity < art.getAmountNumber()) {
        notInStockArticles.add(cloneNotInStock);
      }
    };
  }

}
