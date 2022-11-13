package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationProperty;
import com.sagag.services.copydb.domain.dest.DestOrganisationProperty;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationPropertyProcessor implements ItemProcessor<OrganisationProperty, DestOrganisationProperty> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOrganisationProperty process(OrganisationProperty item) throws Exception {
    return dozerBeanMapper.map(item, DestOrganisationProperty.class);
  }
}
