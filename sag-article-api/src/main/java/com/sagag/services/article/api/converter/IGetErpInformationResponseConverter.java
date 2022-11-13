package com.sagag.services.article.api.converter;

import com.sagag.services.domain.article.ArticleDocDto;

import java.util.List;

public interface IGetErpInformationResponseConverter<T> {

  List<ArticleDocDto> apply(List<ArticleDocDto> articles, T response, double vatRate, String lang);
}
