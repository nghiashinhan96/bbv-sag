package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageAccessRight;
import com.sagag.services.copydb.domain.dest.DestMessageAccessRight;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAccessRightCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageAccessRightProcessor messageAccessRightProcessor;

  @Autowired
  private DestMessageAccessRightWriter messageAccessRightWriter;

  @Bean(name = "copyMessageAccessRight")
  public Step copyMessageAccessRight() {
    return stepBuilderFactory.get("copyMessageAccessRight").<MessageAccessRight, DestMessageAccessRight>chunk(DF_CHUNK)
        .reader(messageAccessRightReader())
        .processor(messageAccessRightProcessor)
        .writer(messageAccessRightWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageAccessRight> messageAccessRightReader() {
    final JpaPagingItemReader<MessageAccessRight> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageAccessRight e");
    reader.setName("messageAccessRightReader");
    return reader;
  }

}
