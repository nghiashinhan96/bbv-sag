package com.sagag.services.stakis.erp.client;

import org.springframework.ws.soap.client.core.SoapActionCallback;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TmConnectSoapAction {

  public static final SoapActionCallback SOAP_ACTION_GET_ERP_INFORMATION =
      new SoapActionCallback("http://topmotive.eu/TMConnect/ITMConnectContract/GetErpInformation");

  public static final SoapActionCallback SOAP_ACTION_SEND_ORDER =
      new SoapActionCallback("http://topmotive.eu/TMConnect/ITMConnectContract/SendOrder");
}
