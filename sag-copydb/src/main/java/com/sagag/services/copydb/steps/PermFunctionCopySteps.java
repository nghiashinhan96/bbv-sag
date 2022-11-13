package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.PermFunction;
import com.sagag.services.copydb.domain.dest.DestPermFunction;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class PermFunctionCopySteps extends AbstractJobConfig {

  @Autowired
  private PermFunctionProcessor permFunctionProcessor;

  @Autowired
  private DestPermFunctionWriter permFunctionWriter;

  @Bean(name = "copyPermFunction")
  public Step copyPermFunction() {
    return stepBuilderFactory.get("copyPermFunction").<PermFunction, DestPermFunction>chunk(DF_CHUNK)
        .reader(permFunctionReader())
        .processor(permFunctionProcessor)
        .writer(permFunctionWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<PermFunction> permFunctionReader() {
    final JpaPagingItemReader<PermFunction> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from PermFunction e");
    reader.setName("permFunctionReader");
    return reader;
  }

}
