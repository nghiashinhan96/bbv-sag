package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopClientResource;
import com.sagag.services.copydb.domain.dest.DestEshopClientResource;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopClientResourceCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopClientResourceProcessor eshopClientResourceProcessor;

  @Autowired
  private DestEshopClientResourceWriter eshopClientResourceWriter;

  @Bean(name = "copyEshopClientResource")
  public Step copyEshopClientResource() {
    return stepBuilderFactory.get("copyEshopClientResource").<EshopClientResource, DestEshopClientResource>chunk(DF_CHUNK)
        .reader(eshopClientResourceReader())
        .processor(eshopClientResourceProcessor)
        .writer(eshopClientResourceWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopClientResource> eshopClientResourceReader() {
    final JpaPagingItemReader<EshopClientResource> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopClientResource e");
    reader.setName("eshopClientResourceReader");
    return reader;
  }

}
