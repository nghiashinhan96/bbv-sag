package com.sagag.services.dvse.api.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.sagag.services.article.api.utils.DefaultAmountHelper;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.api.VehicleSearchService;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;

public class DvseProcessor {

  @Autowired
  private ArticleSearchService articleSearchService;

  @Autowired
  private VehicleSearchService vehicleSearchService;

  protected Optional<VehicleDto> searchVehicle(Optional<Integer> kTypeNr) {
    if (!kTypeNr.isPresent()) {
      return Optional.empty();
    }
    return vehicleSearchService.searchVehiclesByKTypeNr(kTypeNr.get()).stream()
        .findFirst().map(vehicle -> SagBeanUtils.map(vehicle, VehicleDto.class));
  }

  protected List<ArticleDocDto> searchArticles(SupportedAffiliate affiliate,
    Map<String, Optional<Integer>> quantitiesMap, boolean isSaleOnBehalf) {
    if (affiliate == null || MapUtils.isEmpty(quantitiesMap)) {
      return Collections.emptyList();
    }
    final List<String> articleIds = quantitiesMap.keySet().stream().collect(Collectors.toList());
    final PageRequest pageable = PageUtils.defaultPageable(articleIds.size());
    return articleSearchService.searchArticlesByIdSagSyses(articleIds, pageable, isSaleOnBehalf,
        affiliate.getEsShortName()).map(amountNumberConverter(affiliate, quantitiesMap))
        .getContent();
  }

  private static Function<ArticleDoc, ArticleDocDto> amountNumberConverter(
    final SupportedAffiliate affiliate, final Map<String, Optional<Integer>> quantitiesMap) {
    return article -> {
      final ArticleDocDto articleDocDto = SagBeanUtils.map(article, ArticleDocDto.class);
      DefaultAmountHelper.updateArticleQuantities(articleDocDto, Optional.empty(), Optional.empty(),
        affiliate);
      final Optional<Integer> requestedQuantity = quantitiesMap.getOrDefault(
        articleDocDto.getIdSagsys(), Optional.empty());
      articleDocDto.setAmountNumber(requestedQuantity.orElse(articleDocDto.getSalesQuantity()));
      return articleDocDto;
    };
  }

}
