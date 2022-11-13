package com.sagag.services.dvse.api;

import com.sagag.services.dvse.wsdl.unicat.SendOrderResponse;
import com.sagag.services.dvse.wsdl.unicat.GetArticleInformationResponse;
import com.sagag.services.dvse.wsdl.unicat.SendOrder;
import com.sagag.services.dvse.wsdl.unicat.GetArticleInformations;


/**
 * The service to wrap up the all specific logic and return to end point UNICAT.
 *
 */
public interface UnicatCatalogService {

  GetArticleInformationResponse getArticleInfos(GetArticleInformations request);

  SendOrderResponse addItemsToCart(SendOrder request);


}
