package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.BasketHistory;
import com.sagag.services.copydb.domain.dest.DestBasketHistory;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class BasketHistoryCopySteps extends AbstractJobConfig {

  @Autowired
  private BasketHistoryProcessor basketHistoryProcessor;

  @Autowired
  private DestBasketHistoryWriter basketHistoryWriter;

  @Bean(name = "copyBasketHistory")
  public Step copyBasketHistory() {
    return stepBuilderFactory.get("copyBasketHistory").<BasketHistory, DestBasketHistory>chunk(DF_CHUNK)
        .reader(basketHistoryReader())
        .processor(basketHistoryProcessor)
        .writer(basketHistoryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<BasketHistory> basketHistoryReader() {
    final JpaPagingItemReader<BasketHistory> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from BasketHistory e");
    reader.setName("basketHistoryReader");
    return reader;
  }

}
