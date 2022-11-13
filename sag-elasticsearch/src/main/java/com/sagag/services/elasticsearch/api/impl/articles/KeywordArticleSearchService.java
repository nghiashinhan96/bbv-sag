package com.sagag.services.elasticsearch.api.impl.articles;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;

import org.springframework.data.domain.Page;

public abstract class KeywordArticleSearchService extends AbstractArticleElasticsearchService
    implements IArticleSearchService<KeywordArticleSearchCriteria, Page<ArticleDoc>> {

}
