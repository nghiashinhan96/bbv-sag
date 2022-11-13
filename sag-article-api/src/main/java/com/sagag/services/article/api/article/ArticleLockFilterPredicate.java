package com.sagag.services.article.api.article;

import com.sagag.services.domain.article.ArticleDocDto;

import java.util.function.Predicate;

public interface ArticleLockFilterPredicate extends Predicate<ArticleDocDto> {

}
