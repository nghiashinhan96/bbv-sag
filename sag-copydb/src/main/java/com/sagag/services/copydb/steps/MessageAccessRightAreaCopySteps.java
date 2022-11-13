package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageAccessRightArea;
import com.sagag.services.copydb.domain.dest.DestMessageAccessRightArea;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAccessRightAreaCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageAccessRightAreaProcessor messageAccessRightAreaProcessor;

  @Autowired
  private DestMessageAccessRightAreaWriter messageAccessRightAreaWriter;

  @Bean(name = "copyMessageAccessRightArea")
  public Step copyMessageAccessRightArea() {
    return stepBuilderFactory.get("copyMessageAccessRightArea").<MessageAccessRightArea, DestMessageAccessRightArea>chunk(DF_CHUNK)
        .reader(messageAccessRightAreaReader())
        .processor(messageAccessRightAreaProcessor)
        .writer(messageAccessRightAreaWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageAccessRightArea> messageAccessRightAreaReader() {
    final JpaPagingItemReader<MessageAccessRightArea> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageAccessRightArea e");
    reader.setName("messageAccessRightAreaReader");
    return reader;
  }

}
