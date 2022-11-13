package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLanguage;
import com.sagag.services.copydb.domain.dest.DestMessageLanguage;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLanguageCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageLanguageProcessor messageLanguageProcessor;

  @Autowired
  private DestMessageLanguageWriter messageLanguageWriter;

  @Bean(name = "copyMessageLanguage")
  public Step copyMessageLanguage() {
    return stepBuilderFactory.get("copyMessageLanguage").<MessageLanguage, DestMessageLanguage>chunk(DF_CHUNK)
        .reader(messageLanguageReader())
        .processor(messageLanguageProcessor)
        .writer(messageLanguageWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageLanguage> messageLanguageReader() {
    final JpaPagingItemReader<MessageLanguage> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageLanguage e");
    reader.setName("messageLanguageReader");
    return reader;
  }

}
