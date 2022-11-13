package com.sagag.services.tools.batch.sales.sso;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.SagsysProfile;
import com.sagag.services.tools.domain.sagsys.AadAccounts;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Configuration
@SagsysProfile
public class TransferSalesAccountSSOFromSagSysConfig extends AbstractJobConfig {

  private static final int PAGE_SIZE = 50;

  @Autowired
  private TransferSalesAccountSSOFromSagSysProcessor processor;
  @Autowired
  private TransferSalesAccountSSOFromSagSysWriter writer;

  @Override
  protected String jobName() {
    return "transferSalesSSOFromSAGSYS";
  }

  @Bean
  public Job createTestSalesUser(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .start(initialSalesAccountSSOCreationStep())
        .build();
  }

  @Bean
  public Step initialSalesAccountSSOCreationStep() {
    return stepBuilderFactory.get("initialSalesAccountSSOCreationStep")
        .<AadAccounts, Optional<com.sagag.services.tools.domain.target.AadAccounts>>chunk(DF_CHUNK)
        .reader(getAccounts())
        .processor(processor)
        .writer(writer)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean(destroyMethod = "")
  @StepScope
  public JpaPagingItemReader<AadAccounts> getAccounts() {
    return new JpaPagingItemReaderBuilder<AadAccounts>()
        .transacted(true)
        .entityManagerFactory(getSourceEntityManagerFactory())
        .pageSize(PAGE_SIZE)
        .queryProvider(new JpaQueryProvider() {
          private EntityManager entityManager;

          @Override
          public Query createQuery() {
            final String hqlStr = "SELECT a FROM AadAccounts a";
            return this.entityManager.createQuery(hqlStr, AadAccounts.class);
          }

          @Override
          public void setEntityManager(final EntityManager entityManager) {
            this.entityManager = entityManager;
          }
        })
        .name("userMigrationItemReader")
        .build();
  }

}
