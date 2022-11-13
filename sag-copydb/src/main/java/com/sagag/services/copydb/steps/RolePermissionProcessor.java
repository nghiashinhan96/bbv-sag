package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.RolePermission;
import com.sagag.services.copydb.domain.dest.DestRolePermission;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class RolePermissionProcessor implements ItemProcessor<RolePermission, DestRolePermission> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestRolePermission process(RolePermission item) throws Exception {
    return dozerBeanMapper.map(item, DestRolePermission.class);
  }
}
