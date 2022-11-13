package com.sagag.services.dvse.api;

import java.util.List;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.dvse.wsdl.unicat.GetBackItems;
import com.sagag.services.dvse.wsdl.unicat.UnicatItem;
/**
 * <p>
 * The service for getting article information from Stakis ERP.
 * </p>
 */
public interface UnicatArticleService {


  /**
   * Returns the UNICAT response for article info request.
   *
   * @param userInfo
   * @param itemsInOrder
   * @return
   */
  GetBackItems getArticleInformation(UserInfo userInfo, List<UnicatItem> itemsInOrder);


}
