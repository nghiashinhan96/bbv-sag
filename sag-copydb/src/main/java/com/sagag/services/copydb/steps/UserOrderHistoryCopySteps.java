package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.UserOrderHistory;
import com.sagag.services.copydb.domain.dest.DestUserOrderHistory;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class UserOrderHistoryCopySteps extends AbstractJobConfig {

  @Autowired
  private UserOrderHistoryProcessor userOrderHistoryProcessor;

  @Autowired
  private DestUserOrderHistoryWriter userOrderHistoryWriter;

  @Bean(name = "copyUserOrderHistory")
  public Step copyUserOrderHistory() {
    return stepBuilderFactory.get("copyUserOrderHistory").<UserOrderHistory, DestUserOrderHistory>chunk(DF_CHUNK)
        .reader(userOrderHistoryReader())
        .processor(userOrderHistoryProcessor)
        .writer(userOrderHistoryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<UserOrderHistory> userOrderHistoryReader() {
    final JpaPagingItemReader<UserOrderHistory> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from UserOrderHistory e");
    reader.setName("userOrderHistoryReader");
    return reader;
  }

}
