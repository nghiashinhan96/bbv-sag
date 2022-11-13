package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.SagCollectionUtils;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.CrossReferenceArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.enums.ArticlePartType;
import com.sagag.services.elasticsearch.query.articles.article.CrossReferenceQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrossReferenceSearchServiceImpl extends AbstractArticleElasticsearchService implements
    IArticleSearchService<CrossReferenceArticleSearchCriteria, Page<ArticleDoc>> {

  @Autowired
  private CrossReferenceQueryBuilder queryBuilder;

  @Override
  public Page<ArticleDoc> search(CrossReferenceArticleSearchCriteria criteria, Pageable pageable) {
    return null;
  }

  @LogExecutionTime
  @Override
  public ArticleFilteringResponse filter(CrossReferenceArticleSearchCriteria criteria, Pageable pageable) {
    final String artNumber = criteria.getArtNr();
    Asserts.notNull(artNumber, "Article number should not be null");
    final String brandId = criteria.getBrandId();
    Asserts.notNull(brandId, "Brand id should not be null");
    // jira-79: Get all data from ES - deal to issue from parts.brandid that can't be searched
    final PageRequest esPageRequest = PageRequest.of(0, SagConstants.MAX_PAGE_SIZE);
    final SearchQuery searchQuery = queryBuilder.buildQuery(criteria, esPageRequest, index());
    List<ArticleDoc> articles = new ArrayList<>();
    ArticleFilteringResponse response = filterLoop(new SearchQuery[] {searchQuery}, filteringExtractor(esPageRequest));

    response.getArticles().stream().forEach(article -> {
      // jira-79: by pass itself
      if (StringUtils.equalsIgnoreCase(artNumber, article.getArtnr())
          && StringUtils.equalsIgnoreCase(brandId, article.getIdDlnr())) {
        return;
      }
      article.getParts().forEach(artPart -> {
        // jira-79: filter by part type, part number and part brand
        if ((StringUtils.equalsIgnoreCase(ArticlePartType.IAM.name(), artPart.getPtype())
            || StringUtils.equalsIgnoreCase(ArticlePartType.PCC.name(), artPart.getPtype()))
            && StringUtils.equalsIgnoreCase(artNumber, artPart.getPnrn())
            && StringUtils.equalsIgnoreCase(brandId, artPart.getBrandid())) {
          articles.add(article);
          return;
        }
      });
    });
    List<ArticleDoc> updatedArticles = articles.stream()
        .filter(SagCollectionUtils.distinctByKeys(ArticleDoc::getIdSagsys)).collect(Collectors.toList());
    response.setArticles(new PageImpl<>(updatedArticles, pageable, updatedArticles.size()));
    return response;
  }
}
