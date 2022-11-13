package com.sagag.services.gtmotive.builder.gtinterface;

public class AbstractGtmotiveRequestBuilder {

  public static final String ATTR_LOCKED_FALSE = "\" locked=\"false\"/>";

  public static final String ELE_ITEM_CLOSE = "</item>";

  public static final String ELE_ITEM_OPEN = "<item>";

  protected static final String DF_REGISTRATION_NUMBER = "123CH";

  protected static final String TAG_END = "\"/>";

  protected StringBuilder soapHeader() {
    return new StringBuilder("<soapenv:Header/>");
  }

  protected StringBuilder endSoapEnvelope() {
    return new StringBuilder("</soapenv:Envelope>");
  }
}
