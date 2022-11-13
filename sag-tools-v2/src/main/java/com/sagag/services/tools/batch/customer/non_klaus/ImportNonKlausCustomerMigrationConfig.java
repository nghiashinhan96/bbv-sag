package com.sagag.services.tools.batch.customer.non_klaus;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.customer.CsvCustomerInfoData;
import com.sagag.services.tools.domain.customer.CsvNonKlausCustomerInfoData;
import com.sagag.services.tools.domain.customer.ImportedCustomerData;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Configuration
@Slf4j
public class ImportNonKlausCustomerMigrationConfig extends AbstractJobConfig {

  @Value("${sag.csv.support:}")
  private String csvSupportName;

  @Autowired
  private NonKlausCustomerMigrationItemProcessorV2 customerMigrationItemProcessor;

  @Autowired
  private NonKlausCustomerMigrationItemWriterV2 customerMigrationItemWriter;

  @Autowired
  private ItemProcessListener<?, ?> customerMigrationItemProcessListener;

  @Autowired
  private ItemWriteListener<?> customerMigrationItemWriteListener;

  private List<CsvCustomerInfoData> csvCustomerInfoItems;

  @Override
  protected String jobName() {
    return "importNonKlausCustomers";
  }

  @Bean
  @Transactional
  public Job importNonKlausCustomersJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilder(listener)
        .start(importNonKlausCustomersStep()) // Import the customer list of Klaus affiliate and create sales on behalf user
        .build();
  }

  @Bean
  public Step importNonKlausCustomersStep() throws Exception {
    final SimpleStepBuilder<CsvCustomerInfoData, ImportedCustomerData> stepBuilder =
        stepBuilderFactory.get("importNonKlausCustomersStep")
        .<CsvCustomerInfoData, ImportedCustomerData>chunk(DF_CHUNK);

    stepBuilder.reader(importNonKlausCustomersCsvReader());
    stepBuilder.processor(customerMigrationItemProcessor);
    stepBuilder.listener(customerMigrationItemProcessListener);
    stepBuilder.writer(customerMigrationItemWriter);
    stepBuilder.listener(customerMigrationItemWriteListener);
    stepBuilder.transactionManager(targetTransactionManager);

    return stepBuilder.build();
  }

  @Bean
  @StepScope
  public ListItemReader<CsvCustomerInfoData> importNonKlausCustomersCsvReader() throws BatchJobException {
    final StringBuilder fileNameBuilder = new StringBuilder();
    fileNameBuilder.append("/csv/");
    fileNameBuilder.append("NON_KLAUS_CUSTOMER");
    if (!StringUtils.isBlank(csvSupportName)) {
      fileNameBuilder.append(csvSupportName);
    }
    fileNameBuilder.append(".csv");
    log.debug("fileNameBuilder = {}", fileNameBuilder);
    final File file = new File(SystemUtils.getUserDir() + fileNameBuilder.toString());
    csvCustomerInfoItems = CsvUtils.read(file, ' ', StandardCharsets.UTF_8,
        CsvNonKlausCustomerInfoData.class, true).stream()
        .map(CsvNonKlausCustomerInfoData::build).collect(Collectors.toList());
    // csvCustomerInfoItems.forEach(item -> item.setAffiliate(SupportedAffiliate.KLAUS));
    return new ListItemReader<>(csvCustomerInfoItems);
  }

}
