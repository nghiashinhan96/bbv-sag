package com.sagag.services.ax.article;

import org.springframework.stereotype.Component;

import com.sagag.services.article.api.article.StockAndPriceArticleConsumer;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.article.ArticleDocDto;

@Component
@AxProfile
public class AxStockAndPriceArticleConsumer implements StockAndPriceArticleConsumer {

  @Override
  public void accept(ArticleDocDto article) {
    if (!article.hasNetPrice()) {
      article.setStock(null);
    }
  }

}
