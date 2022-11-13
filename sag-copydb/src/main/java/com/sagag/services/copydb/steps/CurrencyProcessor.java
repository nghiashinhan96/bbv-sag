package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Currency;
import com.sagag.services.copydb.domain.dest.DestCurrency;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CurrencyProcessor implements ItemProcessor<Currency, DestCurrency> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestCurrency process(Currency item) throws Exception {
    return dozerBeanMapper.map(item, DestCurrency.class);
  }
}
