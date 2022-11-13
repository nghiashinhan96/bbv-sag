package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.api.impl.articles.AbstractArticleElasticsearchService;
import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.api.impl.articles.IArticleSearchService;
import com.sagag.services.elasticsearch.api.impl.articles.KeywordArticleSearchService;
import com.sagag.services.elasticsearch.criteria.article.ArticleIdListSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.PartNumberListSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.PartReferenceAndGaIdListSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.PartReferenceSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.query.articles.article.ArticleNumberDeepLinkQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.ArticleUmarIdListArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.PartNumberListArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.PartReferenceAndGaIdListArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.PartReferenceArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.wsp.UniversalPartArticleListQueryBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

/**
 * Article Elasticsearch service implementation class.
 */
@Service
@Slf4j
public class ArticleSearchServiceImpl extends AbstractArticleElasticsearchService
  implements ArticleSearchService {

  @Autowired
  @Qualifier("idPimArticleSearchServiceImpl")
  private KeywordArticleSearchService idPimArticleSearchService;

  @Autowired
  @Qualifier("OEAndArtNumArticleSearchServiceImpl")
  private ArticleLoopSearchService oeAndArtNumArticleSearchService;

  @Autowired
  @Qualifier("freetextArticleSearchServiceImpl")
  private ArticleLoopSearchService freetextArticleSearchService;

  @Autowired
  private PartReferenceArticleQueryBuilder partReferenceArticleQueryBuilder;

  @Autowired
  private ArticleNumberDeepLinkQueryBuilder articleNumberDeepLinkQueryBuilder;

  @Autowired
  @Qualifier("articleIdListSearchServiceImpl")
  private IArticleSearchService<ArticleIdListSearchCriteria, Page<ArticleDoc>> articleIdListSearchService;

  @Autowired
  private ArticleUmarIdListArticleQueryBuilder articleUmarIdListArticleQueryBuilder;

  @Autowired
  private PartReferenceAndGaIdListArticleQueryBuilder partReferenceAndGaIdListArticleQueryBuilder;

  @Autowired
  private UniversalPartArticleListQueryBuilder universalPartArticleListQueryBuilder;

  @Autowired
  private PartNumberListArticleQueryBuilder partNumberListArticleQueryBuilder;

  @Override
  public Page<ArticleDoc> searchArticlesByNumber(final KeywordArticleSearchCriteria criteria,
      final Pageable pageable) {
    log.debug("searching articles by number = {}", criteria.getText());
    if (StringUtils.isBlank(criteria.getText())) {
      return Page.empty();
    }

    return oeAndArtNumArticleSearchService.search(criteria, pageable);
  }

  @Override
  @LogExecutionTime
  public Page<ArticleDoc> searchArticlesByIdSagSys(String idSagsys, Pageable pageable,
      boolean isSaleOnBehalf, String... affNameLocks) {
    log.debug("Searching articles by id = {}, pageable = {}", idSagsys, pageable);
    if (StringUtils.isBlank(idSagsys)) {
      return Page.empty();
    }
    final KeywordArticleSearchCriteria criteria = new KeywordArticleSearchCriteria(
        idSagsys, affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    return idPimArticleSearchService.search(criteria, pageable);
  }

  @Override
  public Page<ArticleDoc> searchArticlesByFreeText(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    log.debug("Searching articles by free text = {}", criteria.getText());
    if (StringUtils.isBlank(criteria.getText())) {
      return Page.empty();
    }
    return freetextArticleSearchService.search(criteria, pageable);
  }

  @Override
  public List<ArticleDoc> searchArticlesByIdSagSyses(List<String> ids, boolean isSaleOnBehalf,
      String... affNameLocks) {
    if (CollectionUtils.isEmpty(ids)) {
      return Collections.emptyList();
    }
    return searchArticlesByIdSagSyses(ids, PageUtils.defaultPageable(CollectionUtils.size(ids)),
        isSaleOnBehalf, affNameLocks).getContent();
  }

  @Override
  public List<ArticleDoc> searchArticlesByPartRefs(final List<String> prnrs,
      final boolean isSaleOnBehalf, final String... affNameLocks) {
    log.debug("Searching articles from part references = {}", prnrs);
    if (CollectionUtils.isEmpty(prnrs)) {
      return Collections.emptyList();
    }

    final PartReferenceSearchCriteria criteria =
        PartReferenceSearchCriteria.builder().partNrs(prnrs).build();
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);

    final List<SearchQuery> searchQueries = new ArrayList<>();
    searchQueries.add(partRefArticleQueryBuilder().apply(criteria));

    // Build queries with parts_ext list
    criteria.setUsePartsExt(true);
    searchQueries.add(partRefArticleQueryBuilder().apply(criteria));

    return searchListLoop(searchQueries.toArray(new SearchQuery[] {}), ArticleDoc.class);
  }

  private Function<PartReferenceSearchCriteria, SearchQuery> partRefArticleQueryBuilder() {
    return criteria -> partReferenceArticleQueryBuilder.buildQuery(criteria, PageUtils.MAX_PAGE,
        index());
  }

  @Override
  public Page<ArticleDoc> searchArticlesByUmarIds(Set<String> umarIds, final Pageable pageable,
      final String... affNameLocks) {
    log.debug("Searching articles by the list of umarIds = {}", umarIds);
    if (CollectionUtils.isEmpty(umarIds)) {
      return Page.empty();
    }

    final ArticleIdListSearchCriteria criteria = new ArticleIdListSearchCriteria();
    criteria.setArticleIdList(umarIds.stream().collect(Collectors.toList()));
    criteria.setAffNameLocks(affNameLocks);
    final SearchQuery query =
      articleUmarIdListArticleQueryBuilder.buildQuery(criteria, pageable, index());
    return searchPage(query, ArticleDoc.class);
  }

  @Override
  public Page<ArticleDoc> searchArticlesByIdSagSyses(List<String> idSagSyses, Pageable pageable,
      String... affNameLocks) {
    final ArticleIdListSearchCriteria criteria = new ArticleIdListSearchCriteria();
    criteria.setArticleIdList(idSagSyses);
    criteria.setAffNameLocks(affNameLocks);
    return articleIdListSearchService.search(criteria, pageable);
  }

  @Override
  public Page<ArticleDoc> searchArticleByNumberDeepLink(String articleNr, Pageable pageable,
      String... affNameLocks) {
    log.debug("Searching articles by number = {}", articleNr);
    if (StringUtils.isBlank(articleNr)) {
      return Page.empty();
    }

    final KeywordArticleSearchCriteria criteria =
      new KeywordArticleSearchCriteria(articleNr, affNameLocks);
    final SearchQuery query =
        articleNumberDeepLinkQueryBuilder.buildQuery(criteria, pageable, index());
    return searchPage(query, ArticleDoc.class);
  }

  @Override
  public List<ArticleDoc> searchArticlesByPartRefsAndGaIds(List<String> prnrs, List<String> gaIds,
    String... affNameLocks) {
    log.debug("Searching articles from part references = {} and gaids = {}", prnrs, gaIds);
    if (CollectionUtils.isEmpty(prnrs) || CollectionUtils.isEmpty(gaIds)) {
      return Collections.emptyList();
    }
    final PartReferenceAndGaIdListSearchCriteria criteria =
        new PartReferenceAndGaIdListSearchCriteria();
    criteria.setGaIds(gaIds);
    criteria.setPrnrs(prnrs);
    criteria.setAffNameLocks(affNameLocks);

    final SearchQuery query =
      partReferenceAndGaIdListArticleQueryBuilder.buildQuery(criteria, PageUtils.MAX_PAGE, index());

    return searchList(query, ArticleDoc.class);
  }

  @Override
  public List<ArticleDoc> searchArticleByPartNumbersAndSupplier(String[] partNrs, String supplier, boolean isSaleOnBehalf,
      String... affNameLocks) {
    if (ArrayUtils.isEmpty(partNrs)) {
      return Collections.emptyList();
    }
    final PartNumberListSearchCriteria criteria =
        new PartNumberListSearchCriteria(partNrs, supplier, affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    final SearchQuery searchQuery = partNumberListArticleQueryBuilder.buildQuery(criteria,
        PageUtils.MAX_PAGE, index());
    return searchList(searchQuery, ArticleDoc.class);
  }

  @Override
  public Page<ArticleDoc> searchArticlesByIdSagSyses(List<String> idSagSyses, Pageable pageable,
      boolean isSaleOnBehalf, String... affNameLocks) {
    final ArticleIdListSearchCriteria criteria = new ArticleIdListSearchCriteria();
    criteria.setArticleIdList(idSagSyses);
    criteria.setAffNameLocks(affNameLocks);
    criteria.setSaleOnBehalf(isSaleOnBehalf);
    return articleIdListSearchService.search(criteria, pageable);
  }

  @Override
  public List<ArticleDoc> searchArticlesByUniversalPart(
      UniversalPartArticleSearchCriteria criteria) {
    log.debug("Searching articles from universal part = {}", criteria);

    final SearchQuery query =
        universalPartArticleListQueryBuilder.buildQuery(criteria, PageUtils.MAX_PAGE, index());

    return searchList(query, ArticleDoc.class);
  }

}
