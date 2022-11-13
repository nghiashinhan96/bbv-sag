package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Country;
import com.sagag.services.copydb.domain.dest.DestCountry;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CountryCopySteps extends AbstractJobConfig {

  @Autowired
  private CountryProcessor countryProcessor;

  @Autowired
  private DestCountryWriter countryWriter;

  @Bean(name = "copyCountry")
  public Step copyCountry() {
    return stepBuilderFactory.get("copyCountry").<Country, DestCountry>chunk(DF_CHUNK)
        .reader(countryReader())
        .processor(countryProcessor)
        .writer(countryWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Country> countryReader() {
    final JpaPagingItemReader<Country> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Country e");
    reader.setName("countryReader");
    return reader;
  }

}
