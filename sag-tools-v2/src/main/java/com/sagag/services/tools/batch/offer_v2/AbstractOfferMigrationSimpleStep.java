package com.sagag.services.tools.batch.offer_v2;

import com.sagag.services.tools.service.MappingUserIdEblConnectService;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractOfferMigrationSimpleStep<I, O>
  extends AbstractOfferSimpleStep<I, O> {

  @Autowired
  protected MappingUserIdEblConnectService mappingUserIdEblConnectService;

}
