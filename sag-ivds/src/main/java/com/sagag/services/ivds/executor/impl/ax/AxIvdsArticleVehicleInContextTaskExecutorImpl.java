package com.sagag.services.ivds.executor.impl.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.executor.IvdsArticleVehicleInContextTaskExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AxProfile
@Order(1)
public class AxIvdsArticleVehicleInContextTaskExecutorImpl
    implements IvdsArticleVehicleInContextTaskExecutor {

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Override
  public List<ArticleDocDto> execute(UserInfo user, List<ArticleDocDto> articles,
      Optional<VehicleDto> vehInContextOpt, Optional<AdditionalSearchCriteria> additionalCriteria,
      Object... args) {
    return ivdsArticleTaskExecutors.executeTaskWithPriceAndStock(user, articles, vehInContextOpt,
        additionalCriteria);
  }

}
