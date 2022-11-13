package com.sagag.services.dvse.api;

import com.sagag.services.dvse.wsdl.dvse.GetArticleInformation;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformationResponse;
import com.sagag.services.dvse.wsdl.dvse.SendOrder;
import com.sagag.services.dvse.wsdl.dvse.SendOrderResponse;

/**
 * The service to wrap up the all specific logic and return to end point.
 *
 */
public interface CatalogService {

  /**
   * Returns the article information.
   *
   * @param request the requested info
   * @return the result of {@link GetArticleInformationResponse}
   */
  GetArticleInformationResponse getArticleInfos(GetArticleInformation request);

  /**
   * Returns the added basket item information.
   *
   * @param order the added request item
   * @return the result of {@link SendOrderResponse}
   */
  SendOrderResponse addItemsToCart(SendOrder order);

}
