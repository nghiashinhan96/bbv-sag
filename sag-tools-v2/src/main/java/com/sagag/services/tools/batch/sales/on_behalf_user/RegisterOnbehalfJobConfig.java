package com.sagag.services.tools.batch.sales.on_behalf_user;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.Organisation;
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
public class RegisterOnbehalfJobConfig extends AbstractJobConfig {

  @Autowired
  private RegisterOnbehalfProcessor registerOnbehalfProcessor;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected String jobName() {
    return "createVirtualUserOnBehalf";
  }

  @Bean
  @Transactional
  public Job registerOnbehalfJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer()).listener(listener).start(registerOnbehalfStep())
        .next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step registerOnbehalfStep() throws Exception {
    return stepBuilderFactory.get("registerOnbehalfStep").<Organisation, Organisation>chunk(200)
        .reader(registerOnbehalfJpaPagingItemReader()).processor(registerOnbehalfProcessor)
        .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Organisation> registerOnbehalfJpaPagingItemReader() throws Exception {
    JpaPagingItemReader<Organisation> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        final String hqlStr = "SELECT org FROM Organisation org WHERE org.orgCode NOT IN "
            + "(SELECT DISTINCT org.orgCode "
            + "FROM Organisation org, EshopUser usr, GroupUser gu, OrganisationGroup og "
            + "WHERE org.id = og.organisation.id AND usr.id = gu.eshopUser.id AND og.eshopGroup.id = gu.eshopGroup.id and usr.type = 'ON_BEHALF_ADMIN') "
            + "AND org.orgTypeId = 3 AND org.parentId NOT IN (4,8) " + "ORDER BY org.id";
        return this.entityManager.createQuery(hqlStr, Organisation.class);
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
