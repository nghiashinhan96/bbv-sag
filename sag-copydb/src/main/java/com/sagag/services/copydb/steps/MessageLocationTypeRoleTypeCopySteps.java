package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MessageLocationTypeRoleType;
import com.sagag.services.copydb.domain.dest.DestMessageLocationTypeRoleType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MessageLocationTypeRoleTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private MessageLocationTypeRoleTypeProcessor messageLocationTypeRoleTypeProcessor;

  @Autowired
  private DestMessageLocationTypeRoleTypeWriter messageLocationTypeRoleTypeWriter;

  @Bean(name = "copyMessageLocationTypeRoleType")
  public Step copyMessageLocationTypeRoleType() {
    return stepBuilderFactory.get("copyMessageLocationTypeRoleType").<MessageLocationTypeRoleType, DestMessageLocationTypeRoleType>chunk(DF_CHUNK)
        .reader(messageLocationTypeRoleTypeReader())
        .processor(messageLocationTypeRoleTypeProcessor)
        .writer(messageLocationTypeRoleTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MessageLocationTypeRoleType> messageLocationTypeRoleTypeReader() {
    final JpaPagingItemReader<MessageLocationTypeRoleType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MessageLocationTypeRoleType e");
    reader.setName("messageLocationTypeRoleTypeReader");
    return reader;
  }

}
