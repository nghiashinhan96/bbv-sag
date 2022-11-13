package com.sagag.services.ax.availability.filter;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AxConArticleSearchCriteria extends ArticleSearchCriteria {

  private static final long serialVersionUID = -7616958930463298656L;

  private ExternalStockInfo stockInfo;

}
