package com.sagag.services.tools.batch.vin.license;


import com.sagag.services.tools.batch.AbstractJobConfig;
import com.sagag.services.tools.batch.BatchJobCompletionNotificationListener;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.support.SupportedAffiliate;
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

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

@Configuration
@OracleProfile
public class AddVinLicenseJobConfig extends AbstractJobConfig {

  @Autowired
  private AddVinLicenseProcessor addVinLicencesProcessor;

  @Autowired
  @Qualifier("targetEntityManager")
  private EntityManagerFactory entityManagerFactory;

  @Override
  protected String jobName() {
    return "addVinLicensesByCSV";
  }

  @Bean
  @Transactional
  public Job addVinLicensesJob(BatchJobCompletionNotificationListener listener) throws Exception {
    return jobBuilderFactory.get(jobName())
        .incrementer(new RunIdIncrementer()).listener(listener).start(addVinLicensesStep())
        .next(restoreDbConfigStep()).build();
  }

  @Bean
  public Step addVinLicensesStep() throws Exception {
    return stepBuilderFactory.get("addVinLicensesStep").<String, String>chunk(200).reader(addVinLicensesJpaPagingItemReader())
        .processor(addVinLicencesProcessor).transactionManager(targetTransactionManager).build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<String> addVinLicensesJpaPagingItemReader() throws Exception {
    JpaPagingItemReader<String> reader = new JpaPagingItemReader<>();
    reader.setEntityManagerFactory(entityManagerFactory);
    reader.setPageSize(ToolConstants.MAX_SIZE);
    reader.setQueryProvider(new JpaQueryProvider() {
      private EntityManager entityManager;

      List<String> affiliates = SupportedAffiliate.getAtAffiliates().stream().map(SupportedAffiliate::getAffiliate).collect(Collectors.toList());

      @Override
      public Query createQuery() {
        String hqlStr = "SELECT distinct org.orgCode FROM Organisation org "
            + " WHERE org.orgTypeId = 3 AND org.parentId in "
            + " (SELECT affi.id FROM Organisation affi WHERE affi.shortname in :listShortName AND affi.orgTypeId = 2)"
            + " ORDER BY org.orgCode";

        return this.entityManager.createQuery(hqlStr, String.class).setParameter("listShortName", affiliates);
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
