package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.VinLogging;
import com.sagag.services.copydb.domain.dest.DestVinLogging;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class VinLoggingCopySteps extends AbstractJobConfig {

  @Autowired
  private VinLoggingProcessor vinLoggingProcessor;

  @Autowired
  private DestVinLoggingWriter vinLoggingWriter;

  @Bean(name = "copyVinLogging")
  public Step copyVinLogging() {
    return stepBuilderFactory.get("copyVinLogging").<VinLogging, DestVinLogging>chunk(DF_CHUNK)
        .reader(vinLoggingReader())
        .processor(vinLoggingProcessor)
        .writer(vinLoggingWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<VinLogging> vinLoggingReader() {
    final JpaPagingItemReader<VinLogging> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from VinLogging e");
    reader.setName("vinLoggingReader");
    return reader;
  }

}
