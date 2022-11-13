package com.sagag.services.tools.batch.offer_feature.shop_articles;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to define job scenario.
 */
@Configuration
@OracleProfile
public class MigrateShopArticlesJobConfig extends AbstractJobConfig {

  @Autowired
  private MigrateShopArticleFromCsvTasklet migrateShopArticleTasklet;

  @Override
  protected String jobName() {
    return "importShopArticlesJob";
  }

  @Bean
  public Job importShopArticlesJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migrateShopArticleTasklet)).next(restoreDbConfigStep()).build();
  }

}
