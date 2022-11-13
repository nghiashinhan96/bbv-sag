package com.sagag.services.tools.batch.offer_feature.offer_status;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.utils.ToolConstants;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Configuration
@OracleProfile
public class AdjustOfferStatusJobConfig extends AbstractJobConfig {

  @Autowired
  private AdjustOfferStatusItemProcessor adjustOfferStatusItemProcessor;

  @Override
  protected String jobName() {
    return "adjustOfferStatusJob";
  }

  @Bean
  @Transactional
  public Job adjustOfferStatusJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName())
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(adjustOfferStatusStep()).next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step adjustOfferStatusStep() throws Exception {
    return stepBuilderFactory.get("adjustOfferStatusStep").<TargetOffer, TargetOffer>chunk(100)
      .reader(offerItemReader()).processor(adjustOfferStatusItemProcessor).writer(offerItemWriter())
      .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<TargetOffer> offerItemReader() throws Exception {
    final JpaPagingItemReader<TargetOffer> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(getTargetEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(offerJpaQueryProvider());
    reader.afterPropertiesSet();
    return reader;
  }

  @Bean
  public JpaQueryProvider offerJpaQueryProvider() {
    return new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        return entityManager.createQuery("select o from TargetOffer o", TargetOffer.class);
      }

      @Override
      public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
      }
    };
  }

  @Bean
  @StepScope
  public JpaItemWriter<TargetOffer> offerItemWriter() throws Exception {
    final JpaItemWriter<TargetOffer> writer = new JpaItemWriter<>();
    writer.setEntityManagerFactory(getTargetEntityManagerFactory());
    writer.afterPropertiesSet();
    return writer;
  }
}
