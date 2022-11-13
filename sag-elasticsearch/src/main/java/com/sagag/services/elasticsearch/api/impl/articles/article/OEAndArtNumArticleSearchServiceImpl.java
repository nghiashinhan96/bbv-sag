package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.common.enums.RelevanceGroupType;
import com.sagag.services.common.utils.SagCollectionUtils;
import com.sagag.services.elasticsearch.api.ExternalPartsSearchService;
import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.converter.ExternalPartConverter;
import com.sagag.services.elasticsearch.criteria.ExternalPartsSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.ExternalPartsResponse;
import com.sagag.services.elasticsearch.query.ISearchQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.OEAndArtNumArticleQueryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OEAndArtNumArticleSearchServiceImpl extends ArticleLoopSearchService {

  @Autowired
  private OEAndArtNumArticleQueryBuilder queryBuilder;

  @Autowired
  private ExternalPartsSearchService externalPartsSearchService;

  @Autowired
  private ExternalPartConverter externalPartsConverter;

  @Override
  public Page<ArticleDoc> search(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return Page.empty();
    }
    criteria.onUsePartsExt();

    // Execute direct matches
    criteria.setDirectMatch(true);
    criteria.setSearchExternal(false);
    Page<ArticleDoc> directMatches = searchArticlesLoop(criteria, pageable, false);
    criteria.setDirectMatch(false);
    directMatches
        .forEach(article -> article.setRelevanceGroupType(RelevanceGroupType.DIRECT_MATCH));

    // Execute reference matches
    criteria.setSearchExternal(!directMatches.hasContent());
    criteria.resetUsePartsExt();
    Page<ArticleDoc> referenceMatches = searchArticlesLoop(criteria, pageable, false);
    RelevanceGroupType relevanceType = criteria.isDoubleLoopSearch()
        ? RelevanceGroupType.POSSIBLE_MATCH : RelevanceGroupType.REFERENCE_MATCH;
    referenceMatches.forEach(article -> article.setRelevanceGroupType(relevanceType));

    final List<ArticleDoc> externalParts = new ArrayList<>();
    if (criteria.isUseExternalParts()) {
      ExternalPartsSearchCriteria externalPartsSearchCriteria =
          new ExternalPartsSearchCriteria(criteria.getText());
      ExternalPartsResponse externalPartsResponse =
          externalPartsSearchService.searchByCriteria(externalPartsSearchCriteria);
      Page<ArticleDoc> convertedExtParts =
          externalPartsResponse.getExternalParts().map(externalPartsConverter);
      convertedExtParts
          .forEach(extPart -> extPart.setRelevanceGroupType(RelevanceGroupType.ORIGINAL_PART));
      externalParts.addAll(convertedExtParts.getContent());
    }
    // Combine direct matches and reference matches, also distinct by IdSagsys
    final List<ArticleDoc> allArticles = Stream.of(directMatches.getContent(), externalParts,
            referenceMatches.getContent())
        .flatMap(Collection::stream)
        .filter(SagCollectionUtils.distinctByKeys(ArticleDoc::getIdSagsys))
        .collect(Collectors.toList());
    return new PageImpl<>(allArticles);
  }

  /**
   * Search article by number with supplier or gaid.
   *
   * @param criteria {@link com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria} the article criteria for searching
   * @param pageable the searching page request
   * @return the object response of {@link ArticleFilteringResponse}
   *
   */
  @Override
  public ArticleFilteringResponse filter(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return ArticleFilteringResponse.empty();
    }
    criteria.onAggregated();
    criteria.onUsePartsExt();
    return filterArticlesLoop(criteria, pageable, false);
  }

  @Override
  public ISearchQueryBuilder<KeywordArticleSearchCriteria> queryBuilder() {
    return queryBuilder;
  }
}
