package com.sagag.services.tools.batch.customer.ax;

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
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
public class AXCustomerMigrationConfig extends AbstractJobConfig {

  @Autowired
  private CustomerMigrationProcessor processor;

  @Override
  protected String jobName() {
    return "addAXCustomer";
  }

  @Bean
  @Transactional
  public Job axCustomerMigrationJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(axCustomerMigrationStep())
        .build();
  }

  @Bean
  public Step axCustomerMigrationStep() throws Exception {
    return stepBuilderFactory.get("axCustomerMigrationStep")
        .<CustInfo, String>chunk(DF_CHUNK)
        .reader(axCustomerItemReader())
        .processor(processor)
        .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public ListItemReader<CustInfo> axCustomerItemReader() throws BatchJobException {
    File file = new File(SystemUtils.getUserDir() + "/csv/Customers_missing_in_Connect-20181124.csv");
    final List<CsvAXCustomer> customers = CsvUtils.read(file, CsvAXCustomer.class);
    final List<CustInfo> custInfoList =
        customers.stream().map(CsvAXCustomer::getCustomerNr)
        .map(custNr -> new CustInfo(custNr, CountryCode.AT)).collect(Collectors.toList());
    return new ListItemReader<>(custInfoList);
  }
}
