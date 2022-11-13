package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.GroupUser;
import com.sagag.services.copydb.domain.dest.DestGroupUser;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class GroupUserProcessor implements ItemProcessor<GroupUser, DestGroupUser> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestGroupUser process(GroupUser item) throws Exception {
    return dozerBeanMapper.map(item, DestGroupUser.class);
  }
}
