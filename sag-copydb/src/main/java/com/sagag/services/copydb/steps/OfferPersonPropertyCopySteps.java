package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OfferPersonProperty;
import com.sagag.services.copydb.domain.dest.DestOfferPersonProperty;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OfferPersonPropertyCopySteps extends AbstractJobConfig {

  @Autowired
  private OfferPersonPropertyProcessor offerPersonPropertyProcessor;

  @Autowired
  private DestOfferPersonPropertyWriter offerPersonPropertyWriter;

  @Bean(name = "copyOfferPersonProperty")
  public Step copyOfferPersonProperty() {
    return stepBuilderFactory.get("copyOfferPersonProperty").<OfferPersonProperty, DestOfferPersonProperty>chunk(DF_CHUNK)
        .reader(offerPersonPropertyReader())
        .processor(offerPersonPropertyProcessor)
        .writer(offerPersonPropertyWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<OfferPersonProperty> offerPersonPropertyReader() {
    final JpaPagingItemReader<OfferPersonProperty> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from OfferPersonProperty e");
    reader.setName("offerPersonPropertyReader");
    return reader;
  }

}
