package com.sagag.services.gtmotive.client;

import com.sagag.services.common.aspect.LogExecutionTime;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.wsdl.vehicle.VinDecoder;
import com.sagag.services.gtmotive.wsdl.vehicle.VinDecoderResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Component
@GtmotiveProfile
public class GtmotiveExchangeClient {

  @Autowired
  @Qualifier("gtmotiveVehicleInfoWebServiceTemplate")
  private WebServiceTemplate gtmotiveVehicleInfoWebServiceTemplate;

  @Autowired
  protected RestTemplate restTemplate;

  @LogExecutionTime(
      value = "GTMotive [WSGtVehicleInformationService][VinDecoder][GetVinDecode] in {} ms",
      infoMode = true)
  public VinDecoderResponse exchangeVinDecoderXml(String url, VinDecoder request,
    SoapActionCallback callback) {
    return (VinDecoderResponse) gtmotiveVehicleInfoWebServiceTemplate
      .marshalSendAndReceive(url, request, callback);
  }

  public ResponseEntity<String> exchangeMainModuleXml(String url, HttpMethod method,
    HttpEntity<String> entity) {
    return restTemplate.exchange(url, method, entity, String.class);
  }
}
