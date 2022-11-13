package com.sagag.services.tools.batch.customer.ch;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.batch.customer.CustomerMigrationProcessor;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvAXCustomer;
import com.sagag.services.tools.domain.csv.CustInfo;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.support.CountryCode;
import com.sagag.services.tools.utils.CsvUtils;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Configuration
@OracleProfile
public class CHCustomerMigrationConfig extends AbstractJobConfig {

  @Autowired
  private CustomerMigrationProcessor processor;

  @Override
  protected String jobName() {
    return "addCHCustomer";
  }

  @Bean
  @Transactional
  public Job chCustomerMigrationJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilder(listener).start(chCustomerMigrationStep()).build();
  }

  @Bean
  public Step chCustomerMigrationStep() throws Exception {
    return stepBuilderFactory.get("chCustomerMigrationStep")
        .<CustInfo, String>chunk(DF_CHUNK)
        .reader(chCustomerItemReader())
        .processor(processor)
        .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public ListItemReader<CustInfo> chCustomerItemReader() throws BatchJobException {
    File file = new File(SystemUtils.getUserDir() + "/csv/CH_Customers.csv");
    final List<CsvAXCustomer> customers = CsvUtils.read(file, CsvAXCustomer.class);
    final List<CustInfo> custInfoList =
        customers.stream().map(CsvAXCustomer::getCustomerNr)
        .map(custNr -> new CustInfo(custNr, CountryCode.CH))
        .collect(Collectors.toList());
    return new ListItemReader<>(custInfoList);
  }
}
