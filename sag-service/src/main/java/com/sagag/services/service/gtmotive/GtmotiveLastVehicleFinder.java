package com.sagag.services.service.gtmotive;

import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleDto;
import com.sagag.services.gtmotive.dto.request.gtinterface.GtmotiveVehicleSearchByVinRequest;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import javax.xml.xpath.XPathExpressionException;

public interface GtmotiveLastVehicleFinder {

  /**
   * Returns laster vehicle in case that estimateid is outdated.
   *
   * @param gtResponse
   * @param request
   * @param custNr
   * @return GtmotiveXmlResponseProcessingException
   * @throws XPathExpressionException
   * @throws GtmotiveXmlResponseProcessingException
   */
  GtmotiveVehicleDto find(GtmotiveVehicleDto gtResponse, GtmotiveVehicleSearchByVinRequest request,
      String custNr) throws XPathExpressionException, GtmotiveXmlResponseProcessingException;
}
