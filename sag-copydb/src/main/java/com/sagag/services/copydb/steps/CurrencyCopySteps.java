package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.batch.AbstractJobConfig;
import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Currency;
import com.sagag.services.copydb.domain.dest.DestCurrency;
import com.sagag.services.copydb.utils.Constants;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CurrencyCopySteps extends AbstractJobConfig {

  @Autowired
  private CurrencyProcessor currencyProcessor;

  @Autowired
  private DestCurrencyWriter currencyWriter;

  @Bean(name = "copyCurrency")
  public Step copyCurrency() {
    return stepBuilderFactory.get("copyCurrency").<Currency, DestCurrency>chunk(DF_CHUNK)
        .reader(currencyReader())
        .processor(currencyProcessor)
        .writer(currencyWriter)
        .transactionManager(targetTransactionManager)
        .build();
  }

  @Bean
  @StepScope
  public JpaPagingItemReader<Currency> currencyReader() {
    final JpaPagingItemReader<Currency> reader = new JpaPagingItemReader<>();
    reader.setTransacted(true);
    reader.setEntityManagerFactory(getEntityManagerFactory());
    reader.setPageSize(Constants.MAX_PAGE_SIZE);
    reader.setQueryString("select e from Currency e");
    reader.setName("currencyReader");
    return reader;
  }

}
