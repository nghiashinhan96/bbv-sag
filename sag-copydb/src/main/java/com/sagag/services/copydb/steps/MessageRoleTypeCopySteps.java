package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageRoleType;
import com.sagag.services.copydb.domain.dest.DestMessageRoleType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageRoleTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageRoleTypeProcessor messageRoleTypeProcessor;

  @Autowired
  private DestMessageRoleTypeWriter messageRoleTypeWriter;

  @Bean(name = "copyMessageRoleType")
  public Step copyMessageRoleType() {
    return stepBuilderFactory.get("copyMessageRoleType").<MessageRoleType, DestMessageRoleType>chunk(DF_CHUNK)
        .reader(messageRoleTypeReader())
        .processor(messageRoleTypeProcessor)
        .writer(messageRoleTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageRoleType> messageRoleTypeReader() {
    final JpaPagingItemReader<MessageRoleType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageRoleType e");
    reader.setName("messageRoleTypeReader");
    return reader;
  }

}
