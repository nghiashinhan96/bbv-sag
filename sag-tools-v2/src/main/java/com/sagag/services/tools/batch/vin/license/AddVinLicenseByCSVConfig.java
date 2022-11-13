package com.sagag.services.tools.batch.vin.license;


import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.csv.CsvAXCustomer;
import com.sagag.services.tools.domain.target.License;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;

@Configuration
@OracleProfile
public class AddVinLicenseByCSVConfig extends AbstractJobConfig {


  @Value("${vinlicense.fileName:}")
  private String fileName;

  @Autowired
  private AddVinLicenseByCSVProcessor addVinLicencesProcessor;

  @Autowired
  private AddVinLicenseByCSVWriter addVinLicencesVWriter;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected String jobName() {
    return "addVinLicensesByCSV";
  }

  @Bean
  @Transactional
  public Job addVinLicensesJob(BatchJobCompletionNotificationListener listener) throws BatchJobException {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(addVinLicensesByCSVStep())
        .next(restoreDbConfigStep())
        .build();
  }

  @Bean
  public Step addVinLicensesByCSVStep() throws BatchJobException {
    return stepBuilderFactory.get("addVinLicensesByCSVStep").<String, Optional<License>>chunk(DF_CHUNK)
        .reader(addVinLicensesByCSVItemReader())
        .processor(addVinLicencesProcessor)
        .writer(addVinLicencesVWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public ListItemReader<String> addVinLicensesByCSVItemReader() throws BatchJobException {
    Assert.hasText(fileName, "fileName is required");
    File file = new File(SystemUtils.getUserDir() + File.separator + fileName);
    final List<CsvAXCustomer> customers = CsvUtils.read(file, CsvAXCustomer.class);
    return new ListItemReader<>(customers.stream().map(CsvAXCustomer::getCustomerNr)
        .collect(Collectors.toList()));
  }

}
