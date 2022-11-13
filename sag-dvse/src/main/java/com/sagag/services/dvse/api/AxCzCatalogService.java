package com.sagag.services.dvse.api;

import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationResponse;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrder;
import com.sagag.services.dvse.wsdl.tmconnect.SendOrderResponse;

public interface AxCzCatalogService {

  /**
   * Returns the article information.
   *
   * @param request the requested info
   * @return the result of {@link GetArticleInformationResponse}
   */
  GetErpInformationResponse getArticleInfos(GetErpInformation request);

  /**
   * Returns the added basket item information.
   *
   * @param order the added request item
   * @return the result of {@link SendOrderResponse}
   */
  SendOrderResponse addItemsToCart(SendOrder order);
}
