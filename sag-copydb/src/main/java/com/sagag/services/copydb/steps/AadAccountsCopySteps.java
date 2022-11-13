package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AadAccounts;
import com.sagag.services.copydb.domain.dest.DestAadAccounts;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AadAccountsCopySteps extends AbstractJobConfig {

  @Autowired
  private AadAccountsProcessor aadAccountsProcessor;

  @Autowired
  private DestAadAccountsWriter aadAccountsWriter;

  @Bean(name = "copyAadAccounts")
  public Step copyAadAccounts() {
    return stepBuilderFactory.get("copyAadAccounts").<AadAccounts, DestAadAccounts>chunk(DF_CHUNK)
        .reader(aadAccountsReader())
        .processor(aadAccountsProcessor)
        .writer(aadAccountsWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<AadAccounts> aadAccountsReader() {
    final JpaPagingItemReader<AadAccounts> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from AadAccounts e");
    reader.setName("aadAccountsReader");
    return reader;
  }

}
