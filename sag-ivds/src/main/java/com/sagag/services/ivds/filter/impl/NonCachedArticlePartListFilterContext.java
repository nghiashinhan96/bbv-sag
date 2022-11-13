package com.sagag.services.ivds.filter.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.article.ArticleCriteriaDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.article.ArticlePartItemDto;
import com.sagag.services.elasticsearch.domain.GenArtTxt;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.ivds.domain.FilteredArticleAndAggregationResponse;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.ArticleFilterContext;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.ArticlePartListSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.utils.ArticlesGenArtUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class NonCachedArticlePartListFilterContext extends ArticleFilterContext {

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Autowired
  private GenArtCacheService genArtCacheService;

  @Override
  public ArticleFilteringResponseDto execute(UserInfo user, FilterMode filterMode,
      ArticleFilterRequest request, Pageable pageable,
      Optional<AdditionalSearchCriteria> additional) {
    final FilteredArticleAndAggregationResponse response =
        getArticleFilterResponse(user, filterMode, request, pageable);
    final Page<ArticleDocDto> articles = response.getArticles();

    List<ArticleDocDto> updatedArticles = ivdsArticleTaskExecutors.executeTaskWithErpArticle(user,
        articles.getContent(), Optional.empty(), additional);
    List<ArticleDocDto> result =
        prepareArticlePartList(request.getArticlePartListSearchRequest(), updatedArticles);
    return ofResult(new PageImpl<>(result, pageable, result.size()),
        buildArticleFilterItems(response.getAggregations(), filterMode));
  }

  @Override
  public boolean supportMode(FilterMode filterMode) {
    return filterMode.isNonStoredInCached() && filterMode.isPartList();
  }

  private List<ArticleDocDto> prepareArticlePartList(
      ArticlePartListSearchRequest articlePartListSearchRequest, List<ArticleDocDto> articles) {
    if (Objects.isNull(articlePartListSearchRequest) || CollectionUtils.isEmpty(articles)
        || CollectionUtils.isEmpty(articlePartListSearchRequest.getPartListItems())) {
      return Lists.emptyList();
    }
    List<ArticlePartItemDto> partListItems = articlePartListSearchRequest.getPartListItems();
    List<String> gaids = partListItems.parallelStream().map(ArticlePartItemDto::getGaid)
        .collect(Collectors.toList());
    gaids.removeIf(StringUtils::isBlank);
    final Map<String, GenArtTxt> genArtMap = genArtCacheService.searchGenArtByIds(gaids);
    final Map<String, GenArtTxt> genArtsEng =
        genArtCacheService.searchGenArtByIdsAndLanguage(gaids, Optional.of(Locale.ENGLISH));
    final Map<String, ArticleDocDto> articleMap =
        articles.stream().collect(Collectors.toMap(ArticleDocDto::getOriginIdSagsys, Function.identity()));

    return partListItems.stream()
        .filter(partListItem -> Objects.nonNull(partListItem.getPartsListItemIdArt()))
        .sorted(Comparator.comparing(ArticlePartItemDto::getSort))
        .map(partListItemConverter(articleMap, genArtMap, genArtsEng))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private Function<ArticlePartItemDto, ArticleDocDto> partListItemConverter(
      Map<String, ArticleDocDto> articleMap, Map<String, GenArtTxt> genArtMap,
      final Map<String, GenArtTxt> genArtsEng) {
    return partListItem -> {
      ArticleDocDto article =
          articleMap.get(Objects.toString(partListItem.getPartsListItemIdArt()));
      if (article != null) {
        if (!Objects.isNull(partListItem.getQuantity())) {
          article.setSalesQuantity(partListItem.getQuantity());
        }

        if (StringUtils.isNotBlank(partListItem.getGaid())) {
          article.setGenArtTxts(Collections.singletonList(
              ArticlesGenArtUtils.defaultGenArtTxtIfNull(genArtMap.get(article.getGaId()))));
          article.setGenArtTxtEng(
              ArticlesGenArtUtils.defaultGenArtTxtIfNull(genArtsEng.get(article.getGaId())));
        }

        if (CollectionUtils.isNotEmpty(partListItem.getCriteria())) {
          final List<ArticleCriteriaDto> articleCriteria = partListItem.getCriteria().stream()
              .map(criteria -> SagBeanUtils.map(criteria, ArticleCriteriaDto.class))
              .collect(Collectors.toList());
          article.setCriteria(articleCriteria);
        }
      }
      return article;
    };
  }

}
