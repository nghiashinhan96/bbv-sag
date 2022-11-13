package com.sagag.services.ivds.executor;

import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import java.util.Collection;
import java.util.Optional;

public interface IvdsArticleElasticsearchTaskExecutor {

  /**
   * Returns the updated elastic-search article info.
   *
   * @param articles
   * @param vehicle
   * @param companyName
   */
  void executeTask(Collection<ArticleDocDto> articles, Optional<VehicleDto> vehicle,
      SupportedAffiliate affiliate);

}
