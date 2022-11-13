package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrderHistory;
import com.sagag.services.copydb.domain.dest.DestOrderHistory;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrderHistoryCopySteps extends AbstractJobConfig {

  @Autowired
  private OrderHistoryProcessor orderHistoryProcessor;

  @Autowired
  private DestOrderHistoryWriter orderHistoryWriter;

  @Bean(name = "copyOrderHistory")
  public Step copyOrderHistory() {
    return stepBuilderFactory.get("copyOrderHistory").<OrderHistory, DestOrderHistory>chunk(DF_CHUNK)
        .reader(orderHistoryReader())
        .processor(orderHistoryProcessor)
        .writer(orderHistoryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrderHistory> orderHistoryReader() {
    final JpaPagingItemReader<OrderHistory> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrderHistory e");
    reader.setName("orderHistoryReader");
    return reader;
  }

}
