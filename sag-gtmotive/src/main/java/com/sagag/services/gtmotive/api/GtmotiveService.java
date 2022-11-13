package com.sagag.services.gtmotive.api;

import com.sagag.services.gtmotive.domain.request.GtmotiveVehicleCriteria;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveMultiPartSearchRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleInfoCriteria;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByGtInfoRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVinSecurityCheckCriteria;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotiveEquipmentOptionsSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsListSearchRequest;
import com.sagag.services.gtmotive.dto.request.mainmoduleservice.GtmotivePartsThreeSearchRequest;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveMultiPartSearchResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVehicleInfoResponse;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVinSecurityCheckResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotiveEquipmentOptionsResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsListResponse;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;

public interface GtmotiveService {

  /**
   * Returns the make code from Vin Decoder by Gtmotive.
   *
   * @param vin the vin code.
   * @return the found make code.
   */
  String getMakeCodeFromVinDecoder(String vin);

  /**
   * Returns GTMotive vehicle info.
   *
   * @param custNr the customer number
   * @param criteria the search criteria
   * @return the optional of {@link GtmotiveVehicleDto}
   */
  Optional<GtmotiveVehicleDto> getVehicleInfo(String custNr, GtmotiveVehicleCriteria criteria);


  /**
   * Returns PartsThree response base on search request model.
   *
   * @param searchRequest input data for search action
   * @return PartsThree response
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotivePartsThreeResponse searchReferencesByPartCode(
      GtmotivePartsThreeSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException;

  /**
   * Returns PartList response base on search request model.
   *
   * @param searchRequest input data for search action
   * @return PartsList response
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotivePartsListResponse searchVehiclePartsList(GtmotivePartsListSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException;

  /**
   * Returns vehicle info base on gt-info.
   *
   * @param custNr the customer number
   * @param request the criteria for search action
   * @return the optional of {@link GtmotiveVehicleDto}
   */
  Optional<GtmotiveVehicleDto> searchVehicleInfoByGtInfo(String custNr,
      GtmotiveVehicleSearchByGtInfoRequest request);

  /**
   * Returns vehicle info base on VIN.
   *
   * @param request the criteria for search action
   * @return the optional of {@link GtmotiveVehicleDto}
   */
  Optional<GtmotiveVehicleDto> searchVehicleByVin(GtmotiveVehicleSearchByVinRequest request);

  GtmotiveVehicleInfoResponse getVehicleInfo(GtmotiveVehicleInfoCriteria criteria);

  /**
   * Checks VIN security.
   *
   * @param criteria input for checking process
   * @return
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotiveVinSecurityCheckResponse checkVinSecurity(final GtmotiveVinSecurityCheckCriteria criteria)
      throws GtmotiveXmlResponseProcessingException;

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

  /**
   * Registers new estimateId.
   *
   * @param criteria
   */
  void registerNewEstimateId(final GtmotiveVehicleInfoCriteria criteria);

  /**
   * Returns Equipment options for specific part code.
   *
   * @param searchRequest
   * @return
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotiveEquipmentOptionsResponse searchEquipmentOptions(
      GtmotiveEquipmentOptionsSearchRequest searchRequest)
      throws XPathExpressionException, GtmotiveXmlResponseProcessingException;
}
