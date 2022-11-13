package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.GroupUser;
import com.sagag.services.copydb.domain.dest.DestGroupUser;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class GroupUserCopySteps extends AbstractJobConfig {

  @Autowired
  private GroupUserProcessor groupUserProcessor;

  @Autowired
  private DestGroupUserWriter groupUserWriter;

  @Bean(name = "copyGroupUser")
  public Step copyGroupUser() {
    return stepBuilderFactory.get("copyGroupUser").<GroupUser, DestGroupUser>chunk(DF_CHUNK)
        .reader(groupUserReader())
        .processor(groupUserProcessor)
        .writer(groupUserWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<GroupUser> groupUserReader() {
    final JpaPagingItemReader<GroupUser> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from GroupUser e");
    reader.setName("groupUserReader");
    return reader;
  }

}
