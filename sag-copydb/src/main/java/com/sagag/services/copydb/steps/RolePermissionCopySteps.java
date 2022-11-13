package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.RolePermission;
import com.sagag.services.copydb.domain.dest.DestRolePermission;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class RolePermissionCopySteps extends AbstractJobConfig {

  @Autowired
  private RolePermissionProcessor rolePermissionProcessor;

  @Autowired
  private DestRolePermissionWriter rolePermissionWriter;

  @Bean(name = "copyRolePermission")
  public Step copyRolePermission() {
    return stepBuilderFactory.get("copyRolePermission").<RolePermission, DestRolePermission>chunk(DF_CHUNK)
        .reader(rolePermissionReader())
        .processor(rolePermissionProcessor)
        .writer(rolePermissionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<RolePermission> rolePermissionReader() {
    final JpaPagingItemReader<RolePermission> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from RolePermission e");
    reader.setName("rolePermissionReader");
    return reader;
  }

}
