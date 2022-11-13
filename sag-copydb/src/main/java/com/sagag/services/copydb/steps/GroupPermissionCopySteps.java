package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.GroupPermission;
import com.sagag.services.copydb.domain.dest.DestGroupPermission;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class GroupPermissionCopySteps extends AbstractJobConfig {

  @Autowired
  private GroupPermissionProcessor groupPermissionProcessor;

  @Autowired
  private DestGroupPermissionWriter groupPermissionWriter;

  @Bean(name = "copyGroupPermission")
  public Step copyGroupPermission() {
    return stepBuilderFactory.get("copyGroupPermission").<GroupPermission, DestGroupPermission>chunk(DF_CHUNK)
        .reader(groupPermissionReader())
        .processor(groupPermissionProcessor)
        .writer(groupPermissionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<GroupPermission> groupPermissionReader() {
    final JpaPagingItemReader<GroupPermission> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from GroupPermission e");
    reader.setName("groupPermissionReader");
    return reader;
  }

}
