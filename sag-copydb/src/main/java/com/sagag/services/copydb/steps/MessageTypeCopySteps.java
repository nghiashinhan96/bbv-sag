package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageType;
import com.sagag.services.copydb.domain.dest.DestMessageType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageTypeProcessor messageTypeProcessor;

  @Autowired
  private DestMessageTypeWriter messageTypeWriter;

  @Bean(name = "copyMessageType")
  public Step copyMessageType() {
    return stepBuilderFactory.get("copyMessageType").<MessageType, DestMessageType>chunk(DF_CHUNK)
        .reader(messageTypeReader())
        .processor(messageTypeProcessor)
        .writer(messageTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageType> messageTypeReader() {
    final JpaPagingItemReader<MessageType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageType e");
    reader.setName("messageTypeReader");
    return reader;
  }

}
