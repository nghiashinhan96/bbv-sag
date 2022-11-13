package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CollectionPermission;
import com.sagag.services.copydb.domain.dest.DestCollectionPermission;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CollectionPermissionProcessor implements ItemProcessor<CollectionPermission, DestCollectionPermission> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestCollectionPermission process(CollectionPermission item) throws Exception {
    return dozerBeanMapper.map(item, DestCollectionPermission.class);
  }
}
