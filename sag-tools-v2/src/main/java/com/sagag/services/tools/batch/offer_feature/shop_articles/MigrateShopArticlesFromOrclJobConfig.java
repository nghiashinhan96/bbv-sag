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
public class MigrateShopArticlesFromOrclJobConfig extends AbstractJobConfig {

  @Autowired
  private MigrateShopArticleFromOrclTasklet migrateShopArticleFromOrclTasklet;

  @Override
  protected String jobName() {
    return "importShopArticlesFromOrclJob";
  }

  @Bean
  public Job importShopArticlesFromOrclJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migrateShopArticleFromOrclTasklet)).next(restoreDbConfigStep()).build();
  }

}
