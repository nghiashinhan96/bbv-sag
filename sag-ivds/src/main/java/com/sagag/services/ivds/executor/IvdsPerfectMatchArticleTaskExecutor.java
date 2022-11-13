package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IvdsPerfectMatchArticleTaskExecutor {

  /**
   * Returns the perfect match articles.
   *
   * @param user
   * @param articles
   * @param pageable
   * @return the perfect matched articles
   */
  List<ArticleDocDto> executePerfectMatchTask(UserInfo user, Page<ArticleDocDto> articles,
      Pageable pageable, Optional<AdditionalSearchCriteria> additional);

  /**
   * Returns the perfect match price articles.
   *
   * @param user
   * @param allArticles
   * @param articles
   * @param finalCustomerOrgIdOpt
   * @param additional
   * @return the perfect matched articles
   */
  List<ArticleDocDto> executeGetPriceTask(UserInfo user, List<ArticleDocDto> allArticles,
      Page<ArticleDocDto> articles, Optional<Integer> finalCustomerOrgIdOpt,
      Optional<AdditionalSearchCriteria> additional);
}
