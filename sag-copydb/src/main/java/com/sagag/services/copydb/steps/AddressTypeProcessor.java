package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.AddressType;
import com.sagag.services.copydb.domain.dest.DestAddressType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class AddressTypeProcessor implements ItemProcessor<AddressType, DestAddressType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestAddressType process(AddressType item) throws Exception {
    return dozerBeanMapper.map(item, DestAddressType.class);
  }
}
