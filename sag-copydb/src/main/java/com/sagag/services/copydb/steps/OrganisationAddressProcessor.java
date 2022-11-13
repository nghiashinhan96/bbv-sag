package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.OrganisationAddress;
import com.sagag.services.copydb.domain.dest.DestOrganisationAddress;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class OrganisationAddressProcessor implements ItemProcessor<OrganisationAddress, DestOrganisationAddress> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestOrganisationAddress process(OrganisationAddress item) throws Exception {
    return dozerBeanMapper.map(item, DestOrganisationAddress.class);
  }
}
