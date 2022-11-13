package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CollectiveDelivery;
import com.sagag.services.copydb.domain.dest.DestCollectiveDelivery;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CollectiveDeliveryCopySteps extends AbstractJobConfig {

  @Autowired
  private CollectiveDeliveryProcessor collectiveDeliveryProcessor;

  @Autowired
  private DestCollectiveDeliveryWriter collectiveDeliveryWriter;

  @Bean(name = "copyCollectiveDelivery")
  public Step copyCollectiveDelivery() {
    return stepBuilderFactory.get("copyCollectiveDelivery").<CollectiveDelivery, DestCollectiveDelivery>chunk(DF_CHUNK)
        .reader(collectiveDeliveryReader())
        .processor(collectiveDeliveryProcessor)
        .writer(collectiveDeliveryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<CollectiveDelivery> collectiveDeliveryReader() {
    final JpaPagingItemReader<CollectiveDelivery> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from CollectiveDelivery e");
    reader.setName("collectiveDeliveryReader");
    return reader;
  }

}
