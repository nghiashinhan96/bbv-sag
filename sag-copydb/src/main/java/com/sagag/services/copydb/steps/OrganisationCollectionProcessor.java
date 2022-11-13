package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationCollection;
import com.sagag.services.copydb.domain.dest.DestOrganisationCollection;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationCollectionProcessor implements ItemProcessor<OrganisationCollection, DestOrganisationCollection> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOrganisationCollection process(OrganisationCollection item) throws Exception {
    return dozerBeanMapper.map(item, DestOrganisationCollection.class);
  }
}
