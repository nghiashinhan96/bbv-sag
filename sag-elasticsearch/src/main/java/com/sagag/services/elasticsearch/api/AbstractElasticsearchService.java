package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.indices.EsIndexAliasMapper;
import com.sagag.services.elasticsearch.indices.IndexAliasKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.IndexNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.Assert;

/**
 * Abstract elasticsearch service to inherits common properties and apis.
 */
@Slf4j
public abstract class AbstractElasticsearchService implements IndexAliasKey {

  private static final String LOG_NOT_FOUND_INDEX_MSG = "Not found index = {}";

  @Autowired
  private ElasticsearchTemplate esTemplate;

  @Autowired
  private EsIndexAliasMapper defaultEsIndexAliasMapper;

  /**
   * Returns Elasticsearch template.
   *
   * @return the {@link ElasticsearchTemplate}
   */
  protected ElasticsearchTemplate getEsTemplate() {
    return esTemplate;
  }

  protected Client getEsClient() {
    return getEsTemplate().getClient();
  }

  protected <T> T search(final SearchQuery searchQuery, final ResultsExtractor<T> extractor) {
    return getEsTemplate().query(searchQuery, extractor);
  }

  protected <T> T searchOrDefault(final SearchQuery searchQuery,
      final ResultsExtractor<T> extractor, final T defaultValue) {
    return searchOrDefault(searchQuery, extractor, buildDefaultValueSupplier(defaultValue));
  }

  private static <T> Supplier<T> buildDefaultValueSupplier(final T defaultValue) {
    return () -> defaultValue;
  }

  protected <T> T searchOrDefault(final SearchQuery searchQuery,
      final ResultsExtractor<T> extractor, final Supplier<T> defaultValueSupplier) {
    try {
      Supplier<T> searchSupplier = () -> search(searchQuery, extractor);
      return searchDefault(searchSupplier, defaultValueSupplier);
    } catch (ElasticsearchException ex) {
      log.warn("Got the exception from ES then return the default value, error detail: ", ex);
      return defaultValueSupplier.get();
    }
  }

  protected ArticleFilteringResponse filterLoop(final SearchQuery[] queries,
    final ResultsExtractor<ArticleFilteringResponse> extractor) {
    return searchLoop(queries,
      query -> search(query, extractor),
      result -> result != null && result.hasContent(),
      ArticleFilteringResponse::empty);
  }

  protected <T> Page<T> searchPage(final SearchQuery searchQuery, final Class<T> clazz) {
    return searchDefault(() -> getEsTemplate().queryForPage(searchQuery, clazz), Page.empty());
  }

  protected <T> Page<T> searchPageLoop(final SearchQuery[] queries, final Class<T> clazz) {
    return searchLoop(queries,
      query -> searchPage(query, clazz),
      result -> result != null && result.hasContent(),
      Page::empty);
  }

  protected <T> List<T> searchList(final SearchQuery searchQuery, final Class<T> clazz) {
    return searchDefault(() -> getEsTemplate().queryForList(searchQuery, clazz),
        Collections.emptyList());
  }

  protected <T> List<T> searchListLoop(final SearchQuery[] queries, final Class<T> clazz) {
    return searchLoop(queries,
      query -> searchList(query, clazz),
      CollectionUtils::isNotEmpty,
      Collections::emptyList);
  }

  protected <T> List<T> searchStream(final SearchQuery searchQuery, final Class<T> clazz) {
    final Supplier<List<T>> streamSupplier = () -> {
      final List<T> items = new ArrayList<>();
      getEsTemplate().stream(searchQuery, clazz).forEachRemaining(items::add);
      return items;
    };
    return searchDefault(streamSupplier, Collections.emptyList());
  }

  /**
   * Executes the search with handle some exception as default.
   *
   * @param supplier the search function will be executed
   * @param defaultVal the default value will be returned if got the caught exception.
   * @return the found result
   * @throws ElasticsearchException
   */
  private <T> T searchDefault(final Supplier<T> supplier, final T defaultVal) {
    return searchDefault(supplier, buildDefaultValueSupplier(defaultVal));
  }

  /**
   * Executes the search with handle some exception as default.
   *
   * @param supplier the search function will be executed
   * @param defaultValSupplier the default value supplier will be returned if got the caught exception.
   * @return the found result
   * @throws ElasticsearchException
   */
  private <T> T searchDefault(final Supplier<T> supplier, final Supplier<T> defaultValSupplier) {
    try {
      return supplier.get();
    } catch(IndexNotFoundException ex) {
      log.warn(LOG_NOT_FOUND_INDEX_MSG, ex.getIndex().toString());
      return defaultValSupplier.get();
    } catch (ElasticsearchException ex) {
      throw ex;
    }
  }

  /**
   * Executes the search in loop of array of queries till found the result or last query.
   *
   * @param queries the array of queries
   * @param defaultVal the default value will be returned if not found anything
   * @param searchFunction the search function will be executed
   * @param validPredicate the condition to exit the loop if valid
   * @return the found result
   */
  private <T> T searchLoop(final SearchQuery[] queries,
    final Function<SearchQuery, T> searchFunction,
    final Predicate<T> validPredicate,
    final Supplier<T> defaultVal) {
    if (ArrayUtils.isEmpty(queries)) {
      return defaultVal.get();
    }
    Assert.notNull(searchFunction, "The given search function must not be null");
    Assert.notNull(validPredicate, "The given valid predicate must not be null");

    int lengthOfQueries = ArrayUtils.getLength(queries);
    return IntStream.range(NumberUtils.INTEGER_ZERO, lengthOfQueries)
      .mapToObj(index -> searchFunction.apply(queries[index]))
      .filter(validPredicate).findFirst().orElseGet(defaultVal);
  }

  @Override
  public EsIndexAliasMapper aliasMapper() {
    return defaultEsIndexAliasMapper;
  }
}
