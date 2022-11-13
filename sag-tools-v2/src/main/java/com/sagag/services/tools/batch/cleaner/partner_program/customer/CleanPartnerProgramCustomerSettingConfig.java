package com.sagag.services.tools.batch.cleaner.partner_program.customer;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.csv.CsvPartnerProgramSetting;
import com.sagag.services.tools.domain.target.CustomerSettings;
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

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;

@Configuration
public class CleanPartnerProgramCustomerSettingConfig extends AbstractJobConfig {

  @Value("#{'${csv.clean_partner_program_setting:}'}")
  private String csvFileName;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Autowired
  private CleanPartnerProgramCustomerSettingProcessor settingProcessor;

  @Autowired
  private CleanPartnerProgramCustomerSettingWriter cleanPartnerProgramCustomerSettingWriter;

  @Override
  protected String jobName() {
    return "cleanPartnerProgramCustomerSetting";
  }

  @Bean
  public Job cleanPartnerProgramCustomerSetting(BatchJobCompletionNotificationListener listener)
      throws BatchJobException {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(cleanPartnerProgramCustomerSettingStep())
        .build();
  }

  @Bean
  public Step cleanPartnerProgramCustomerSettingStep() throws BatchJobException {
    return stepBuilderFactory.get("cleanPartnerProgramCustomerSettingStep")
        .<CsvPartnerProgramSetting, Optional<CustomerSettings>>chunk(DF_CHUNK)
        .reader(cleanPartnerProgramCustomerSettingCsvReader())
        .processor(settingProcessor)
        .writer(cleanPartnerProgramCustomerSettingWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean(destroyMethod = "")
  @StepScope
  public ListItemReader<CsvPartnerProgramSetting> cleanPartnerProgramCustomerSettingCsvReader()
      throws BatchJobException {
    final File file = new File(SystemUtils.getUserDir() + "/csv/" + csvFileName);
    final List<CsvPartnerProgramSetting> rawCsvDatas = CsvUtils.read(file, CsvPartnerProgramSetting.class);
    final List<CsvPartnerProgramSetting> csvDatas =
        rawCsvDatas.stream().filter(distinctByKey(CsvPartnerProgramSetting::getOrgCode)).collect(Collectors.toList());
    return new ListItemReader<>(csvDatas);
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

}
