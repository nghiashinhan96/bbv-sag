package com.sagag.services.copydb.steps;

import com.sagag.services.copydb.config.CopyDbProfile;
import com.sagag.services.copydb.domain.src.LegalDocumentCustomerAcceptedLog;
import com.sagag.services.copydb.domain.dest.DestLegalDocumentCustomerAcceptedLog;

import org.dozer.DozerBeanMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@CopyDbProfile
public class LegalDocumentCustomerAcceptedLogProcessor implements ItemProcessor<LegalDocumentCustomerAcceptedLog, DestLegalDocumentCustomerAcceptedLog> {

  @Autowired
  @Qualifier(value = "dozerBeanMapper")
  private DozerBeanMapper dozerBeanMapper;

  @Override
  public DestLegalDocumentCustomerAcceptedLog process(LegalDocumentCustomerAcceptedLog item) throws Exception {
    return dozerBeanMapper.map(item, DestLegalDocumentCustomerAcceptedLog.class);
  }
}
