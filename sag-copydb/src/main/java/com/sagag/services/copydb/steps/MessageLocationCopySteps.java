package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLocation;
import com.sagag.services.copydb.domain.dest.DestMessageLocation;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLocationCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageLocationProcessor messageLocationProcessor;

  @Autowired
  private DestMessageLocationWriter messageLocationWriter;

  @Bean(name = "copyMessageLocation")
  public Step copyMessageLocation() {
    return stepBuilderFactory.get("copyMessageLocation").<MessageLocation, DestMessageLocation>chunk(DF_CHUNK)
        .reader(messageLocationReader())
        .processor(messageLocationProcessor)
        .writer(messageLocationWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageLocation> messageLocationReader() {
    final JpaPagingItemReader<MessageLocation> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageLocation e");
    reader.setName("messageLocationReader");
    return reader;
  }

}
