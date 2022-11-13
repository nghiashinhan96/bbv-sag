package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrderStatus;
import com.sagag.services.copydb.domain.dest.DestOrderStatus;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrderStatusCopySteps extends AbstractJobConfig {

  @Autowired
  private OrderStatusProcessor orderStatusProcessor;

  @Autowired
  private DestOrderStatusWriter orderStatusWriter;

  @Bean(name = "copyOrderStatus")
  public Step copyOrderStatus() {
    return stepBuilderFactory.get("copyOrderStatus").<OrderStatus, DestOrderStatus>chunk(DF_CHUNK)
        .reader(orderStatusReader())
        .processor(orderStatusProcessor)
        .writer(orderStatusWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrderStatus> orderStatusReader() {
    final JpaPagingItemReader<OrderStatus> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrderStatus e");
    reader.setName("orderStatusReader");
    return reader;
  }

}
