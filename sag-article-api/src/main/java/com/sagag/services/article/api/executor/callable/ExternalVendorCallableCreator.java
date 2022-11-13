package com.sagag.services.article.api.executor.callable;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.request.ExternalVendorRequestBody;
import com.sagag.services.common.executor.CallableCreator;

import java.util.List;

@FunctionalInterface
public interface ExternalVendorCallableCreator
  extends CallableCreator<ExternalVendorRequestBody, List<VendorDto>> {

}
