package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveMultiPartSearchRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByGtInfoRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveReferenceSearchByPartCodesCriteria;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveMultiPartSearchResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;
import com.sagag.services.ivds.request.gtmotive.GtmotiveOperationRequest;
import com.sagag.services.ivds.response.GtmotiveResponse;
import com.sagag.services.service.resource.gtmotive.GtmotiveVehicleResource;
import com.sagag.services.service.response.gtmotive.GtmotivePartsListSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveReferenceSearchResponse;
import com.sagag.services.service.response.gtmotive.GtmotiveVehicleSearchResponse;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;

public interface GtmotiveBusinessService {

  /**
   * Returns the vehicle info by Gtmotive criteria.
   *
   * @param custNr the customer number.
   * @param criteria the gtmotive criteria
   * @return the optional of {@link GtmotiveVehicleResource}
   */
  Optional<GtmotiveVehicleResource> searchVehicleByGtmotive(String custNr,
      GtmotiveVehicleCriteria criteria);

  /**
   * Returns the selected parts from the Gtmotive operations.
   *
   * @param user the user who requests.
   * @param request the operation request.
   * @return a list of articles found with its correspondent vehicle.
   */
  GtmotiveResponse searchArticlesByGtOperations(UserInfo user, GtmotiveOperationRequest request);

  /**
   * Searches part references by GTMotive part-code.
   *
   * @param criteria input for search action
   * @return gtmotive response
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotiveReferenceSearchResponse searchReferencesByPartCode(
      GtmotiveReferenceSearchByPartCodesCriteria criteria)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException;

  /**
   * Searches vehicle by GTMotive information.
   *
   * @param custNr customer number
   * @param request input for search action
   * @return vehicle data
   */
  GtmotiveVehicleSearchResponse searchVehicleByGtInfo(String custNr,
      GtmotiveVehicleSearchByGtInfoRequest request);

  /**
   * Searches vehicle by VIN.
   *
   * @param custNr
   * @param request input for search action
   * @return vehicle data
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotiveVehicleSearchResponse searchVehicleByVin(String custNr,
      GtmotiveVehicleSearchByVinRequest request)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException;

  /**
   * Returns GtPartsList.
   *
   * @param searchRequest input for search action
   * @return GtPartsList
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotivePartsListSearchResponse searchGtPartsList(GtmotivePartsListSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException;

  /**
   * Check vin security.
   *
   * @param userId
   * @param custNr
   * @param criteria
   * @return
   * @throws GtmotiveXmlResponseProcessingException
   * @throws SocketTimeoutException
   * @throws ConnectTimeoutException
   */
  GtmotiveVinSecurityCheckResponse checkVinSecurity(Long userId, String custNr,
      GtmotiveVinSecurityCheckCriteria criteria) throws GtmotiveXmlResponseProcessingException,
      SocketTimeoutException, ConnectTimeoutException, ValidationException;

  /**
   * Search multi part.
   *
   * @param request
   * @return
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotiveMultiPartSearchResponse searchMultiPart(GtmotiveMultiPartSearchRequest request)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException;

}
