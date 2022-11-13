package com.sagag.services.tools.batch.cleaner.partner_program.user;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.domain.csv.CsvPartnerProgramSetting;
import com.sagag.services.tools.domain.target.UserSettings;
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

import javax.persistence.EntityManagerFactory;

@Configuration
public class CleanPartnerProgramUserSettingConfig extends AbstractJobConfig {

  @Value("#{'${csv.clean_partner_program_setting:}'}")
  private String csvFileName;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Autowired
  private CleanPartnerProgramUserSettingProcessor settingProcessor;

  @Autowired
  private CleanPartnerProgramUserSettingWriter cleanPartnerProgramUserSettingWriter;

  @Override
  protected String jobName() {
    return "cleanPartnerProgramUserSetting";
  }

  @Bean
  public Job cleanPartnerProgramUserSetting(BatchJobCompletionNotificationListener listener) throws BatchJobException {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(cleanPartnerProgramUserSettingStep())
        .build();
  }

  @Bean
  public Step cleanPartnerProgramUserSettingStep() throws BatchJobException {
    return stepBuilderFactory.get("cleanPartnerProgramUserSettingStep")
        .<CsvPartnerProgramSetting, Optional<UserSettings>>chunk(DF_CHUNK)
        .reader(cleanPartnerProgramUserSettingCsvReader())
        .processor(settingProcessor)
        .writer(cleanPartnerProgramUserSettingWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean(destroyMethod = "")
  @StepScope
  public ListItemReader<CsvPartnerProgramSetting> cleanPartnerProgramUserSettingCsvReader() throws BatchJobException {
    final File file = new File(SystemUtils.getUserDir() + "/csv/" + csvFileName);
    final List<CsvPartnerProgramSetting> csvDatas = CsvUtils.read(file, CsvPartnerProgramSetting.class);
    return new ListItemReader<>(csvDatas);
  }

}
