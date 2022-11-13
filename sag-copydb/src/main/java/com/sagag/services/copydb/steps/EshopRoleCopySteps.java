package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.EshopRole;
import com.sagag.services.copydb.domain.dest.DestEshopRole;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class EshopRoleCopySteps extends AbstractJobConfig {

  @Autowired
  private EshopRoleProcessor eshopRoleProcessor;

  @Autowired
  private DestEshopRoleWriter eshopRoleWriter;

  @Bean(name = "copyEshopRole")
  public Step copyEshopRole() {
    return stepBuilderFactory.get("copyEshopRole").<EshopRole, DestEshopRole>chunk(DF_CHUNK)
        .reader(eshopRoleReader())
        .processor(eshopRoleProcessor)
        .writer(eshopRoleWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<EshopRole> eshopRoleReader() {
    final JpaPagingItemReader<EshopRole> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from EshopRole e");
    reader.setName("eshopRoleReader");
    return reader;
  }

}
