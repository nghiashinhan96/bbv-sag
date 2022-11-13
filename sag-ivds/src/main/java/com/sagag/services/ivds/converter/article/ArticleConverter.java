package com.sagag.services.ivds.converter.article;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;

import java.util.function.Function;

public interface ArticleConverter extends Function<ArticleDoc, ArticleDocDto> {

}
