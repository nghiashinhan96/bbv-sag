package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopGroup;
import com.sagag.services.copydb.domain.dest.DestEshopGroup;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopGroupCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopGroupProcessor eshopGroupProcessor;

  @Autowired
  private DestEshopGroupWriter eshopGroupWriter;

  @Bean(name = "copyEshopGroup")
  public Step copyEshopGroup() {
    return stepBuilderFactory.get("copyEshopGroup").<EshopGroup, DestEshopGroup>chunk(DF_CHUNK)
        .reader(eshopGroupReader())
        .processor(eshopGroupProcessor)
        .writer(eshopGroupWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopGroup> eshopGroupReader() {
    final JpaPagingItemReader<EshopGroup> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopGroup e");
    reader.setName("eshopGroupReader");
    return reader;
  }

}
