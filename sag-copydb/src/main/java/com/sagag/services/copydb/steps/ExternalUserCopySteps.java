package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ExternalUser;
import com.sagag.services.copydb.domain.dest.DestExternalUser;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ExternalUserCopySteps extends AbstractJobConfig {

  @Autowired
  private ExternalUserProcessor externalUserProcessor;

  @Autowired
  private DestExternalUserWriter externalUserWriter;

  @Bean(name = "copyExternalUser")
  public Step copyExternalUser() {
    return stepBuilderFactory.get("copyExternalUser").<ExternalUser, DestExternalUser>chunk(DF_CHUNK)
        .reader(externalUserReader())
        .processor(externalUserProcessor)
        .writer(externalUserWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<ExternalUser> externalUserReader() {
    final JpaPagingItemReader<ExternalUser> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from ExternalUser e");
    reader.setName("externalUserReader");
    return reader;
  }

}
