package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.LegalDocumentMaster;
import com.sagag.services.copydb.domain.dest.DestLegalDocumentMaster;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LegalDocumentMasterCopySteps extends AbstractJobConfig {

  @Autowired
  private LegalDocumentMasterProcessor legalDocumentMasterProcessor;

  @Autowired
  private DestLegalDocumentMasterWriter legalDocumentMasterWriter;

  @Bean(name = "copyLegalDocumentMaster")
  public Step copyLegalDocumentMaster() {
    return stepBuilderFactory.get("copyLegalDocumentMaster").<LegalDocumentMaster, DestLegalDocumentMaster>chunk(DF_CHUNK)
        .reader(legalDocumentMasterReader())
        .processor(legalDocumentMasterProcessor)
        .writer(legalDocumentMasterWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<LegalDocumentMaster> legalDocumentMasterReader() {
    final JpaPagingItemReader<LegalDocumentMaster> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from LegalDocumentMaster e");
    reader.setName("legalDocumentMasterReader");
    return reader;
  }

}
