package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopUser;
import com.sagag.services.copydb.domain.dest.DestEshopUser;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopUserCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopUserProcessor eshopUserProcessor;

  @Autowired
  private DestEshopUserWriter eshopUserWriter;

  @Bean(name = "copyEshopUser")
  public Step copyEshopUser() {
    return stepBuilderFactory.get("copyEshopUser").<EshopUser, DestEshopUser>chunk(DF_CHUNK)
        .reader(eshopUserReader())
        .processor(eshopUserProcessor)
        .writer(eshopUserWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopUser> eshopUserReader() {
    final JpaPagingItemReader<EshopUser> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopUser e");
    reader.setName("eshopUserReader");
    return reader;
  }

}
