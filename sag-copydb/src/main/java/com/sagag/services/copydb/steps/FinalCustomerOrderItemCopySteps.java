package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FinalCustomerOrderItem;
import com.sagag.services.copydb.domain.dest.DestFinalCustomerOrderItem;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FinalCustomerOrderItemCopySteps extends AbstractJobConfig {

  @Autowired
  private FinalCustomerOrderItemProcessor finalCustomerOrderItemProcessor;

  @Autowired
  private DestFinalCustomerOrderItemWriter finalCustomerOrderItemWriter;

  @Bean(name = "copyFinalCustomerOrderItem")
  public Step copyFinalCustomerOrderItem() {
    return stepBuilderFactory.get("copyFinalCustomerOrderItem").<FinalCustomerOrderItem, DestFinalCustomerOrderItem>chunk(DF_CHUNK)
        .reader(finalCustomerOrderItemReader())
        .processor(finalCustomerOrderItemProcessor)
        .writer(finalCustomerOrderItemWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FinalCustomerOrderItem> finalCustomerOrderItemReader() {
    final JpaPagingItemReader<FinalCustomerOrderItem> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FinalCustomerOrderItem e");
    reader.setName("finalCustomerOrderItemReader");
    return reader;
  }

}
