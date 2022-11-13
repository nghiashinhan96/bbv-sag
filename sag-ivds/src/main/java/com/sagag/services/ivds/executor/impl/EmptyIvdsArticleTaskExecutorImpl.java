package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.profiles.NoneErpProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.ivds.domain.ArticleExternalRequestOption;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@NoneErpProfile
@Slf4j
public class EmptyIvdsArticleTaskExecutorImpl implements IvdsArticleTaskExecutor {

  @Override
  public List<ArticleDocDto> executeTask(UserInfo user, List<ArticleDocDto> articles,
      Optional<VehicleDto> vehicleOpt, ArticleExternalRequestOption option) {
    log.warn("Not setup specified kind of ERP, so just return raw article information.");
    return articles;
  }

}
