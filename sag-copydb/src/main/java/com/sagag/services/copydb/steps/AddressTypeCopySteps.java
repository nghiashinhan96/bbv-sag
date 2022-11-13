package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AddressType;
import com.sagag.services.copydb.domain.dest.DestAddressType;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AddressTypeCopySteps extends AbstractJobConfig {

  @Autowired
  private AddressTypeProcessor addressTypeProcessor;

  @Autowired
  private DestAddressTypeWriter addressTypeWriter;

  @Bean(name = "copyAddressType")
  public Step copyAddressType() {
    return stepBuilderFactory.get("copyAddressType").<AddressType, DestAddressType>chunk(DF_CHUNK)
        .reader(addressTypeReader())
        .processor(addressTypeProcessor)
        .writer(addressTypeWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<AddressType> addressTypeReader() {
    final JpaPagingItemReader<AddressType> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from AddressType e");
    reader.setName("addressTypeReader");
    return reader;
  }

}
