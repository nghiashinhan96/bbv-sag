package com.sagag.services.dvse.api;

import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.wsdl.dvse.ArrayOfItem;
import com.sagag.services.dvse.wsdl.dvse.GetBackItems;

/**
 * <p>
 * The service for getting article information from SAG Connect API.
 * </p>
 */
public interface DvseArticleService {

  /**
   * Returns the DVSE response for article info request.
   *
   * @param user the current user.
   * @param items the list of requested items.
   * @return the result of {@link GetBackItems}
   */
  GetBackItems getArticleInformation(ConnectUser user, ArrayOfItem items);

}
