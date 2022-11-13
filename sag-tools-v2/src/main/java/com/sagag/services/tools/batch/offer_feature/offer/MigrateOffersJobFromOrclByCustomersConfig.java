package com.sagag.services.tools.batch.offer_feature.offer;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.source.SourceOffer;
import com.sagag.services.tools.domain.source.SourceOfferPosition;
import com.sagag.services.tools.domain.target.TargetOffer;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.repository.target.TargetOfferPositionRepository;
import com.sagag.services.tools.repository.target.TargetOfferRepository;
import com.sagag.services.tools.support.AvailablecusCustomerResource;
import com.sagag.services.tools.support.ConnectQueryFactory;
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
import org.springframework.util.Assert;

import java.util.List;
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
public class MigrateOffersJobFromOrclByCustomersConfig extends AbstractJobConfig {

  @Autowired
  private OfferPositionProcessor offerPositionProcessor;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManager targetEntityManager;

  @Autowired
  private TargetOfferPositionRepository targetOfferPositionRepo;

  @Autowired
  private AvailablecusCustomerResource availablecusCustomerResource;

  @Autowired
  private OfferProcessor offerProcessor;

  @Autowired
  private TargetOfferRepository targetOfferRepo;

  @Autowired
  private ConnectQueryFactory queryFactory;

  @Override
  protected String jobName() {
    return StringUtils.EMPTY;
  }

  @Bean
  @Transactional
  public Job importOfferFromOrclJobForCustomer(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get("importOffersForCustomerFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(offerStepForCustomerStep()).next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step offerStepForCustomerStep() throws Exception {
    return stepBuilderFactory.get("offerStepForCustomerStep")
      .<SourceOffer, TargetOffer>chunk(ToolConstants.MAX_CHUNK_SIZE)
      .reader(offerReader()).processor(offerProcessor)
      .writer(offerWriter()).transactionManager(targetTransactionManager)
      .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<SourceOffer> offerReader() throws Exception {
    JpaPagingItemReader<SourceOffer> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(getSourceEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        final String queryStr = queryFactory.createFindOffersByVendorIdListQuery();
        final List<Long> customerList = availablecusCustomerResource.getEblOrgIdList();
        Assert.notEmpty(customerList, "You dont have customer number list to run this job");
        return this.entityManager.createQuery(queryStr, SourceOffer.class)
          .setParameter("vendorIds", customerList);
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
  public ItemWriter<TargetOffer> offerWriter() throws Exception {
    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER.getTableName());
    return items -> targetOfferRepo.saveAll(items.stream()
      .filter(item -> Objects.nonNull(item)).collect(Collectors.toList()));
  }

  @Bean
  @Transactional
  public Job importOfferPositionsFromOrclJobForCustomer(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get("importOfferPositionsForCustomerFromOrclJob")
      .incrementer(new RunIdIncrementer()).listener(listener)
      .start(offerPositionForCustomerStep()).next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step offerPositionForCustomerStep() throws Exception {
    return stepBuilderFactory.get("offerPositionForCustomerStep")
      .<SourceOfferPosition, TargetOfferPosition>chunk(ToolConstants.MAX_CHUNK_SIZE)
      .reader(offerPositionForCustomerReader()).processor(offerPositionProcessor)
      .writer(offerPositionForCustomerWriter()).transactionManager(targetTransactionManager)
      .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<SourceOfferPosition> offerPositionForCustomerReader() throws Exception {
    JpaPagingItemReader<SourceOfferPosition> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(getSourceEntityManagerFactory());
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        final String queryStr = queryFactory.createFindOfferPositionsByVendorIdListQuery();
        final List<Long> customerList = availablecusCustomerResource.getEblOrgIdList();
        Assert.notEmpty(customerList, "You dont have customer number list to run this job");
        return this.entityManager.createQuery(queryStr, SourceOfferPosition.class)
            .setParameter("vendorIds", customerList);
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
  public ItemWriter<TargetOfferPosition> offerPositionForCustomerWriter() throws Exception {
    // Update custom config to set exactly id
    SessionImpl session = (SessionImpl) targetEntityManager.getDelegate();
    EntityManagerUtils.turnOnIdentityInsert(session, OfferTables.OFFER_POSITION.getTableName());
    return items -> targetOfferPositionRepo.saveAll(items.stream()
          .filter(item -> Objects.nonNull(item)).collect(Collectors.toList()));
  }

}
