package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.GroupRole;
import com.sagag.services.copydb.domain.dest.DestGroupRole;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class GroupRoleCopySteps extends AbstractJobConfig {

  @Autowired
  private GroupRoleProcessor groupRoleProcessor;

  @Autowired
  private DestGroupRoleWriter groupRoleWriter;

  @Bean(name = "copyGroupRole")
  public Step copyGroupRole() {
    return stepBuilderFactory.get("copyGroupRole").<GroupRole, DestGroupRole>chunk(DF_CHUNK)
        .reader(groupRoleReader())
        .processor(groupRoleProcessor)
        .writer(groupRoleWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<GroupRole> groupRoleReader() {
    final JpaPagingItemReader<GroupRole> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from GroupRole e");
    reader.setName("groupRoleReader");
    return reader;
  }

}
