package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageStyle;
import com.sagag.services.copydb.domain.dest.DestMessageStyle;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageStyleCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageStyleProcessor messageStyleProcessor;

  @Autowired
  private DestMessageStyleWriter messageStyleWriter;

  @Bean(name = "copyMessageStyle")
  public Step copyMessageStyle() {
    return stepBuilderFactory.get("copyMessageStyle").<MessageStyle, DestMessageStyle>chunk(DF_CHUNK)
        .reader(messageStyleReader())
        .processor(messageStyleProcessor)
        .writer(messageStyleWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageStyle> messageStyleReader() {
    final JpaPagingItemReader<MessageStyle> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageStyle e");
    reader.setName("messageStyleReader");
    return reader;
  }

}
