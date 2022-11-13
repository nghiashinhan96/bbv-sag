package com.sagag.services.elasticsearch.api.impl.articles.article;

import com.sagag.services.elasticsearch.api.impl.articles.ArticleLoopSearchService;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.query.articles.article.ArticleIdArticleSearchQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.parts.EanArticleSearchQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.parts.IamArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.parts.OemArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.parts.PccArticleQueryBuilder;
import com.sagag.services.elasticsearch.utils.QueryUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class BarcodeArticleSearchServiceImpl extends ArticleLoopSearchService {

  private static final int ARTICLE_ID_LENGTH = 10;

  private static final int[] EAN_CODE_LENGTHS = new int[] { 12, 13 };

  @Autowired
  private ArticleIdArticleSearchQueryBuilder articleIdArticleSearchQueryBuilder;

  @Autowired
  private EanArticleSearchQueryBuilder eanArticleSearchQueryBuilder;

  @Autowired
  private IamArticleQueryBuilder iamArticleQueryBuilder;

  @Autowired
  private OemArticleQueryBuilder oemArticleQueryBuilder;

  @Autowired
  private PccArticleQueryBuilder pccArticleQueryBuilder;

  @Override
  public Page<ArticleDoc> search(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    if (!criteria.hasText()) {
      return Page.empty();
    }
    criteria.setSearchExternal(false);
    return searchArticlesLoop(criteria, pageable, false);
  }

  /**
   * Search article by number with supplier or gaid.
   *
   * @param criteria
   *        {@link com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria}
   *        the article criteria for searching
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
    criteria.setSearchExternal(false);
    return filterArticlesLoop(criteria, pageable, false);
  }

  @Override
  public SearchQuery[] buildLoopQueries(KeywordArticleSearchCriteria criteria, Pageable pageable) {
    final List<SearchQuery> queries = new ArrayList<>();

    // #3618: [CH/AT] Fast Scan search must also work in situations where barcodes
    // contain special characters such as blanks, dashes (-), slashes (/) and more
    criteria.setText(QueryUtils.removeNonAlphaChars(StringUtils.remove(
        criteria.getText(), StringUtils.SPACE), true));

    if (isEanFormat(criteria.getText())) {
      criteria.setUsePartsExt(false);
      queries.add(eanArticleSearchQueryBuilder.buildQuery(criteria, pageable, index()));
      criteria.setUsePartsExt(true);
      queries.add(eanArticleSearchQueryBuilder.buildQuery(criteria, pageable, index()));
    }

    if (isArticleIdFormat(criteria.getText())) {
      queries.add(articleIdArticleSearchQueryBuilder.buildQuery(criteria, pageable, index()));
    }

    criteria.setUsePartsExt(false);
    queries.add(oemArticleQueryBuilder.buildQuery(criteria, pageable, index()));
    criteria.setUsePartsExt(true);
    queries.add(oemArticleQueryBuilder.buildQuery(criteria, pageable, index()));

    criteria.setUsePartsExt(false);
    queries.add(iamArticleQueryBuilder.buildQuery(criteria, pageable, index()));
    criteria.setUsePartsExt(true);
    queries.add(iamArticleQueryBuilder.buildQuery(criteria, pageable, index()));

    criteria.setUsePartsExt(false);
    queries.add(pccArticleQueryBuilder.buildQuery(criteria, pageable, index()));
    criteria.setUsePartsExt(true);
    queries.add(pccArticleQueryBuilder.buildQuery(criteria, pageable, index()));

    return queries.toArray(new SearchQuery[0]);
  }

  private boolean isArticleIdFormat(String text) {
    return StringUtils.isNumeric(text) && text.length() == ARTICLE_ID_LENGTH
        && Long.valueOf(text) <= Integer.MAX_VALUE;
  }

  private boolean isEanFormat(String text) {
    return !StringUtils.isEmpty(text)
        && IntStream.of(EAN_CODE_LENGTHS).anyMatch(Integer.valueOf(text.length())::equals);
  }
}
