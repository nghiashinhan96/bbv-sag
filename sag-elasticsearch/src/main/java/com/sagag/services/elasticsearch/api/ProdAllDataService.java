package com.sagag.services.elasticsearch.api;

import com.sagag.services.elasticsearch.domain.alldata.ProdAllDataDoc;
import java.util.List;

public interface ProdAllDataService {

  /**
   * Returns the all data of articles info by text.
   *
   * @param text the search criteria
   * @return the list of result
   */
  List<ProdAllDataDoc> searchAllDataByText(String text);

}
