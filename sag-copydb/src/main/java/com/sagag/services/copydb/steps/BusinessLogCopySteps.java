package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.BusinessLog;
import com.sagag.services.copydb.domain.dest.DestBusinessLog;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class BusinessLogCopySteps extends AbstractJobConfig {

  @Autowired
  private BusinessLogProcessor businessLogProcessor;

  @Autowired
  private DestBusinessLogWriter businessLogWriter;

  @Bean(name = "copyBusinessLog")
  public Step copyBusinessLog() {
    return stepBuilderFactory.get("copyBusinessLog").<BusinessLog, DestBusinessLog>chunk(DF_CHUNK)
        .reader(businessLogReader())
        .processor(businessLogProcessor)
        .writer(businessLogWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<BusinessLog> businessLogReader() {
    final JpaPagingItemReader<BusinessLog> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from BusinessLog e");
    reader.setName("businessLogReader");
    return reader;
  }

}
