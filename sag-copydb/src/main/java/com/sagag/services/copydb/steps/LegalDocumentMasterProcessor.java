package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.LegalDocumentMaster;
import com.sagag.services.copydb.domain.dest.DestLegalDocumentMaster;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LegalDocumentMasterProcessor implements ItemProcessor<LegalDocumentMaster, DestLegalDocumentMaster> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestLegalDocumentMaster process(LegalDocumentMaster item) throws Exception {
    return dozerBeanMapper.map(item, DestLegalDocumentMaster.class);
  }
}
