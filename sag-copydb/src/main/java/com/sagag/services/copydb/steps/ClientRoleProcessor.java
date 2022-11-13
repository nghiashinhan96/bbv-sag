package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ClientRole;
import com.sagag.services.copydb.domain.dest.DestClientRole;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ClientRoleProcessor implements ItemProcessor<ClientRole, DestClientRole> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestClientRole process(ClientRole item) throws Exception {
    return dozerBeanMapper.map(item, DestClientRole.class);
  }
}
