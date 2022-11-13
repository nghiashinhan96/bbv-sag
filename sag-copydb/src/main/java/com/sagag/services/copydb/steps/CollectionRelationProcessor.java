package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.CollectionRelation;
import com.sagag.services.copydb.domain.dest.DestCollectionRelation;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class CollectionRelationProcessor implements ItemProcessor<CollectionRelation, DestCollectionRelation> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestCollectionRelation process(CollectionRelation item) throws Exception {
    return dozerBeanMapper.map(item, DestCollectionRelation.class);
  }
}
