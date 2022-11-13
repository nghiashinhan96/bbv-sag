package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopClient;
import com.sagag.services.copydb.domain.dest.DestEshopClient;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopClientCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopClientProcessor eshopClientProcessor;

  @Autowired
  private DestEshopClientWriter eshopClientWriter;

  @Bean(name = "copyEshopClient")
  public Step copyEshopClient() {
    return stepBuilderFactory.get("copyEshopClient").<EshopClient, DestEshopClient>chunk(DF_CHUNK)
        .reader(eshopClientReader())
        .processor(eshopClientProcessor)
        .writer(eshopClientWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopClient> eshopClientReader() {
    final JpaPagingItemReader<EshopClient> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopClient e");
    reader.setName("eshopClientReader");
    return reader;
  }

}
