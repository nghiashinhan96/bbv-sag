package com.sagag.services.tools.batch.migration.branch;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.csv.CsvBranchOpeningHour;
import com.sagag.services.tools.domain.target.Branch;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

@Configuration
public class BranchMigrationConfig extends AbstractJobConfig {

  @Autowired
  private BranchMigrationProcessor processor;

  @Autowired
  private BranchItemWriter writer;

  @Value("#{'${csv.branch_opening_hours:}'}")
  private String csvFileName;

  @Override
  protected String jobName() {
    return "branchMigration";
  }

  @Bean
  @Transactional
  public Job branchMigrationJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(branchMigrationStep())
        .build();
  }

  @Bean
  public Step branchMigrationStep() throws Exception {
    return stepBuilderFactory.get("branchMigrationStep")
        .<CsvBranchOpeningHour, Branch>chunk(DF_CHUNK)
        .reader(branchItemReader())
        .processor(processor)
        .writer(writer)
        .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public ListItemReader<CsvBranchOpeningHour> branchItemReader() throws BatchJobException {
    final File file = new File(SystemUtils.getUserDir() + "/csv/" + csvFileName);
    final List<CsvBranchOpeningHour> branches = CsvUtils.read(file, CsvBranchOpeningHour.class);
    return new ListItemReader<>(branches);
  }

}
