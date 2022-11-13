package com.sagag.services.article.api.executor;

import com.sagag.services.article.api.domain.vendor.VendorDto;

import java.util.List;

public interface ExternalVendorSearchExecutor {

  /**
   * Returns external vendors by article id list.
   *
   * @param companyName
   * @param articleIds
   * @return the list of external vendors
   */
  List<VendorDto> execute(String companyName, List<String> articleIds);
}
