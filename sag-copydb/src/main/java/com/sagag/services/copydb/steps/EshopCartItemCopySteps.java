package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopCartItem;
import com.sagag.services.copydb.domain.dest.DestEshopCartItem;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopCartItemCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopCartItemProcessor eshopCartItemProcessor;

  @Autowired
  private DestEshopCartItemWriter eshopCartItemWriter;

  @Bean(name = "copyEshopCartItem")
  public Step copyEshopCartItem() {
    return stepBuilderFactory.get("copyEshopCartItem").<EshopCartItem, DestEshopCartItem>chunk(DF_CHUNK)
        .reader(eshopCartItemReader())
        .processor(eshopCartItemProcessor)
        .writer(eshopCartItemWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopCartItem> eshopCartItemReader() {
    final JpaPagingItemReader<EshopCartItem> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopCartItem e");
    reader.setName("eshopCartItemReader");
    return reader;
  }

}
