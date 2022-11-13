package com.sagag.services.tools.batch.sales.email;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.VUserDetail;
import com.sagag.services.tools.utils.ToolConstants;

import org.apache.commons.lang3.StringUtils;
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

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Configuration
@OracleProfile
public class SalesUserRegistrationConfig extends AbstractJobConfig {

  @Autowired
  private SalesUserRegistrationItemProcessor processor;
  @Autowired
  private SalesUserRegistrationItemWriter writer;
  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  @Bean
  public Job updateSalesEmail(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get("UpdateSalesEmail")
      .incrementer(new RunIdIncrementer())
      .listener(listener)
      .start(initialSalesUserStep())
      .build();
  }

  @Bean
  public Step initialSalesUserStep() throws Exception {
    return stepBuilderFactory.get("initialSalesUserStep")
        .<VUserDetail, Optional<EshopUser>>chunk(DF_CHUNK)
      .reader(salesUserNrs())
      .processor(processor)
      .writer(writer)
      .transactionManager(targetTransactionManager)
      .build();
  }

  @Bean(destroyMethod = "")
  @StepScope
  public JpaPagingItemReader<VUserDetail> salesUserNrs() throws Exception {
    JpaPagingItemReader<VUserDetail> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        final String hqlStr = "SELECT u FROM VUserDetail u WHERE u.userName LIKE 'sales%' and u.roleName = 'SALES_ASSISTANT' ";
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
