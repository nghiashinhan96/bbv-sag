package com.sagag.services.tools.batch.user.category_view_setting;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.batch.user.UserSettingItemWriter;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.utils.ToolConstants;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Configuration
@OracleProfile
public class ConvertingCateViewJobConfig extends AbstractJobConfig {

  @Autowired
  private ConvertingCateViewProcessor convertingCateViewProcessor;

  @Autowired
  private UserSettingItemWriter itemWriter;

  @Override
  protected String jobName() {
    return "convertCateViewJob";
  }

  @Bean
  @Transactional
  public Job convertingCateViewJob(final BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName())
      .incrementer(new RunIdIncrementer())
      .listener(listener)
      .start(convertingCateViewStep())
      .build();
  }

  @Bean
  public Step convertingCateViewStep() throws Exception {
    return stepBuilderFactory.get("convertingCateViewStep")
        .<UserSettings, UserSettings>chunk(1)
      .reader(userSettingsJpaPagingItemReader())
      .processor(convertingCateViewProcessor)
      .writer(itemWriter)
      .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<UserSettings> userSettingsJpaPagingItemReader() throws Exception {
    JpaPagingItemReader<UserSettings> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(getTargetEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        final String hqlStr = "select us from UserSettings us";
        return this.entityManager.createQuery(hqlStr, UserSettings.class);
      }

      @Override
      public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
      }
    });
    reader.afterPropertiesSet();
    return reader;
  }

}
