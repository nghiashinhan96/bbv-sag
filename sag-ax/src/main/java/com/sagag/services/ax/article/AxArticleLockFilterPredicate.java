package com.sagag.services.ax.article;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.sagag.services.article.api.article.ArticleLockFilterPredicate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;

@Component
@AxProfile
public class AxArticleLockFilterPredicate implements ArticleLockFilterPredicate {

  @Override
  public boolean test(ArticleDocDto article) {
    return Objects.isNull(article.getArticle());
  }

}
