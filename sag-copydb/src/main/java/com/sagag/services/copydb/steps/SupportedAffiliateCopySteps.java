package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.SupportedAffiliate;
import com.sagag.services.copydb.domain.dest.DestSupportedAffiliate;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class SupportedAffiliateCopySteps extends AbstractJobConfig {

  @Autowired
  private SupportedAffiliateProcessor supportedAffiliateProcessor;

  @Autowired
  private DestSupportedAffiliateWriter supportedAffiliateWriter;

  @Bean(name = "copySupportedAffiliate")
  public Step copySupportedAffiliate() {
    return stepBuilderFactory.get("copySupportedAffiliate").<SupportedAffiliate, DestSupportedAffiliate>chunk(DF_CHUNK)
        .reader(supportedAffiliateReader())
        .processor(supportedAffiliateProcessor)
        .writer(supportedAffiliateWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<SupportedAffiliate> supportedAffiliateReader() {
    final JpaPagingItemReader<SupportedAffiliate> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from SupportedAffiliate e");
    reader.setName("supportedAffiliateReader");
    return reader;
  }

}
