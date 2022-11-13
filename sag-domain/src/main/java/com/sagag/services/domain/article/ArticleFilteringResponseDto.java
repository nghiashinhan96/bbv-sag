package com.sagag.services.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.PageUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class to contain result of articles filtering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"filters", "articles", "context_key"})
public class ArticleFilteringResponseDto implements Serializable {

  private static final long serialVersionUID = 1206685618210506308L;

  // #2433
  private static final int DF_PAGE_SIZE = 40;
  // #3422
  private static final int CH_DF_PAGE_SIZE = 20;

  private Page<ArticleDocDto> articles;

  private Map<String, List<ArticleFilterItem>> filters;

  @JsonProperty("context_key")
  private String contextKey;

  @JsonIgnore
  private int availabilityCurrentPageNr;

  @JsonIgnore
  public boolean hasContent() {
    return articles != null && articles.hasContent();
  }

  @JsonIgnore
  public boolean isUpdatedArticlesPage(Pageable pageable, SupportedAffiliate supportedAffiliate) {
    final int bunchSize = getAvailBunchSize(supportedAffiliate);
    return pageable != null && pageable.hasPrevious()
        && pageable.getOffset() < (availabilityCurrentPageNr + 1) * bunchSize;
  }

  @JsonIgnore
  public List<ArticleDocDto> getCurrentTopBunchOfArticlesByPageNr(SupportedAffiliate supportedAffiliate) {
    if (!hasContent()) {
      return Collections.emptyList();
    }
    final int bunchSize = getAvailBunchSize(supportedAffiliate);
    return getArticlesByPageable(
        PageUtils.defaultPageable(availabilityCurrentPageNr, bunchSize));
  }

  @JsonIgnore
  public List<ArticleDocDto> getNextTopBunchOfArticles(SupportedAffiliate supportedAffiliate) {
    if (!hasContent()) {
      return Collections.emptyList();
    }
    final int bunchSize = getAvailBunchSize(supportedAffiliate);
    return getArticlesByPageable(PageUtils.defaultPageable(availabilityCurrentPageNr + 1,
        bunchSize));
  }

  @JsonIgnore
  private List<ArticleDocDto> getArticlesByPageable(final Pageable pageable) {
    if (!hasContent() || pageable == null) {
      return Collections.emptyList();
    }
    return ListUtils.partition(articles.getContent(), pageable.getPageSize())
        .get(pageable.getPageNumber());
  }

  @JsonIgnore
  public List<ArticleDocDto> bindUpdatedArticles(final List<ArticleDocDto> updatedArticles) {
    if (CollectionUtils.isEmpty(updatedArticles)) {
      return articles.getContent();
    }
    final Map<String, ArticleDocDto> updatedArticleMap = updatedArticles.stream()
        .collect(Collectors.toMap(ArticleDocDto::getIdSagsys, Function.identity()));
    articles.forEach(article -> {
      final ArticleDocDto updatedArticle = updatedArticleMap.get(article.getIdSagsys());
      if (updatedArticle != null) {
        article.setAvailabilities(updatedArticle.getAvailabilities());
      }
    });
    return articles.getContent();
  }

  @JsonIgnore
  public long totalElements() {
    return articles.getTotalElements();
  }

  @JsonIgnore
  public static List<ArticleDocDto> splitArticlePage(List<ArticleDocDto> articles, Pageable pageable,
      int availabilityCurrentPageNr, SupportedAffiliate supportedAffiliate) {
    final int bunchSize = getAvailBunchSize(supportedAffiliate);
    if (CollectionUtils.isEmpty(articles) || CollectionUtils.size(articles) > bunchSize
        || pageable == null) {
      return Collections.emptyList();
    }
    final int size = pageable.getPageSize();
    final int pageNr = pageable.getPageNumber();
    final int index;
    if (pageNr >= bunchSize / size) {
      index = pageNr - (bunchSize / size) * availabilityCurrentPageNr;
    } else {
      index = pageNr;
    }
    return ListUtils.partition(articles, size).get(index);
  }

  @JsonIgnore
  private static int getAvailBunchSize(SupportedAffiliate supportedAffiliate) {
    if (supportedAffiliate.isChAffiliate()) {
      return CH_DF_PAGE_SIZE;
    }
    return DF_PAGE_SIZE;
  }
}
