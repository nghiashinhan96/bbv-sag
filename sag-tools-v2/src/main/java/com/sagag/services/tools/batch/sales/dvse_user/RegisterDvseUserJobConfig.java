package com.sagag.services.tools.batch.sales.dvse_user;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.VUserDetail;
import com.sagag.services.tools.utils.ToolConstants;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Configuration
@OracleProfile
public class RegisterDvseUserJobConfig extends AbstractJobConfig {

  @Autowired
  private RegisterDvseUserProcessor registerDvseUserProcessor;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected String jobName() {
    return "createDvseUser";
  }

  @Bean
  @Transactional
  public Job registerDvseUserJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName()).incrementer(new RunIdIncrementer()).listener(listener).start(registerDvseUserStep())
        .next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step registerDvseUserStep() throws Exception {
    return stepBuilderFactory.get("registerDvseUserStep").<VUserDetail, VUserDetail>chunk(100).reader(registerDvseUserJpaPagingItemReader())
        .processor(registerDvseUserProcessor).transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<VUserDetail> registerDvseUserJpaPagingItemReader() throws Exception {
    JpaPagingItemReader<VUserDetail> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        final String hqlStr = "SELECT u FROM VUserDetail u WHERE u.userName LIKE 'Agent-%' AND u.lastName = 'Agent' AND u.userType = 'ON_BEHALF_ADMIN' "
            + "AND u.userId NOT IN (SELECT eu.eshopUserId FROM ExternalUser eu)";
        return this.entityManager.createQuery(hqlStr, VUserDetail.class);
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
