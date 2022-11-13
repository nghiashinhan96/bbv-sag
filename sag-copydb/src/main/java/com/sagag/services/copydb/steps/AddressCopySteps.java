package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Address;
import com.sagag.services.copydb.domain.dest.DestAddress;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AddressCopySteps extends AbstractJobConfig {

  @Autowired
  private AddressProcessor addressProcessor;

  @Autowired
  private DestAddressWriter addressWriter;

  @Bean(name = "copyAddress")
  public Step copyAddress() {
    return stepBuilderFactory.get("copyAddress").<Address, DestAddress>chunk(DF_CHUNK)
        .reader(addressReader())
        .processor(addressProcessor)
        .writer(addressWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Address> addressReader() {
    final JpaPagingItemReader<Address> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Address e");
    reader.setName("addressReader");
    return reader;
  }

}
