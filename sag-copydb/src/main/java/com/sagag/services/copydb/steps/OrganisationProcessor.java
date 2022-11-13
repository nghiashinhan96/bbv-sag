package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.Organisation;
import com.sagag.services.copydb.domain.dest.DestOrganisation;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationProcessor implements ItemProcessor<Organisation, DestOrganisation> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOrganisation process(Organisation item) throws Exception {
    return dozerBeanMapper.map(item, DestOrganisation.class);
  }
}
