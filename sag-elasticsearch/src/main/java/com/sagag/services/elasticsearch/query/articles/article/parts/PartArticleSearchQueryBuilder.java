package com.sagag.services.elasticsearch.query.articles.article.parts;

import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.enums.ArticlePartType;

import lombok.experimental.UtilityClass;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@UtilityClass
public class PartArticleSearchQueryBuilder {

  protected static BoolQueryBuilder partQueryBuilder(final String pnrn,
      ArticlePartType articlePartType, boolean isUsePartsExt) {
    ArticleField articleField = isUsePartsExt ? ArticleField.PARTS_EXT_NUMBER : ArticleField.PARTS_NUMBER;

    return QueryBuilders.boolQuery()
        .must(QueryBuilders.nestedQuery(articleField.code(), QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery(ArticleField.PART_PTYPE.value(), articlePartType.name()))
            .must(QueryBuilders.queryStringQuery(pnrn).field(articleField.value())),
            ScoreMode.None));
  }

}
