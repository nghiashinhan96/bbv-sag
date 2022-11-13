package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageArea;
import com.sagag.services.copydb.domain.dest.DestMessageArea;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAreaCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageAreaProcessor messageAreaProcessor;

  @Autowired
  private DestMessageAreaWriter messageAreaWriter;

  @Bean(name = "copyMessageArea")
  public Step copyMessageArea() {
    return stepBuilderFactory.get("copyMessageArea").<MessageArea, DestMessageArea>chunk(DF_CHUNK)
        .reader(messageAreaReader())
        .processor(messageAreaProcessor)
        .writer(messageAreaWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageArea> messageAreaReader() {
    final JpaPagingItemReader<MessageArea> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageArea e");
    reader.setName("messageAreaReader");
    return reader;
  }

}
