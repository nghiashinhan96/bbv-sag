package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageVisibility;
import com.sagag.services.copydb.domain.dest.DestMessageVisibility;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageVisibilityCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageVisibilityProcessor messageVisibilityProcessor;

  @Autowired
  private DestMessageVisibilityWriter messageVisibilityWriter;

  @Bean(name = "copyMessageVisibility")
  public Step copyMessageVisibility() {
    return stepBuilderFactory.get("copyMessageVisibility").<MessageVisibility, DestMessageVisibility>chunk(DF_CHUNK)
        .reader(messageVisibilityReader())
        .processor(messageVisibilityProcessor)
        .writer(messageVisibilityWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageVisibility> messageVisibilityReader() {
    final JpaPagingItemReader<MessageVisibility> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageVisibility e");
    reader.setName("messageVisibilityReader");
    return reader;
  }

}
