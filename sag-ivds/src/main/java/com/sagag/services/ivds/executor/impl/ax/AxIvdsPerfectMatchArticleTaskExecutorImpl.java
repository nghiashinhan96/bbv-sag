package com.sagag.services.ivds.executor.impl.ax;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.ivds.executor.impl.AbstractIvdsPerfectMatchArticleTaskExecutor;
import com.sagag.services.ivds.filter.ArticleFilterContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AxProfile
public class AxIvdsPerfectMatchArticleTaskExecutorImpl
  extends AbstractIvdsPerfectMatchArticleTaskExecutor {

  @Autowired
  private ArticleFilterContext articleFilterContext;

  @Override
  public List<ArticleDocDto> executePerfectMatchTask(UserInfo user, Page<ArticleDocDto> articles,
      Pageable pageable, Optional<AdditionalSearchCriteria> additional) {

    return articleFilterContext.findAndSortStock(user, articles, additional);
  }

  @Override
  public List<ArticleDocDto> executeGetPriceTask(UserInfo user, List<ArticleDocDto> allArticles,
      Page<ArticleDocDto> articles, Optional<Integer> finalCustomerOrgIdOpt,
      Optional<AdditionalSearchCriteria> additional) {
    return ivdsArticleTaskExecutors.executeTaskWithPriceOnlyWithoutVehicle(user,
        articles.getContent(), finalCustomerOrgIdOpt, additional);
  }

}
