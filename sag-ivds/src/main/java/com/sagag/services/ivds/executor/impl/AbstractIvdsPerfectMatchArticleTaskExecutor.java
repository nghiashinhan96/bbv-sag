package com.sagag.services.ivds.executor.impl;

import com.sagag.services.article.api.availability.comparator.AvailabilityArticleComparator;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.executor.IvdsPerfectMatchArticleTaskExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractIvdsPerfectMatchArticleTaskExecutor
  implements IvdsPerfectMatchArticleTaskExecutor {

  protected static final int MAX_AVAIL_REQUEST = 60;

  @Autowired
  protected AvailabilityArticleComparator articleAvailabilityComparator;

  @Autowired
  protected IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  protected static boolean isValidToRequestAvailability(Page<ArticleDocDto> articles,
      Pageable pageable) {
    return articles.hasContent() && isOverTotalElements(articles, pageable);
  }

  private static boolean isOverTotalElements(Page<ArticleDocDto> articles, Pageable pageable) {
    return articles.getTotalElements() >= pageable.getPageNumber() * pageable.getPageSize();
  }

  protected List<ArticleDocDto> splitListOfArticlesToRequestAvailabilitiy(
      Page<ArticleDocDto> articles) {
    final List<ArticleDocDto> limitedArticlesToGetAvail = articles.getContent().stream()
        .limit(MAX_AVAIL_REQUEST).collect(Collectors.toList());

    limitedArticlesToGetAvail.forEach(a -> a.setAvailRequested(true));
    return limitedArticlesToGetAvail;
  }

  protected List<ArticleDocDto> processSortedPerfectMatchArticles(
      final Page<ArticleDocDto> originalArticles, final List<ArticleDocDto> articlesToGetAvail,
      final List<ArticleDocDto> updatedErpArticles) {

    final Map<String, ArticleDocDto> updatedArticleMap = updatedErpArticles.stream()
        .collect(Collectors.toMap(ArticleDocDto::getIdSagsys, Function.identity(),
            (oldArt, newArt) -> newArt));

    articlesToGetAvail
    .forEach(article -> Optional.ofNullable(updatedArticleMap.get(article.getIdSagsys()))
        .map(ArticleDocDto::getAvailabilities).ifPresent(article::setAvailabilities));

    final List<ArticleDocDto> perfectMatchArticles =
        articleAvailabilityComparator.sortPerfectMatchArticles(new ArrayList<>(articlesToGetAvail));

    final List<String> availRequestedIdSagsys = articlesToGetAvail.stream()
        .map(ArticleDocDto::getIdSagsys).distinct().collect(Collectors.toList());

    originalArticles.filter(art -> !availRequestedIdSagsys.contains(art.getIdSagsys()))
    .forEach(perfectMatchArticles::add);

    return perfectMatchArticles;
  }

}
