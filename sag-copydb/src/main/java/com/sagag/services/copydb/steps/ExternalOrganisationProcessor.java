package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.ExternalOrganisation;
import com.sagag.services.copydb.domain.dest.DestExternalOrganisation;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class ExternalOrganisationProcessor implements ItemProcessor<ExternalOrganisation, DestExternalOrganisation> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestExternalOrganisation process(ExternalOrganisation item) throws Exception {
    return dozerBeanMapper.map(item, DestExternalOrganisation.class);
  }
}
