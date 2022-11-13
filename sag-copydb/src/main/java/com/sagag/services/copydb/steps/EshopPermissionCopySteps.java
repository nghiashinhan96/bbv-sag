package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopPermission;
import com.sagag.services.copydb.domain.dest.DestEshopPermission;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopPermissionCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopPermissionProcessor eshopPermissionProcessor;

  @Autowired
  private DestEshopPermissionWriter eshopPermissionWriter;

  @Bean(name = "copyEshopPermission")
  public Step copyEshopPermission() {
    return stepBuilderFactory.get("copyEshopPermission").<EshopPermission, DestEshopPermission>chunk(DF_CHUNK)
        .reader(eshopPermissionReader())
        .processor(eshopPermissionProcessor)
        .writer(eshopPermissionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopPermission> eshopPermissionReader() {
    final JpaPagingItemReader<EshopPermission> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopPermission e");
    reader.setName("eshopPermissionReader");
    return reader;
  }

}
