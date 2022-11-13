package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopFunction;
import com.sagag.services.copydb.domain.dest.DestEshopFunction;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopFunctionCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopFunctionProcessor eshopFunctionProcessor;

  @Autowired
  private DestEshopFunctionWriter eshopFunctionWriter;

  @Bean(name = "copyEshopFunction")
  public Step copyEshopFunction() {
    return stepBuilderFactory.get("copyEshopFunction").<EshopFunction, DestEshopFunction>chunk(DF_CHUNK)
        .reader(eshopFunctionReader())
        .processor(eshopFunctionProcessor)
        .writer(eshopFunctionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopFunction> eshopFunctionReader() {
    final JpaPagingItemReader<EshopFunction> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopFunction e");
    reader.setName("eshopFunctionReader");
    return reader;
  }

}
