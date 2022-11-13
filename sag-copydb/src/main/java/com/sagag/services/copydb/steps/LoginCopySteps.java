package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Login;
import com.sagag.services.copydb.domain.dest.DestLogin;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LoginCopySteps extends AbstractJobConfig {

  @Autowired
  private LoginProcessor loginProcessor;

  @Autowired
  private DestLoginWriter loginWriter;

  @Bean(name = "copyLogin")
  public Step copyLogin() {
    return stepBuilderFactory.get("copyLogin").<Login, DestLogin>chunk(DF_CHUNK)
        .reader(loginReader())
        .processor(loginProcessor)
        .writer(loginWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Login> loginReader() {
    final JpaPagingItemReader<Login> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Login e");
    reader.setName("loginReader");
    return reader;
  }

}
