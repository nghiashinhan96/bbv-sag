package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationType;
import com.sagag.services.copydb.domain.dest.DestOrganisationType;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationTypeProcessor implements ItemProcessor<OrganisationType, DestOrganisationType> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOrganisationType process(OrganisationType item) throws Exception {
    return dozerBeanMapper.map(item, DestOrganisationType.class);
  }
}
