package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.FinalCustomerProperty;
import com.sagag.services.copydb.domain.dest.DestFinalCustomerProperty;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class FinalCustomerPropertyCopySteps extends AbstractJobConfig {

  @Autowired
  private FinalCustomerPropertyProcessor finalCustomerPropertyProcessor;

  @Autowired
  private DestFinalCustomerPropertyWriter finalCustomerPropertyWriter;

  @Bean(name = "copyFinalCustomerProperty")
  public Step copyFinalCustomerProperty() {
    return stepBuilderFactory.get("copyFinalCustomerProperty").<FinalCustomerProperty, DestFinalCustomerProperty>chunk(DF_CHUNK)
        .reader(finalCustomerPropertyReader())
        .processor(finalCustomerPropertyProcessor)
        .writer(finalCustomerPropertyWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<FinalCustomerProperty> finalCustomerPropertyReader() {
    final JpaPagingItemReader<FinalCustomerProperty> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from FinalCustomerProperty e");
    reader.setName("finalCustomerPropertyReader");
    return reader;
  }

}
