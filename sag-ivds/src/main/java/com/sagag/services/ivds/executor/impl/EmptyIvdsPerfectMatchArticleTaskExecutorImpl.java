package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.profiles.NoneErpProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.executor.IvdsPerfectMatchArticleTaskExecutor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@NoneErpProfile
public class EmptyIvdsPerfectMatchArticleTaskExecutorImpl
  implements IvdsPerfectMatchArticleTaskExecutor {

  @Override
  public List<ArticleDocDto> executePerfectMatchTask(UserInfo user, Page<ArticleDocDto> articles,
      Pageable pageable, Optional<AdditionalSearchCriteria> additional) {
    return articles.getContent();
  }

  @Override
  public List<ArticleDocDto> executeGetPriceTask(UserInfo user, List<ArticleDocDto> allArticles,
      Page<ArticleDocDto> articles, Optional<Integer> finalCustomerOrgIdOpt,
      Optional<AdditionalSearchCriteria> additional) {
    return articles.getContent();
  }

}
