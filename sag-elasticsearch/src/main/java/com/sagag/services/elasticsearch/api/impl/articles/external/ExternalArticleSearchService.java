package com.sagag.services.elasticsearch.api.impl.articles.external;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.api.ProdAllDataService;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordExternalArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.ReferenceAndArtNumSearchCriteria;
import com.sagag.services.elasticsearch.domain.alldata.ProdAllDataDoc;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class ExternalArticleSearchService extends AbstractArticleElasticsearchService
  implements IArticleSearchService<KeywordExternalArticleSearchCriteria, Page<ArticleDoc>> {

  @Autowired
  private ProdAllDataService allDataService;

  @Autowired
  @Qualifier("genArtIdAndReferenceArticleSearchServiceImpl")
  protected IArticleSearchService<
    ReferenceAndArtNumSearchCriteria, Page<ArticleDoc>> genArtAndRefArtSearchService;

  @Override
  public Page<ArticleDoc> search(KeywordExternalArticleSearchCriteria criteria, Pageable pageable) {
    criteria.setDoubleLoopSearch(true);
    return searchArticlesAfterProcessAllData(criteria,
      pageable,
      criteria.isFromTextSearch(),
      (c, p) -> genArtAndRefArtSearchService.search(c, p),
      Page::empty);
  }

  @Override
  public ArticleFilteringResponse filter(KeywordExternalArticleSearchCriteria criteria,
      Pageable pageable) {
    return searchArticlesAfterProcessAllData(criteria,
      pageable,
      criteria.isFromTextSearch(),
      (c, p) -> genArtAndRefArtSearchService.filter(c, p),
      ArticleFilteringResponse::empty);
  }

  /**
   * Returns the found articles search by gaId and reference codes.
   * @param searchCriteria the search criteria
   * @param pageable the pagination
   * @return the result object
   */
  protected <T> T searchArticlesAfterProcessAllData(
    KeywordExternalArticleSearchCriteria searchCriteria,
    Pageable pageable,
    boolean fromFreetextSearch,
    BiFunction<ReferenceAndArtNumSearchCriteria, Pageable, T> searchFunction,
    Supplier<T> defaultValueSupplier) {

    // Process loop search business
    final List<ProdAllDataDoc> allDataDocs = allDataService.searchAllDataByText(
      searchCriteria.getText());
    log.debug("The size of all data documents = {}", allDataDocs.size());
    final Map<String, Set<String>> referenceAndGenArtMap = new HashMap<>();
    final List<String> articleNrs = new ArrayList<>();
    allDataDocs.stream().forEach(allDocConsumer(referenceAndGenArtMap, articleNrs));
    if (CollectionUtils.isEmpty(articleNrs) && MapUtils.isEmpty(referenceAndGenArtMap)) {
      return defaultValueSupplier.get();
    }

    final ReferenceAndArtNumSearchCriteria criteria = SagBeanUtils.map(searchCriteria,
      ReferenceAndArtNumSearchCriteria.class);
    criteria.setArticleNrs(articleNrs);
    criteria.setReferenceByGenArtIdMap(referenceAndGenArtMap);
    criteria.setFromFreetextSearch(fromFreetextSearch);
    criteria.setUseGenArtIdInQuery(true);
    criteria.setDisableIgnoreGenArtMatch(searchCriteria.isDisableIgnoreGenArtMatch());
    log.debug("Search Criteria = {}\nReferenceAndArtNum Criteria = {}", searchCriteria, criteria);
    return searchFunction.apply(criteria, pageable);
  }

  private static Consumer<ProdAllDataDoc> allDocConsumer(
    final Map<String, Set<String>> referenceAndGenArtMap, final List<String> articleNrs) {
    return document -> {
      Optional.ofNullable(document.getOriginalArticleNumber())
        .filter(StringUtils::isNotBlank)
        .ifPresent(articleNrs::add);
      referenceAndGenArtMap.compute(
        StringUtils.defaultIfBlank(document.getGaId(), StringUtils.EMPTY),
        referenceAndGenArtMapComputer(document));
    };
  }

  private static BiFunction<String, Set<String>, Set<String>> referenceAndGenArtMapComputer(
    final ProdAllDataDoc document) {
    return (gaId, references) -> {
      references = Optional.ofNullable(references).filter(CollectionUtils::isNotEmpty)
        .orElseGet(HashSet::new);
      Optional.ofNullable(document.getReferenceNumbers()).filter(CollectionUtils::isNotEmpty)
        .ifPresent(references::addAll);
      return references;
    };
  }
}
