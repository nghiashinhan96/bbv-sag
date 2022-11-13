package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOfferPosition;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.repository.target.TargetOfferPositionRepository;
import com.sagag.services.tools.support.OfferTables;
import com.sagag.services.tools.utils.EntityManagerUtils;
import com.sagag.services.tools.utils.ToolConstants;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.orm.JpaQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * Configuration class to define job scenario.
 */
@Configuration
@OracleProfile
public class MigrateOffersJobFromOrclConfig extends AbstractJobConfig {

  @Autowired
  private MigrateOfferFromOrclTasklet migrateOfferFromOrclTasklet;

  @Autowired
  private OfferPositionProcessor offerPositionProcessor;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager targetEntityManager;

  @Autowired
  private TargetOfferPositionRepository targetOfferPositionRepo;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  @Bean
  @Transactional
  public Job importOfferFromOrclJob(BatchJobCompletionNotificationListener listener) {
    return jobBuilderFactory.get("importOffersFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(toStep(migrateOfferFromOrclTasklet)).next(restoreDbConfigStep()).build();
  }

  @Bean
  @Transactional
  public Job importOfferPositionsFromOrclJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get("importOfferPositionsFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(offerPositionStep()).next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step offerPositionStep() throws Exception {
    return stepBuilderFactory.get("slaveStep")
      .<SourceOfferPosition, TargetOfferPosition>chunk(1)
      .reader(offerPositionReader()).processor(offerPositionProcessor)
      .writer(offerPositionWriter()).transactionManager(targetTransactionManager)
      .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<SourceOfferPosition> offerPositionReader() throws Exception {
    JpaPagingItemReader<SourceOfferPosition> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(getSourceEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        return this.entityManager.createQuery("select o from SourceOfferPosition o",
            SourceOfferPosition.class);
      }

      @Override
      public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
      }
    });
    reader.afterPropertiesSet();
    return reader;
  }

  @Bean
  @StepScope
  public ItemWriter<TargetOfferPosition> offerPositionWriter() throws Exception {
    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER_POSITION.getTableName());
    return items -> {
      targetOfferPositionRepo.saveAll(items.stream()
          .filter(item -> Objects.nonNull(item)).collect(Collectors.toList()));
    };
  }
}
