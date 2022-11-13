package com.sagag.services.tools.batch.offer_v2;

import com.sagag.services.tools.batch.offer_v2.offer.MissingOfferFromEblProcessor;
import com.sagag.services.tools.service.MappingUserIdEblConnectService;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMissingOfferSimpleStep<I, O>
  extends AbstractOfferSimpleStep<I, O> {

  @Autowired
  protected MissingOfferFromEblProcessor missingOfferFromEblProcessor;

  @Autowired
  protected MappingUserIdEblConnectService mappingUserIdEblConnectService;

}
