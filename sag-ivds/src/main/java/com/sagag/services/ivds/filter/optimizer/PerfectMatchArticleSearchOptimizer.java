package com.sagag.services.ivds.filter.optimizer;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.utils.QueryUtils;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.executor.IvdsPerfectMatchArticleTaskExecutor;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PerfectMatchArticleSearchOptimizer {

  private static final int LENGTH_OF_GENERAL_SEARCH_TERM = 2;

  private static final int MAX_SIZE_FOR_GENERAL_SEARCH_TERM = 1000;

  private static final Pageable MAX_PAGE_FOR_GENERAL_SEARCH_TERM =
      PageUtils.defaultPageable(MAX_SIZE_FOR_GENERAL_SEARCH_TERM);

  private static final Pageable MAX_PAGE = PageUtils.MAX_PAGE;

  @Autowired
  private IvdsPerfectMatchArticleTaskExecutor ivdsPerfectMatchArtTaskExecutor;

  /**
   * Returns all search result after fetch stock and availabilities then sorting.
   *
   * @param filterFunc the filter function
   * @param request the request
   * @return the result of search all optimized.
   */
  @LogExecutionTime
  public FilteredArticleAndAggregationResponse filterAllOptimized(
      BiFunction<ArticleFilterRequest, Pageable, FilteredArticleAndAggregationResponse> filterFunc,
      ArticleFilterRequest request) {
    log.debug("Returning filtering all result by perfect match");

    Assert.notNull(request, "The given request must not be null");
    final Pageable maxPageable = defaultMaxPageForSearchTerm(request.getKeyword());
    if (hasWildCardPredicate().test(request)) {
      return FilteredArticleAndAggregationResponse.empty(maxPageable);
    }

    request.setPerfectMatched(true); // Enable perfect match flag.

    final FilteredArticleAndAggregationResponse preferedArticlesRes =
        filterFunc.apply(request, maxPageable);
    final Page<ArticleDocDto> preferedArticles = preferedArticlesRes.getArticles();
    if (!preferedArticles.hasContent()) {
      return FilteredArticleAndAggregationResponse.empty(maxPageable);
    }

    final Pageable pageable = preferedArticles.getPageable();
    final FilteredArticleAndAggregationResponse response =
        FilteredArticleAndAggregationResponse.empty(pageable);
    response.setArticles(preferedArticles);
    response.setAggregations(preferedArticlesRes.getAggregations());

    return response;
  }

  private static Predicate<ArticleFilterRequest> hasWildCardPredicate() {
    return request -> QueryUtils.hasWildCard(request.getKeyword());
  }

  private static Pageable defaultMaxPageForSearchTerm(String searchTerm) {
    Assert.hasText(searchTerm, "The given search term must not be null");
    if (StringUtils.length(searchTerm) > LENGTH_OF_GENERAL_SEARCH_TERM) {
      return MAX_PAGE;
    }
    return MAX_PAGE_FOR_GENERAL_SEARCH_TERM;
  }

  /**
   * Returns the page of optimized result per pageable.
   *
   * @param user the user info
   * @param allOptimizedArticles article list
   * @param filterFunc bi function
   * @param request filter request
   * @param pageable paging properties
   * @param additional additional search criteria
   * @return the page result of response.
   */
  @LogExecutionTime
  public FilteredArticleAndAggregationResponse filterOptimized(UserInfo user,
      List<ArticleDocDto> allOptimizedArticles,
      BiFunction<ArticleFilterRequest, Pageable, FilteredArticleAndAggregationResponse> filterFunc,
      ArticleFilterRequest request, Pageable pageable,
      Optional<AdditionalSearchCriteria> additional) {
    log.debug("Returning filtering result per page with cached perfect match");

    final List<ArticleDocDto> shortedArticles = Lists.newArrayList(allOptimizedArticles);
    final List<String> preferSagsysIds = shortedArticles.stream().map(ArticleDocDto::getArtid)
        .distinct().collect(Collectors.toList());

    final FilteredArticleAndAggregationResponse response =
        FilteredArticleAndAggregationResponse.empty(pageable);

    request.setPerfectMatched(false);
    request.setPreferSagsysIds(preferSagsysIds);

    final FilteredArticleAndAggregationResponse artFilterRes = filterFunc.apply(request, pageable);
    final Page<ArticleDocDto> similarArticles = artFilterRes.getArticles();
    // Update possible match to relevance group type
    similarArticles
        .forEach(article -> article.setRelevanceGroupType(RelevanceGroupType.POSSIBLE_MATCH));

    final Page<ArticleDocDto> articles = joinPages(shortedArticles, similarArticles, pageable);

    if (!articles.hasContent()) {
      return response;
    }

    final Pageable maxPageable = defaultMaxPageForSearchTerm(request.getKeyword());
    final List<ArticleDocDto> articlesList = ivdsPerfectMatchArtTaskExecutor
        .executePerfectMatchTask(user, articles, maxPageable, additional);

    response.setAggregations(artFilterRes.getAggregations());
    response.setArticles(new PageImpl<>(articlesList, articles.getPageable(),
        articles.getTotalElements()));

    return response;
  }

  private static Page<ArticleDocDto> joinPages(List<ArticleDocDto> preferredArticles,
      Page<ArticleDocDto> similarArticles, Pageable pageable) {
    final List<ArticleDocDto> allArticles = new ArrayList<>();

    if (!similarArticles.hasContent() && CollectionUtils.isEmpty(preferredArticles)) {
      return Page.empty(pageable);
    }

    final int pageSize = pageable.getPageSize();
    final int size = preferredArticles.size();
    final int startIndex = pageable.getPageNumber() * pageSize;
    if (!CollectionUtils.isEmpty(preferredArticles) && startIndex < size) {
      int endIndex = Math.min(startIndex + pageSize, size);
      allArticles.addAll(preferredArticles.subList(startIndex, endIndex));
    }

    final List<String> preferredIds =
        preferredArticles.stream().map(ArticleDocDto::getIdSagsys).collect(Collectors.toList());

    similarArticles.getContent().stream()
    .filter(a -> !preferredIds.contains(a.getIdSagsys()) && allArticles.size() < pageSize)
    .forEach(allArticles::add);
    return new PageImpl<>(allArticles, pageable, similarArticles.getTotalElements());
  }

}
