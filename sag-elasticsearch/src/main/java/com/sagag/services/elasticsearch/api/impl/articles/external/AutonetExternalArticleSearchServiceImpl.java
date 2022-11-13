package com.sagag.services.elasticsearch.api.impl.articles.external;

import com.sagag.services.elasticsearch.criteria.article.KeywordExternalArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.multisearch.AutonetArticleMultiSearchExecutor;
import com.sagag.services.elasticsearch.profiles.AnArtExternalArticleServiceMode;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Service
@AnArtExternalArticleServiceMode
public class AutonetExternalArticleSearchServiceImpl extends ExternalArticleSearchService {

  private static final boolean DISABLE_IGNORE_GEN_ART_MATCH_STEP = true;

  private static final String AN_ARTICLE_EXTERNAL_INDEX = "an_article_external";

  private static final String[] AN_ARTICLE_EXTERNAL_SEARCH_FIELDS = { "artnr", "artnr_display" };

  @Autowired
  private AutonetArticleMultiSearchExecutor autonetArticleMultiSearchExecutor;

  @Override
  public Page<ArticleDoc> search(KeywordExternalArticleSearchCriteria criteria, Pageable pageable) {
    final List<Supplier<Page<ArticleDoc>>> suppliers = new ArrayList<>();
    for (SearchQuery query : criteria.getSearchQueries()) {
      suppliers.add(articleSearchSupplier(query, criteria.getText(), pageable));
    }
    suppliers.add(() -> {
      criteria.setDisableIgnoreGenArtMatch(DISABLE_IGNORE_GEN_ART_MATCH_STEP);
      return super.search(criteria, pageable);
    });
    return processSearchLoop(suppliers, Page::hasContent, Page::empty);
  }

  private Supplier<Page<ArticleDoc>> articleSearchSupplier(SearchQuery searchQuery,
    String text, Pageable pageable) {
    return () -> {
      SearchQuery[] queries = buildAnArticleExternalQuery(new SearchQuery[] { searchQuery },
        text, pageable);
      SearchResponse[] responses = autonetArticleMultiSearchExecutor.execute(queries, pageable);
      final DefaultResultMapper resultMapper = new DefaultResultMapper();
      return IntStream.range(0, responses.length).mapToObj(index -> responses[index])
        .map(searchResponse -> resultMapper.mapResults(searchResponse, ArticleDoc.class, pageable))
        .filter(Page::hasContent).findFirst()
        .orElseGet(() -> new AggregatedPageImpl<>(Collections.emptyList()));
    };
  }

  @Override
  public ArticleFilteringResponse filter(KeywordExternalArticleSearchCriteria criteria,
    Pageable pageable) {
    criteria.onAggregated();
    final List<Supplier<ArticleFilteringResponse>> suppliers = new ArrayList<>();
    for (SearchQuery query : criteria.getSearchQueries()) {
      suppliers.add(articleFilteringSupplier(query, criteria.getText(), pageable));
    }
    suppliers.add(() -> {
      criteria.setDisableIgnoreGenArtMatch(DISABLE_IGNORE_GEN_ART_MATCH_STEP);
      return super.filter(criteria, pageable);
    });
    return processSearchLoop(suppliers, ArticleFilteringResponse::hasContent,
      ArticleFilteringResponse::empty);
  }

  private Supplier<ArticleFilteringResponse> articleFilteringSupplier(SearchQuery searchQuery,
    String text, Pageable pageable) {
    return () -> {
      SearchQuery[] queries = buildAnArticleExternalQuery(new SearchQuery[] { searchQuery },
        text, pageable);
      SearchResponse[] responses = autonetArticleMultiSearchExecutor.execute(queries, pageable);
      return IntStream.range(0, responses.length)
        .mapToObj(index -> filteringExtractor(pageable).extract(responses[index]))
        .filter(ArticleFilteringResponse::hasContent).findFirst()
        .orElseGet(ArticleFilteringResponse::empty);
    };
  }

  private static SearchQuery[] buildAnArticleExternalQuery(SearchQuery[] queries, String text,
    Pageable pageable) {
    final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
      .must(QueryBuilders.multiMatchQuery(text, AN_ARTICLE_EXTERNAL_SEARCH_FIELDS));
    final NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
      .withPageable(pageable).withIndices(AN_ARTICLE_EXTERNAL_INDEX)
      .withQuery(queryBuilder);
    return ArrayUtils.add(queries, searchQueryBuilder.build());
  }

  private <T> T processSearchLoop(List<Supplier<T>> suppliers, Predicate<T> resultValidator,
    Supplier<T> defaultVal) {
    T result;
    for (Supplier<T> supplier : suppliers) {
      result = supplier.get();
      if (resultValidator.test(result)) {
        return result;
      }
    }
    return defaultVal.get();
  }
}
