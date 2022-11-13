package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ArticleHistory;
import com.sagag.services.copydb.domain.dest.DestArticleHistory;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ArticleHistoryCopySteps extends AbstractJobConfig {

  @Autowired
  private ArticleHistoryProcessor articleHistoryProcessor;

  @Autowired
  private DestArticleHistoryWriter articleHistoryWriter;

  @Bean(name = "copyArticleHistory")
  public Step copyArticleHistory() {
    return stepBuilderFactory.get("copyArticleHistory").<ArticleHistory, DestArticleHistory>chunk(DF_CHUNK)
        .reader(articleHistoryReader())
        .processor(articleHistoryProcessor)
        .writer(articleHistoryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<ArticleHistory> articleHistoryReader() {
    final JpaPagingItemReader<ArticleHistory> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from ArticleHistory e");
    reader.setName("articleHistoryReader");
    return reader;
  }

}
