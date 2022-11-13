package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.LegalDocumentCustomerAcceptedLog;
import com.sagag.services.copydb.domain.dest.DestLegalDocumentCustomerAcceptedLog;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LegalDocumentCustomerAcceptedLogCopySteps extends AbstractJobConfig {

  @Autowired
  private LegalDocumentCustomerAcceptedLogProcessor legalDocumentCustomerAcceptedLogProcessor;

  @Autowired
  private DestLegalDocumentCustomerAcceptedLogWriter legalDocumentCustomerAcceptedLogWriter;

  @Bean(name = "copyLegalDocumentCustomerAcceptedLog")
  public Step copyLegalDocumentCustomerAcceptedLog() {
    return stepBuilderFactory.get("copyLegalDocumentCustomerAcceptedLog").<LegalDocumentCustomerAcceptedLog, DestLegalDocumentCustomerAcceptedLog>chunk(DF_CHUNK)
        .reader(legalDocumentCustomerAcceptedLogReader())
        .processor(legalDocumentCustomerAcceptedLogProcessor)
        .writer(legalDocumentCustomerAcceptedLogWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<LegalDocumentCustomerAcceptedLog> legalDocumentCustomerAcceptedLogReader() {
    final JpaPagingItemReader<LegalDocumentCustomerAcceptedLog> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from LegalDocumentCustomerAcceptedLog e");
    reader.setName("legalDocumentCustomerAcceptedLogReader");
    return reader;
  }

}
