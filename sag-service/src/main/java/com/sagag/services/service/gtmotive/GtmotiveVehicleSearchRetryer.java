package com.sagag.services.service.gtmotive;

import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;

public interface GtmotiveVehicleSearchRetryer {

  /**
   * Retries when has error in response.
   *
   * @param response the response need to handle
   * @param criteria
   * @return the result of <code>GtmotiveVinSecurityCheckResponse</code>
   * @throws GtmotiveXmlResponseProcessingException
   * @throws SocketTimeoutException
   * @throws org.apache.http.conn.ConnectTimeoutException
   * @throws com.sagag.services.common.exception.ValidationException
   */
  GtmotiveVinSecurityCheckResponse retryWhenError(GtmotiveVinSecurityCheckResponse response,
      final GtmotiveVinSecurityCheckCriteria criteria) throws GtmotiveXmlResponseProcessingException,
      SocketTimeoutException, ConnectTimeoutException, ValidationException;
}
