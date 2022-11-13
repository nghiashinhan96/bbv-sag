package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLocationType;
import com.sagag.services.copydb.domain.dest.DestMessageLocationType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLocationTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageLocationTypeProcessor messageLocationTypeProcessor;

  @Autowired
  private DestMessageLocationTypeWriter messageLocationTypeWriter;

  @Bean(name = "copyMessageLocationType")
  public Step copyMessageLocationType() {
    return stepBuilderFactory.get("copyMessageLocationType").<MessageLocationType, DestMessageLocationType>chunk(DF_CHUNK)
        .reader(messageLocationTypeReader())
        .processor(messageLocationTypeProcessor)
        .writer(messageLocationTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageLocationType> messageLocationTypeReader() {
    final JpaPagingItemReader<MessageLocationType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageLocationType e");
    reader.setName("messageLocationTypeReader");
    return reader;
  }

}
