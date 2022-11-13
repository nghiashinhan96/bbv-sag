package com.sagag.services.gtmotive.client;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.gtmotive.app.GtmotiveVehicleAccountConfiguration;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.domain.request.GtVehicleInfo;
import com.sagag.services.gtmotive.domain.request.GtVehicleInfoRequest;
import com.sagag.services.gtmotive.domain.request.GtVehicleRequestInfo;
import com.sagag.services.gtmotive.domain.response.GtVehicleInfoResponse;
import com.sagag.services.gtmotive.wsdl.vehicle.ObjectFactory;
import com.sagag.services.gtmotive.wsdl.vehicle.VinDecoder;
import com.sagag.services.gtmotive.wsdl.vehicle.VinDecoderResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

@Component
@GtmotiveProfile
@Slf4j
public class GtmotiveVehicleClient {

  private static final String VEHICLE_INFORMATION_SERVICE_SOAP_ACTION =
      "GtMotive.WSGTI.DistributedServices.WSGtVehicleInformationService/IWSGtVehicleInformationService/VinDecoder";

  private static final SoapActionCallback VIN_DECODER_CALLBACK =
      new SoapActionCallback(VEHICLE_INFORMATION_SERVICE_SOAP_ACTION);

  @Autowired
  private GtmotiveVehicleAccountConfiguration gtmotiveVehicleAccountConfiguration;

  @Autowired
  @Qualifier("gtmotiveVehicleInfoWebServiceTemplate")
  private WebServiceTemplate gtmotiveVehicleInfoWebServiceTemplate;

  @Autowired
  private GtmotiveExchangeClient exchangeClient;

  public GtVehicleInfoResponse getVinDecoder(final String vinCode) {
    Assert.hasText(vinCode, "The given vin code must not be empty");

    final GtVehicleInfo vehicleInfo = GtVehicleInfo.builder().vin(vinCode).build();
    final GtVehicleRequestInfo requestInfo =
        GtVehicleRequestInfo.builder().vehicleInfo(vehicleInfo).build();
    final GtVehicleInfoRequest request = GtVehicleInfoRequest.builder()
        .authenticationData(gtmotiveVehicleAccountConfiguration.toAuthenticationData())
        .requestInfo(requestInfo).build();
    final String xmlRequest = XmlUtils.marshal(request);

    final ObjectFactory vehicleObjectFactory = new ObjectFactory();
    final VinDecoder vinDecoder = vehicleObjectFactory.createVinDecoder();
    vinDecoder.setStrXmlRequest(vehicleObjectFactory.createVinDecoderStrXmlRequest(xmlRequest));

    return doExecuteVinCoderSoapService(vinDecoder);
  }

  private GtVehicleInfoResponse doExecuteVinCoderSoapService(final VinDecoder request) {
    log.info(
        "GTMotive [WSGtVehicleInformationService][VinDecoder][GetVinDecode] Request at URL = {} with request = \n{}",
        gtmotiveVehicleAccountConfiguration.getUrl(), XmlUtils.marshalWithPrettyMode(request));

    final VinDecoderResponse response = exchangeClient.exchangeVinDecoderXml(
            gtmotiveVehicleAccountConfiguration.getUrl(), request, VIN_DECODER_CALLBACK);

    log.info("GTMotive [WSGtVehicleInformationService][VinDecoder][GetVinDecode] Response = {}",
        XmlUtils.marshalWithPrettyMode(response));

    final GtVehicleInfoResponse gtVehicleInfoResponse =
        XmlUtils.unmarshal(GtVehicleInfoResponse.class, response.getVinDecoderResult().getValue());

    log.info("Vin Decoder GtVehicleInfoResponse = \n{}",
        SagJSONUtil.convertObjectToPrettyJson(gtVehicleInfoResponse));

    return gtVehicleInfoResponse;
  }

}
