package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.builder.AdditionalCriteriaBuilder;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticlePartDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.ivds.api.IvdsThuleService;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.response.ThuleArticleListSearchResponse;
import com.sagag.services.thule.api.ThuleService;
import com.sagag.services.thule.domain.BuyersGuideData;
import com.sagag.services.thule.domain.BuyersGuideOrder;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IvdsThuleServiceImpl extends ArticleProcessor implements IvdsThuleService {

  private static final String THULE_SUPPLIER = "THULE";

  @Autowired
  private ThuleService thuleService;

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private AdditionalCriteriaBuilder additionalCriteriaBuilder;

  @Override
  public ThuleArticleListSearchResponse searchArticlesByBuyersGuide(UserInfo user,
      Map<String, String> formData) {
    final Optional<BuyersGuideData> buyersGuideDataOpt = thuleService.addBuyersGuide(formData);
    if (!buyersGuideDataOpt.isPresent()) {
      return ThuleArticleListSearchResponse.empty();
    }

    final Map<String, Long> quantityMapByMovexId = buyersGuideDataOpt.get().getOrders().stream()
        .collect(Collectors.toMap(BuyersGuideOrder::getMovexId, BuyersGuideOrder::getQuantity));

    final Set<String> movexIdSet = quantityMapByMovexId.keySet();
    final String[] partNrs = movexIdSet.toArray(new String[0]);
    final String[] esAffNameLocks = findEsAffilateNameLocks(user);
    final List<ArticleDocDto> articles = articleSearchService.searchArticleByPartNumbersAndSupplier(
        partNrs, THULE_SUPPLIER, user.isSaleOnBehalf(), esAffNameLocks).stream().map(articleConverter)
        .collect(Collectors.toList());

    articles.forEach(article -> {
      final String movexId =
          findMovexIdFromList(article.getArtnr(), article.getParts(), movexIdSet);
      final Long quantity = quantityMapByMovexId.getOrDefault(movexId, NumberUtils.LONG_ZERO);
      article.setAmountNumber(quantity.intValue());
    });

    final List<ArticleDocDto> updatedArticles = ivdsArticleTaskExecutors
        .executeTaskWithPriceAndStockWithoutVehicle(user, articles, additionalCriteriaBuilder
            .buildDetailAdditional(articles.stream().map(ArticleDocDto::getArtid)
                .collect(Collectors.toList())));

    return ThuleArticleListSearchResponse.builder().thuleArticles(updatedArticles)
        .notFoundPartNumbers(
            getNotFoundPartNumbersFromThule(quantityMapByMovexId.keySet(), updatedArticles))
        .build();
  }

  private static String findMovexIdFromList(String articleNr, List<ArticlePartDto> parts,
      Set<String> movexIdSet) {
    if (CollectionUtils.isEmpty(movexIdSet)) {
      return StringUtils.EMPTY;
    }

    final Optional<String> foundMovexIdByArtNr = movexIdSet.stream()
        .filter(movexId -> StringUtils.containsIgnoreCase(articleNr, movexId)).findFirst();
    if (foundMovexIdByArtNr.isPresent()) {
      return foundMovexIdByArtNr.get();
    }
    return CollectionUtils.emptyIfNull(parts).stream()
        .map(ArticlePartDto::getPnrn).filter(movexIdSet::contains).findFirst()
        .orElse(StringUtils.EMPTY);
  }

  private static List<String> getNotFoundPartNumbersFromThule(Set<String> thulePartNumbers,
      List<ArticleDocDto> foundArticles) {
    if (CollectionUtils.isEmpty(thulePartNumbers)) {
      return Collections.emptyList();
    }
    if (CollectionUtils.isEmpty(foundArticles)) {
      log.warn("Not found any articles from Thule part numbers");
      return new ArrayList<>(thulePartNumbers);
    }
    final Set<String> foundPartNumbers = foundArticles.stream()
        .flatMap(article -> article.getParts().stream()).map(ArticlePartDto::getPnrn)
        .collect(Collectors.toSet());
    final Set<String> foundArtNrs = foundArticles.stream().map(ArticleDocDto::getArtnr)
        .collect(Collectors.toSet());
    final Set<String> foundThulePartNumbers = SetUtils.union(foundPartNumbers, foundArtNrs).toSet();
    log.debug("Found Thule Part Numbers = {}", foundThulePartNumbers);

    final List<String> notFoundPartNumbers = new ArrayList<>(thulePartNumbers);
    notFoundPartNumbers.removeIf(nr -> foundThulePartNumbers.stream()
        .anyMatch(partNr -> StringUtils.startsWithIgnoreCase(partNr, nr)));
    log.debug("Not Found Thule Part Numbers = {}", notFoundPartNumbers);

    return notFoundPartNumbers;
  }

}
