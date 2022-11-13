package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.DeliveryType;
import com.sagag.services.copydb.domain.dest.DestDeliveryType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class DeliveryTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private DeliveryTypeProcessor deliveryTypeProcessor;

  @Autowired
  private DestDeliveryTypeWriter deliveryTypeWriter;

  @Bean(name = "copyDeliveryType")
  public Step copyDeliveryType() {
    return stepBuilderFactory.get("copyDeliveryType").<DeliveryType, DestDeliveryType>chunk(DF_CHUNK)
        .reader(deliveryTypeReader())
        .processor(deliveryTypeProcessor)
        .writer(deliveryTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<DeliveryType> deliveryTypeReader() {
    final JpaPagingItemReader<DeliveryType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from DeliveryType e");
    reader.setName("deliveryTypeReader");
    return reader;
  }

}
