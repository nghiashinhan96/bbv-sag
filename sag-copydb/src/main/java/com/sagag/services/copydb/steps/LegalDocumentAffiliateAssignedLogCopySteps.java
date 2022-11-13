package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.LegalDocumentAffiliateAssignedLog;
import com.sagag.services.copydb.domain.dest.DestLegalDocumentAffiliateAssignedLog;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LegalDocumentAffiliateAssignedLogCopySteps extends AbstractJobConfig {

  @Autowired
  private LegalDocumentAffiliateAssignedLogProcessor legalDocumentAffiliateAssignedLogProcessor;

  @Autowired
  private DestLegalDocumentAffiliateAssignedLogWriter legalDocumentAffiliateAssignedLogWriter;

  @Bean(name = "copyLegalDocumentAffiliateAssignedLog")
  public Step copyLegalDocumentAffiliateAssignedLog() {
    return stepBuilderFactory.get("copyLegalDocumentAffiliateAssignedLog").<LegalDocumentAffiliateAssignedLog, DestLegalDocumentAffiliateAssignedLog>chunk(DF_CHUNK)
        .reader(legalDocumentAffiliateAssignedLogReader())
        .processor(legalDocumentAffiliateAssignedLogProcessor)
        .writer(legalDocumentAffiliateAssignedLogWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<LegalDocumentAffiliateAssignedLog> legalDocumentAffiliateAssignedLogReader() {
    final JpaPagingItemReader<LegalDocumentAffiliateAssignedLog> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from LegalDocumentAffiliateAssignedLog e");
    reader.setName("legalDocumentAffiliateAssignedLogReader");
    return reader;
  }

}
