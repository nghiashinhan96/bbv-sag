package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AllocationType;
import com.sagag.services.copydb.domain.dest.DestAllocationType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AllocationTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private AllocationTypeProcessor allocationTypeProcessor;

  @Autowired
  private DestAllocationTypeWriter allocationTypeWriter;

  @Bean(name = "copyAllocationType")
  public Step copyAllocationType() {
    return stepBuilderFactory.get("copyAllocationType").<AllocationType, DestAllocationType>chunk(DF_CHUNK)
        .reader(allocationTypeReader())
        .processor(allocationTypeProcessor)
        .writer(allocationTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<AllocationType> allocationTypeReader() {
    final JpaPagingItemReader<AllocationType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from AllocationType e");
    reader.setName("allocationTypeReader");
    return reader;
  }

}
