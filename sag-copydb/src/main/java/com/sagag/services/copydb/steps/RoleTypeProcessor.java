package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.RoleType;
import com.sagag.services.copydb.domain.dest.DestRoleType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class RoleTypeProcessor implements ItemProcessor<RoleType, DestRoleType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestRoleType process(RoleType item) throws Exception {
    return dozerBeanMapper.map(item, DestRoleType.class);
  }
}
