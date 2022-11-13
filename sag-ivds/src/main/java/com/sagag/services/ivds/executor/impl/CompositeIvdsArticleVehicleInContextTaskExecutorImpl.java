package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.executor.IvdsArticleVehicleInContextTaskExecutor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Primary
public class CompositeIvdsArticleVehicleInContextTaskExecutorImpl
    implements IvdsArticleVehicleInContextTaskExecutor {

  @Autowired
  private IvdsArticleTaskExecutors ivdsArtTaskExecutors;

  @Autowired(required = false)
  private List<IvdsArticleVehicleInContextTaskExecutor> ivdsArticleVehInContextTaskExecutors;

  @Override
  public List<ArticleDocDto> execute(UserInfo user, List<ArticleDocDto> articles,
      Optional<VehicleDto> vehInContextOpt, Optional<AdditionalSearchCriteria> additionalCriteria,
      Object... args) {
    if (CollectionUtils.isEmpty(ivdsArticleVehInContextTaskExecutors)) {
      return ivdsArtTaskExecutors.executeTaskWithoutErp(user, articles, vehInContextOpt);
    }

    final IvdsArticleVehicleInContextTaskExecutor executor;
    if (ArrayUtils.contains(args, Boolean.TRUE)
        && ivdsArticleVehInContextTaskExecutors.size() > 1) {
      executor = ivdsArticleVehInContextTaskExecutors.get(1);
    } else {
      executor = ivdsArticleVehInContextTaskExecutors.get(0);
    }

    return executor.execute(user, articles, vehInContextOpt, additionalCriteria);
  }

}
