package com.sagag.services.ivds.utils;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.response.ArticleInformationResponseItem;

public interface ArticleInformationExtractor {

  public ArticleInformationResponseItem buildErpInformation(ArticleDocDto art);
}
