package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MappingUserIdEblConnect;
import com.sagag.services.copydb.domain.dest.DestMappingUserIdEblConnect;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MappingUserIdEblConnectCopySteps extends AbstractJobConfig {

  @Autowired
  private MappingUserIdEblConnectProcessor mappingUserIdEblConnectProcessor;

  @Autowired
  private DestMappingUserIdEblConnectWriter mappingUserIdEblConnectWriter;

  @Bean(name = "copyMappingUserIdEblConnect")
  public Step copyMappingUserIdEblConnect() {
    return stepBuilderFactory.get("copyMappingUserIdEblConnect").<MappingUserIdEblConnect, DestMappingUserIdEblConnect>chunk(DF_CHUNK)
        .reader(mappingUserIdEblConnectReader())
        .processor(mappingUserIdEblConnectProcessor)
        .writer(mappingUserIdEblConnectWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<MappingUserIdEblConnect> mappingUserIdEblConnectReader() {
    final JpaPagingItemReader<MappingUserIdEblConnect> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from MappingUserIdEblConnect e");
    reader.setName("mappingUserIdEblConnectReader");
    return reader;
  }

}
