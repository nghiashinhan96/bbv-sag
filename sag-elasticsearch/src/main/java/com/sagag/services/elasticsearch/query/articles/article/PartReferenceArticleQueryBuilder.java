package com.sagag.services.elasticsearch.query.articles.article;

import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.criteria.article.PartReferenceSearchCriteria;
import com.sagag.services.elasticsearch.enums.IAttributePath;
import com.sagag.services.elasticsearch.enums.Index;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.ArticleQueryUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PartReferenceArticleQueryBuilder
    extends AbstractArticleQueryBuilder<PartReferenceSearchCriteria> {

  @Override
  public SearchQuery buildQuery(PartReferenceSearchCriteria criteria, Pageable pageable,
      String... indices) {
    final List<String> prnrs = criteria.getPartNrs();
    final boolean isUsePartsExt = criteria.isUsePartsExt();
    final List<String> lowercasePrnrs =
        prnrs.stream().map(StringUtils::lowerCase).collect(Collectors.toList());

    final IAttributePath partArticleField =
        isUsePartsExt ? Index.Article.PARTS_EX_PNRN : Index.Article.PNRN;
    final BoolQueryBuilder queryBuilder = applyCommonQueryBuilder(criteria.getAffNameLocks(), criteria.isSaleOnBehalf())
        .apply(partQueryBuilder(lowercasePrnrs, partArticleField));

    final NativeSearchQueryBuilder searchQueryBuilder =
        ArticleQueryUtils.nativeQueryBuilder(PageUtils.MAX_PAGE, indices);
    searchQueryBuilder.withQuery(queryBuilder);
    return searchQueryBuilder.build();
  }

  private BoolQueryBuilder partQueryBuilder(List<String> lowercasePrnrs, IAttributePath field) {
    return QueryBuilders.boolQuery().must(
        QueryBuilders.nestedQuery(field.path(),
            QueryBuilders.boolQuery().must(
                QueryBuilders.termsQuery(field.fullQField(), lowercasePrnrs)), ScoreMode.None));
  }

}
