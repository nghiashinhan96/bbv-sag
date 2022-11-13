package com.sagag.services.tools.batch.offer_feature.convert_id_sagsys;

import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.TargetOfferPosition;
import com.sagag.services.tools.repository.target.TargetOfferPositionRepository;
import com.sagag.services.tools.utils.ToolConstants;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Configuration
@OracleProfile
public class ConvertingIdSagsysJobConfig extends AbstractJobConfig {

  @Autowired
  private ConvertingIdSagsysProcessor convertingIdSagsysProcessor;

  @Autowired
  private VerifyConvertingIdSagsysTasklet verifyConvertingIdSagsysTasklet;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Autowired
  private TargetOfferPositionRepository targetOfferPositionRepo;

  @Override
  protected String jobName() {
    return "convertIdSagsysJob";
  }

  @Bean
  @Transactional
  public Job convertingIdSagsysJob(final BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer())
      .listener(listener)
      .start(convertingIdSagsysStep())
      .next(toStep(verifyConvertingIdSagsysTasklet)).build();
  }

  @Bean
  public Step convertingIdSagsysStep() throws Exception {
    return stepBuilderFactory.get("convertingIdSagsysStep")
        .<TargetOfferPosition, TargetOfferPosition>chunk(1)
      .reader(offerPositionJpaPagingItemReader())
      .processor(convertingIdSagsysProcessor)
      .writer(offerPositionWriter())
      .transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<TargetOfferPosition> offerPositionJpaPagingItemReader() throws Exception {
    JpaPagingItemReader<TargetOfferPosition> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      @Override
      public Query createQuery() {
        final String hqlStr = "select t from TargetOfferPosition t where (t.type = 'VENDORARTICLE' or t.type = 'VENDORARTICLEWITHOUTVEHICLE')";
        return this.entityManager.createQuery(hqlStr, TargetOfferPosition.class);
      }

      @Override
      public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
      }
    });
    reader.afterPropertiesSet();
    return reader;
  }

  @Bean
  @StepScope
  public ItemWriter<TargetOfferPosition> offerPositionWriter() throws Exception {
    return items -> targetOfferPositionRepo.saveAll(items.stream()
      .filter(Objects::nonNull).collect(Collectors.toList()));
  }

}
