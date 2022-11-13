package com.sagag.services.stakis.erp.client;

import org.springframework.ws.soap.client.core.SoapActionCallback;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CisSoapAction {

  protected static final SoapActionCallback GET_CUSTOMER_SOAP_ACTION =
      new SoapActionCallback("DVSE.WebApp.CISService/GetCustomer");

  protected static final SoapActionCallback GET_VOUCHER_SOAP_ACTION =
  		new SoapActionCallback("DVSE.WebApp.CISService/GetVouchers");

  protected static final SoapActionCallback GET_VOUCHER_DETAIL_SOAP_ACTION =
  		new SoapActionCallback("DVSE.WebApp.CISService/GetVoucherDetails");

}
