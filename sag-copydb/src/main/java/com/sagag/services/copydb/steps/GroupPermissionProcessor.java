package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.GroupPermission;
import com.sagag.services.copydb.domain.dest.DestGroupPermission;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class GroupPermissionProcessor implements ItemProcessor<GroupPermission, DestGroupPermission> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestGroupPermission process(GroupPermission item) throws Exception {
    return dozerBeanMapper.map(item, DestGroupPermission.class);
  }
}
