package com.sagag.services.ivds.response;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.category.GtCUPIInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GTmotiveArticleDocDto extends ArticleDocDto {

  private static final long serialVersionUID = 35637896978664371L;

  private GtCUPIInfo cupi;

}