package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ShopArticle;
import com.sagag.services.copydb.domain.dest.DestShopArticle;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ShopArticleCopySteps extends AbstractJobConfig {

  @Autowired
  private ShopArticleProcessor shopArticleProcessor;

  @Autowired
  private DestShopArticleWriter shopArticleWriter;

  @Bean(name = "copyShopArticle")
  public Step copyShopArticle() {
    return stepBuilderFactory.get("copyShopArticle").<ShopArticle, DestShopArticle>chunk(DF_CHUNK)
        .reader(shopArticleReader())
        .processor(shopArticleProcessor)
        .writer(shopArticleWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<ShopArticle> shopArticleReader() {
    final JpaPagingItemReader<ShopArticle> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from ShopArticle e");
    reader.setName("shopArticleReader");
    return reader;
  }

}
