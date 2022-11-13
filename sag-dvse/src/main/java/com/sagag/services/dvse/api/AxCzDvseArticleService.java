package com.sagag.services.dvse.api;

import com.sagag.services.dvse.dto.dvse.ConnectUser;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformation;
import com.sagag.services.dvse.wsdl.tmconnect.GetErpInformationResponse;

/**
 * <p>
 * The service for getting article information from SAG Connect API using for DVSE TMConnect.
 * </p>
 */
public interface AxCzDvseArticleService {

  GetErpInformationResponse getArticleInformation(ConnectUser user, GetErpInformation request);

}
