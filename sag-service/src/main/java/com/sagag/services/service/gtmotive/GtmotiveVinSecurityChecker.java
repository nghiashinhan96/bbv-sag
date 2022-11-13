package com.sagag.services.service.gtmotive;

import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;

public interface GtmotiveVinSecurityChecker {

  /**
   * Checks VIN security.
   *
   * @param userId
   * @param custNr
   * @param criteria
   * @return the result of VIN security
   * @throws GtmotiveXmlResponseProcessingException
   * @throws SocketTimeoutException
   * @throws ConnectTimeoutException
   * @throws ValidationException
   */
  GtmotiveVinSecurityCheckResponse check(Long userId, String custNr,
      GtmotiveVinSecurityCheckCriteria criteria) throws GtmotiveXmlResponseProcessingException,
      SocketTimeoutException, ConnectTimeoutException, ValidationException;
}
