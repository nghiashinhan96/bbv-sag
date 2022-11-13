package com.sagag.services.ivds.executor;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;

import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IvdsArticleAmountTaskExecutor {

  Page<ArticleDocDto> execute(UserInfo user, String idSagSys,
      Integer amount, Optional<Integer> finalCustomerId);
}
