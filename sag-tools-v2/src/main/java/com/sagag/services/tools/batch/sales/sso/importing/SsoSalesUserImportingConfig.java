package com.sagag.services.tools.batch.sales.sso.importing;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.SsoSalesUser;
import com.sagag.services.tools.domain.SsoSalesUserOutput;
import com.sagag.services.tools.domain.csv.CsvSsoSalesUser;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@OracleProfile
public class SsoSalesUserImportingConfig extends AbstractJobConfig {

  @Autowired
  private SsoSalesUserImportingItemProcessor ssoSalesUserImportingItemProcessor;

  @Autowired
  private SsoSalesUserImportingItemWriter ssoSalesUserImportingItemWriter;

  @Override
  protected String jobName() {
    return "ImportSsoSalesUsers";
  }

  @Bean
  @Transactional
  public Job importSsoSalesUsersJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName()).incrementer(new RunIdIncrementer()).listener(listener).start(importSsoSalesUserStep()).build();
  }

  @Bean
  public Step importSsoSalesUserStep() throws Exception {
    return stepBuilderFactory.get("importSsoSalesUserStep").<SsoSalesUser, SsoSalesUserOutput>chunk(DF_CHUNK).reader(ssoSalesItemReader())
        .processor(ssoSalesUserImportingItemProcessor).writer(ssoSalesUserImportingItemWriter).transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public ListItemReader<SsoSalesUser> ssoSalesItemReader() throws BatchJobException {
    File file = new File(SystemUtils.getUserDir() + "/csv/new_sso_sales_users.csv");
    List<CsvSsoSalesUser> csvSsoSalesUsers = CsvUtils.read(file, CsvSsoSalesUser.class);
    List<SsoSalesUser> ssoSalesUser = csvSsoSalesUsers.stream().map(SsoSalesUser::fromCsvSsoSalesUser).collect(Collectors.toList());
    return new ListItemReader<>(ssoSalesUser);
  }
}
