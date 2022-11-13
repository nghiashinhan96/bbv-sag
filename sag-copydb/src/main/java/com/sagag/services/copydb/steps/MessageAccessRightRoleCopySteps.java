package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageAccessRightRole;
import com.sagag.services.copydb.domain.dest.DestMessageAccessRightRole;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageAccessRightRoleCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageAccessRightRoleProcessor messageAccessRightRoleProcessor;

  @Autowired
  private DestMessageAccessRightRoleWriter messageAccessRightRoleWriter;

  @Bean(name = "copyMessageAccessRightRole")
  public Step copyMessageAccessRightRole() {
    return stepBuilderFactory.get("copyMessageAccessRightRole").<MessageAccessRightRole, DestMessageAccessRightRole>chunk(DF_CHUNK)
        .reader(messageAccessRightRoleReader())
        .processor(messageAccessRightRoleProcessor)
        .writer(messageAccessRightRoleWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageAccessRightRole> messageAccessRightRoleReader() {
    final JpaPagingItemReader<MessageAccessRightRole> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageAccessRightRole e");
    reader.setName("messageAccessRightRoleReader");
    return reader;
  }

}
