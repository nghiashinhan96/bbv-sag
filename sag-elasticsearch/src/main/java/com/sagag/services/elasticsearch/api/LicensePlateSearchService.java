package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.vehicle.licenseplate.LicensePlateDoc;
import java.util.List;

public interface LicensePlateSearchService {

  /**
   * Returns list of license plate by text.
   *
   * @param text the text to search
   * @return the list of result
   */
  List<LicensePlateDoc> searchLicensePlateByText(String text);

}
