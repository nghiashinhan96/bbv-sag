package com.sagag.services.stakis.erp.exception;

import com.sagag.services.common.exceptionhandler.IExternalExceptionResolver;
import com.sagag.services.stakis.erp.wsdl.cis.BaseResponse;

public class StakisCisExceptionHandler implements IExternalExceptionResolver<BaseResponse> {

  @Override
  public void resolve(BaseResponse exResponse) {
    if (exResponse == null) {
      throw new IllegalArgumentException("Not found any response for verify error");
    }
    if (exResponse.getStateId() == 0) {
      return;
    }
    String errorMessage = exResponse.getErrorMessage().getValue();
    String information = exResponse.getInformation().getValue();
    throw new IllegalArgumentException(
        String.format("Statkis exception message = %s and information = %s",
        errorMessage, information));
  }

}
