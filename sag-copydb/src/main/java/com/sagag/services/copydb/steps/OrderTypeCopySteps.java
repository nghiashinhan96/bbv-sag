package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrderType;
import com.sagag.services.copydb.domain.dest.DestOrderType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrderTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private OrderTypeProcessor orderTypeProcessor;

  @Autowired
  private DestOrderTypeWriter orderTypeWriter;

  @Bean(name = "copyOrderType")
  public Step copyOrderType() {
    return stepBuilderFactory.get("copyOrderType").<OrderType, DestOrderType>chunk(DF_CHUNK)
        .reader(orderTypeReader())
        .processor(orderTypeProcessor)
        .writer(orderTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OrderType> orderTypeReader() {
    final JpaPagingItemReader<OrderType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OrderType e");
    reader.setName("orderTypeReader");
    return reader;
  }

}
