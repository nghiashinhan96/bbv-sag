package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageSubArea;
import com.sagag.services.copydb.domain.dest.DestMessageSubArea;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageSubAreaCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageSubAreaProcessor messageSubAreaProcessor;

  @Autowired
  private DestMessageSubAreaWriter messageSubAreaWriter;

  @Bean(name = "copyMessageSubArea")
  public Step copyMessageSubArea() {
    return stepBuilderFactory.get("copyMessageSubArea").<MessageSubArea, DestMessageSubArea>chunk(DF_CHUNK)
        .reader(messageSubAreaReader())
        .processor(messageSubAreaProcessor)
        .writer(messageSubAreaWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageSubArea> messageSubAreaReader() {
    final JpaPagingItemReader<MessageSubArea> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageSubArea e");
    reader.setName("messageSubAreaReader");
    return reader;
  }

}
