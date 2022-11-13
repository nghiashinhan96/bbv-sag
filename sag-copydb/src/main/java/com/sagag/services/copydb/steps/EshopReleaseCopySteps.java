package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopRelease;
import com.sagag.services.copydb.domain.dest.DestEshopRelease;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopReleaseCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopReleaseProcessor eshopReleaseProcessor;

  @Autowired
  private DestEshopReleaseWriter eshopReleaseWriter;

  @Bean(name = "copyEshopRelease")
  public Step copyEshopRelease() {
    return stepBuilderFactory.get("copyEshopRelease").<EshopRelease, DestEshopRelease>chunk(DF_CHUNK)
        .reader(eshopReleaseReader())
        .processor(eshopReleaseProcessor)
        .writer(eshopReleaseWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopRelease> eshopReleaseReader() {
    final JpaPagingItemReader<EshopRelease> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopRelease e");
    reader.setName("eshopReleaseReader");
    return reader;
  }

}
