package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.RoleType;
import com.sagag.services.copydb.domain.dest.DestRoleType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class RoleTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private RoleTypeProcessor roleTypeProcessor;

  @Autowired
  private DestRoleTypeWriter roleTypeWriter;

  @Bean(name = "copyRoleType")
  public Step copyRoleType() {
    return stepBuilderFactory.get("copyRoleType").<RoleType, DestRoleType>chunk(DF_CHUNK)
        .reader(roleTypeReader())
        .processor(roleTypeProcessor)
        .writer(roleTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<RoleType> roleTypeReader() {
    final JpaPagingItemReader<RoleType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from RoleType e");
    reader.setName("roleTypeReader");
    return reader;
  }

}
