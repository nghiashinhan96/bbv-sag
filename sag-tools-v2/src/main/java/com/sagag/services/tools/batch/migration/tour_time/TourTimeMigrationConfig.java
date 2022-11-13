package com.sagag.services.tools.batch.migration.tour_time;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.csv.CsvTourTime;
import com.sagag.services.tools.domain.target.TargetTourTime;
import com.sagag.services.tools.exception.BatchJobException;
import com.sagag.services.tools.utils.CsvUtils;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

@Configuration
@Slf4j
public class TourTimeMigrationConfig extends AbstractJobConfig {

  @Value("#{'${csv.tour_time:tour_time.csv}'}")
  private String csvFileName;

  @Autowired
  private TourTimeMigrationItemProcessor processor;

  @Autowired
  private TourTimeMigrationItemWriter writer;

  @Override
  protected String jobName() {
    return "tourTimeMigration";
  }

  @Bean
  @Transactional
  public Job tourTimeMigrationJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilder(listener).start(tourTimeMigrationStep()).build();
  }

  @Bean
  public Step tourTimeMigrationStep() throws Exception {
    return stepBuilderFactory.get("tourTimeMigrationStep")
        .<CsvTourTime, TargetTourTime>chunk(2000)
        .reader(tourTimeMigrationItemReader())
        .processor(processor)
        .writer(writer)
        .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public ListItemReader<CsvTourTime> tourTimeMigrationItemReader() throws BatchJobException {
    final File file = new File(SystemUtils.getUserDir() + "/csv/" + csvFileName);
    final List<CsvTourTime> branches = CsvUtils.read(file, CsvTourTime.class);
    log.info("Size of csv = {}", branches.size());
    return new ListItemReader<>(branches);
  }
}
