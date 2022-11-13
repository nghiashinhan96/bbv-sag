package com.sagag.services.ivds.filter.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.SagCollectionUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.utils.RelevanceArticlesUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Order
public class NonCachedArticleFilterContext extends ArticleFilterContext {

  @Override
  public ArticleFilteringResponseDto execute(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request, Pageable pageable,
      Optional<AdditionalSearchCriteria> additional) {

    Pageable fullPage = PageUtils.MAX_PAGE;
    request.setPerfectMatched(true);
    request.setDirectMatch(true);
    ArticleFilterRequest directMatchRequest =
        RelevanceArticlesUtils.prepareDirectMatchRequest(request);
    final FilteredArticleAndAggregationResponse directMatches =
        getArticleFilterResponse(user, filterMode, directMatchRequest, fullPage);
    request.setDirectMatch(false);
    directMatches.getArticles()
        .forEach(article -> article.setRelevanceGroupType(RelevanceGroupType.DIRECT_MATCH));

    request.setHasPreviousData(directMatches.hasArticles());
    final FilteredArticleAndAggregationResponse referenceMatches =
        getArticleFilterResponse(user, filterMode, request, fullPage);
    referenceMatches.getArticles()
        .forEach(article -> article.setRelevanceGroupType(RelevanceGroupType.REFERENCE_MATCH));
    referenceMatches.setArticles(
        new PageImpl<>(sortArticlesBrandPriority(user, referenceMatches.getArticles().getContent()),
            fullPage, referenceMatches.getArticles().getTotalElements()));
    request.setPerfectMatched(false);

    // Collect sagIds to create es constant_score
    List<ArticleDocDto> perfectArticles = searchAndCombineExternalParts(user,
        filterMode, request, fullPage, directMatches, referenceMatches);

    List<String> perfectIds = perfectArticles
        .stream().map(ArticleDocDto::getIdSagsys).distinct().collect(Collectors.toList());
    request.setPreferSagsysIds(perfectIds);

    request.setHasPreviousData(directMatches.hasArticles() || referenceMatches.hasArticles());
    final FilteredArticleAndAggregationResponse possibleMatches =
        getArticleFilterResponse(user, filterMode, request, fullPage);
    possibleMatches.getArticles().forEach(article -> article.setRelevanceGroupType(
        RelevanceGroupType.POSSIBLE_MATCH));
    // Eliminate duplicated articles
    List<ArticleDocDto> newPosMatches = possibleMatches.getArticles()
        .stream().filter(art -> !perfectIds.contains(art.getIdSagsys()))
        .collect(Collectors.toList());

    // Merge direct-match and reference-match and possible-match
    List<ArticleDocDto> articles =
        Stream.of(perfectArticles, newPosMatches)
            .flatMap(Collection::stream)
            .filter(SagCollectionUtils.distinctByKeys(ArticleDocDto::getIdSagsys,
                ArticleDocDto::getRelevanceGroupType))
            .collect(Collectors.toList());

    List<ArticleDocDto> result = new ArrayList<>();
    if (user.getSupportedAffiliate().isAutonetAffiliate()) {
      ivdsArticleTaskExecutors.executeTaskElasticsearch(articles, Optional.empty(),
          user.getSupportedAffiliate());
      result.addAll(articles);
    } else {
      result.addAll(ivdsArticleTaskExecutors
          .executeTaskStockWithoutVehicle(user, articles, additional));
    }

    return ofResult(buildPaging(result, pageable),
        buildArticleFilterItems(possibleMatches.getAggregations(), filterMode));
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    return true;
  }
}
