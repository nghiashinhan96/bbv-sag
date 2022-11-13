package com.sagag.services.ivds.filter.articles.impl;

import com.sagag.services.domain.article.ArticleAccessoryDto;
import com.sagag.services.domain.article.ArticleAccessoryItemDto;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.ArticleIdListSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.articles.IArticleFilter;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessoryListArticleFilterImpl implements IArticleFilter {

  @Autowired
  @Qualifier("articleIdListSearchServiceImpl")
  private IArticleSearchService<ArticleIdListSearchCriteria, Page<ArticleDoc>> articleIdListSearchService;

  @Override
  public ArticleFilteringResponse filterArticles(ArticleFilterRequest request, Pageable pageable,
      String[] affNameLocks, boolean isSaleOnBehalf) {
    Assert.notNull(request.getAccessorySearchRequest(),
        "The given Accessory Search request mut not be null");

    final List<String> artIds = request.getAccessorySearchRequest().getAccessoryList().stream()
        .map(ArticleAccessoryDto::getAccesoryListItems)
        .flatMap(articleAccessoryItemDtos -> articleAccessoryItemDtos.stream())
        .map(ArticleAccessoryItemDto::getAccessoryArticleIdArt).map(id -> String.valueOf(id))
        .collect(Collectors.toList());

    final ArticleIdListSearchCriteria criteria = new ArticleIdListSearchCriteria();
    criteria.setArticleIdList(artIds);
    criteria.setAffNameLocks(affNameLocks);
    criteria.setUseDefaultArtId(true);
    criteria.setSaleOnBehalf(isSaleOnBehalf);

    return articleIdListSearchService.filter(criteria, pageable);
  }

  @Override
  public FilterMode mode() {
    return FilterMode.ACCESSORY_LIST;
  }

}
