package com.sagag.services.stakis.erp.domain;

import com.sagag.services.article.api.request.AttachedArticleRequest;
import com.sagag.services.domain.article.ArticleDocDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TmAttachedArticleRequest extends AttachedArticleRequest {

  private static final long serialVersionUID = -5403215922676525252L;

  private ArticleDocDto depositArticle;

}
