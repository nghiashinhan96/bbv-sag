package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.wsp.UniversalPartArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ArticleSearchService {

  /**
   * Returns the {@link ArticleDoc} list from its number.
   *
   * <p>
   * With one article number, the service can return more than one article.
   *
   * @param criteria the search criteria
   * @param pageable the searching page request
   * @return a list of {@link ArticleDoc}
   */
  Page<ArticleDoc> searchArticlesByNumber(KeywordArticleSearchCriteria criteria, Pageable pageable);

  /**
   * Returns the articles by number for deep link.
   *
   * @param articleNr the searching article number
   * @param pageable the searching page request
   * @return a list of {@link ArticleDoc}
   */
  Page<ArticleDoc> searchArticleByNumberDeepLink(String articleNr, Pageable pageable,
      String... affNameLocks);

  /**
   * Returns the {@link ArticleDoc} list from its idSagSys.
   *
   * <p>
   * With one idSagSys, the service can return more than one article.
   *
   * @param idSagSys the searching idSagSys
   * @param isSaleOnBehalf
   * @param pageable the searching page request
   * @return a list of {@link ArticleDoc}
   */
  Page<ArticleDoc> searchArticlesByIdSagSys(String idSagSys, Pageable pageable, boolean isSaleOnBehalf, String... affNameLocks);

  /**
   * Returns the {@link ArticleDoc} list from its idSagSyses.
   *
   * <p>
   * With one idSagSys, the service can return more than one article.
   *
   * @param idSagSyses the list of searching idSagSys
   * @param isSaleOnBehalf
   * @param pageable the searching page request
   * @return a list of {@link ArticleDoc}
   */
  Page<ArticleDoc> searchArticlesByIdSagSyses(List<String> idSagSyses, Pageable pageable, boolean isSaleOnBehalf,
      String... affNameLocks);

  /**
   * Returns the {@link ArticleDoc} list from its idSagSyses.
   *
   * <p>
   * With one idSagSys, the service can return more than one article.
   *
   * @param idSagSyses the list of searching idSagSys
   * @param pageable the searching page request
   * @return a list of {@link ArticleDoc}
   */
  Page<ArticleDoc> searchArticlesByIdSagSyses(List<String> idSagSyses, Pageable pageable,
      String... affNameLocks);

  /**
   * Returns all articles found by free text search.
   *
   * @param freeText the free text to search on multifield
   * @param pageable the searching page request
   * @return a list of {@link ArticleDoc}
   */
  Page<ArticleDoc> searchArticlesByFreeText(KeywordArticleSearchCriteria criteria, Pageable pageable);

  /**
   * Returns all available articles in Elasticsearch server.
   *
   * @param ids the list of article id
   * @param isSaleOnBehalf
   * @return the list of {@link ArticleDoc}
   */
  List<ArticleDoc> searchArticlesByIdSagSyses(List<String> ids, boolean isSaleOnBehalf, String... affNameLocks);

  /**
   * Returns the articles in a list of part number by OEM type.
   *
   * @param prnrs the part reference numbers.
   * @param isSaleOnBehalf
   * @return the list of articles found.
   */
  List<ArticleDoc> searchArticlesByPartRefs(List<String> prnrs, boolean isSaleOnBehalf, String... affNameLocks);

  /**
   * search articles by the list of UMARIDs.
   *
   * @param umarIds {@link Set<String>} the list of umarids for searching
   * @param pageable the searching page request
   * @return the list of {@link ArticleDoc} with pageable
   */
  Page<ArticleDoc> searchArticlesByUmarIds(Set<String> umarIds, Pageable pageable, String... affNameLocks);

  /**
   * Returns the articles in a list of part number by OEM type and gaIds.
   *
   * @param prnrs the part reference numbers.
   * @param gaIds the genart id list.
   * @return the list of articles found.
   */
  List<ArticleDoc> searchArticlesByPartRefsAndGaIds(List<String> prnrs, List<String> gaIds,
    String... affNameLocks);

  /**
   * Returns the articles by number list.
   *
   * @param partNrs the searching part numbers
   * @param supplier the supplier
   * @param isSaleOnBehalf
   * @param affNameLocks the locked affiliates
   * @return a list of {@link ArticleDoc}
   */
  List<ArticleDoc> searchArticleByPartNumbersAndSupplier(String[] partNrs, String supplier, boolean isSaleOnBehalf,
      String... affNameLocks);

  /**
   * Returns the articles by universal part criteria.
   *
   * @param criteria the searching by universal part criteria {@link UniversalPartArticleSearchCriteria}
   * @return a list of {@link ArticleDoc}
   */
  List<ArticleDoc> searchArticlesByUniversalPart(UniversalPartArticleSearchCriteria criteria);
}
