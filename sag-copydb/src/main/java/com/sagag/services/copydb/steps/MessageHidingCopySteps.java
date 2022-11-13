package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageHiding;
import com.sagag.services.copydb.domain.dest.DestMessageHiding;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageHidingCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageHidingProcessor messageHidingProcessor;

  @Autowired
  private DestMessageHidingWriter messageHidingWriter;

  @Bean(name = "copyMessageHiding")
  public Step copyMessageHiding() {
    return stepBuilderFactory.get("copyMessageHiding").<MessageHiding, DestMessageHiding>chunk(DF_CHUNK)
        .reader(messageHidingReader())
        .processor(messageHidingProcessor)
        .writer(messageHidingWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageHiding> messageHidingReader() {
    final JpaPagingItemReader<MessageHiding> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageHiding e");
    reader.setName("messageHidingReader");
    return reader;
  }

}
