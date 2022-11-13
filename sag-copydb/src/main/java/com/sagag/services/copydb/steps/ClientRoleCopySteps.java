package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ClientRole;
import com.sagag.services.copydb.domain.dest.DestClientRole;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ClientRoleCopySteps extends AbstractJobConfig {

  @Autowired
  private ClientRoleProcessor clientRoleProcessor;

  @Autowired
  private DestClientRoleWriter clientRoleWriter;

  @Bean(name = "copyClientRole")
  public Step copyClientRole() {
    return stepBuilderFactory.get("copyClientRole").<ClientRole, DestClientRole>chunk(DF_CHUNK)
        .reader(clientRoleReader())
        .processor(clientRoleProcessor)
        .writer(clientRoleWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<ClientRole> clientRoleReader() {
    final JpaPagingItemReader<ClientRole> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from ClientRole e");
    reader.setName("clientRoleReader");
    return reader;
  }

}
