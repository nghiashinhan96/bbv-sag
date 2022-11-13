package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FinalCustomerOrder;
import com.sagag.services.copydb.domain.dest.DestFinalCustomerOrder;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FinalCustomerOrderCopySteps extends AbstractJobConfig {

  @Autowired
  private FinalCustomerOrderProcessor finalCustomerOrderProcessor;

  @Autowired
  private DestFinalCustomerOrderWriter finalCustomerOrderWriter;

  @Bean(name = "copyFinalCustomerOrder")
  public Step copyFinalCustomerOrder() {
    return stepBuilderFactory.get("copyFinalCustomerOrder").<FinalCustomerOrder, DestFinalCustomerOrder>chunk(DF_CHUNK)
        .reader(finalCustomerOrderReader())
        .processor(finalCustomerOrderProcessor)
        .writer(finalCustomerOrderWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FinalCustomerOrder> finalCustomerOrderReader() {
    final JpaPagingItemReader<FinalCustomerOrder> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FinalCustomerOrder e");
    reader.setName("finalCustomerOrderReader");
    return reader;
  }

}
