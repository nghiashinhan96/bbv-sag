package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Salutation;
import com.sagag.services.copydb.domain.dest.DestSalutation;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class SalutationCopySteps extends AbstractJobConfig {

  @Autowired
  private SalutationProcessor salutationProcessor;

  @Autowired
  private DestSalutationWriter salutationWriter;

  @Bean(name = "copySalutation")
  public Step copySalutation() {
    return stepBuilderFactory.get("copySalutation").<Salutation, DestSalutation>chunk(DF_CHUNK)
        .reader(salutationReader())
        .processor(salutationProcessor)
        .writer(salutationWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Salutation> salutationReader() {
    final JpaPagingItemReader<Salutation> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Salutation e");
    reader.setName("salutationReader");
    return reader;
  }

}
