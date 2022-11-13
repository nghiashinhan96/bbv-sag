package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.MappingUserIdEblConnect;
import com.sagag.services.copydb.domain.dest.DestMappingUserIdEblConnect;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class MappingUserIdEblConnectProcessor implements ItemProcessor<MappingUserIdEblConnect, DestMappingUserIdEblConnect> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestMappingUserIdEblConnect process(MappingUserIdEblConnect item) throws Exception {
    return dozerBeanMapper.map(item, DestMappingUserIdEblConnect.class);
  }
}
