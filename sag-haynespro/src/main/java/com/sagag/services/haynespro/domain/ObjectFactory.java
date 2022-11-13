package com.sagag.services.haynespro.domain;

import lombok.NoArgsConstructor;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.sagag.services.haynespro.wsdl package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation
 * for XML content. The Java representation of XML content can consist of schema derived interfaces
 * and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 *
 */
@NoArgsConstructor
@XmlRegistry
public class ObjectFactory {

  private static final String REGISTRATION_V2 = "http://registration.webservice.vivid.nl/v2";

  private static final QName _Key_QNAME = new QName("http://xml.apache.org/xml-soap", "key");

  private static final QName _Value_QNAME = new QName("http://xml.apache.org/xml-soap", "value");

  private static final QName _ClearStickyUserData_QNAME =
      new QName(REGISTRATION_V2, "clearStickyUserData");

  private static final QName _ClearStickyUserDataResponse_QNAME =
      new QName(REGISTRATION_V2, "clearStickyUserDataResponse");

  private static final QName _RegisterVisit_QNAME = new QName(REGISTRATION_V2, "registerVisit");

  private static final QName _RegisterVisitByDistributor_QNAME =
      new QName(REGISTRATION_V2, "registerVisitByDistributor");

  private static final QName _RegisterVisitByDistributorResponse_QNAME =
      new QName(REGISTRATION_V2, "registerVisitByDistributorResponse");

  private static final QName _RegisterVisitBySitekey_QNAME =
      new QName(REGISTRATION_V2, "registerVisitBySitekey");

  private static final QName _RegisterVisitBySitekeyResponse_QNAME =
      new QName(REGISTRATION_V2, "registerVisitBySitekeyResponse");

  private static final QName _RegisterVisitResponse_QNAME =
      new QName(REGISTRATION_V2, "registerVisitResponse");

  /**
   * Create an instance of {@link HaynesProMap }
   *
   */
  public HaynesProMap createMap() {
    return new HaynesProMap();
  }

  /**
   * Create an instance of {@link ClearStickyUserData }
   *
   */
  public ClearStickyUserData createClearStickyUserData() {
    return new ClearStickyUserData();
  }

  /**
   * Create an instance of {@link ClearStickyUserDataResponse }
   *
   */
  public ClearStickyUserDataResponse createClearStickyUserDataResponse() {
    return new ClearStickyUserDataResponse();
  }

  /**
   * Create an instance of {@link RegisterVisit }
   *
   */
  public RegisterVisit createRegisterVisit() {
    return new RegisterVisit();
  }

  /**
   * Create an instance of {@link RegisterVisitByDistributor }
   *
   */
  public RegisterVisitByDistributor createRegisterVisitByDistributor() {
    return new RegisterVisitByDistributor();
  }

  /**
   * Create an instance of {@link RegisterVisitByDistributorResponse }
   *
   */
  public RegisterVisitByDistributorResponse createRegisterVisitByDistributorResponse() {
    return new RegisterVisitByDistributorResponse();
  }

  /**
   * Create an instance of {@link RegisterVisitBySitekey }
   *
   */
  public RegisterVisitBySitekey createRegisterVisitBySitekey() {
    return new RegisterVisitBySitekey();
  }

  /**
   * Create an instance of {@link RegisterVisitBySitekeyResponse }
   *
   */
  public RegisterVisitBySitekeyResponse createRegisterVisitBySitekeyResponse() {
    return new RegisterVisitBySitekeyResponse();
  }

  /**
   * Create an instance of {@link RegisterVisitResponse }
   *
   */
  public RegisterVisitResponse createRegisterVisitResponse() {
    return new RegisterVisitResponse();
  }

  /**
   * Create an instance of {@link HaynesProItem }
   *
   */
  public HaynesProItem createItem() {
    return new HaynesProItem();
  }

  /**
   * Create an instance of {@link RegisterVisitResult }
   *
   */
  public RegisterVisitResult createRegisterVisitResult() {
    return new RegisterVisitResult();
  }

  /**
   * Create an instance of {@link com.sagag.services.haynespro.domain.RegistrationResult }
   *
   */
  public RegistrationResult createRegistrationResult() {
    return new RegistrationResult();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   *
   */
  @XmlElementDecl(namespace = "http://xml.apache.org/xml-soap", name = "key")
  public JAXBElement<String> createKey(String value) {
    return new JAXBElement<>(_Key_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   *
   */
  @XmlElementDecl(namespace = "http://xml.apache.org/xml-soap", name = "value")
  public JAXBElement<String> createValue(String value) {
    return new JAXBElement<>(_Value_QNAME, String.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ClearStickyUserData }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "clearStickyUserData")
  public JAXBElement<ClearStickyUserData> createClearStickyUserData(ClearStickyUserData value) {
    return new JAXBElement<>(_ClearStickyUserData_QNAME, ClearStickyUserData.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link ClearStickyUserDataResponse
   * }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "clearStickyUserDataResponse")
  public JAXBElement<ClearStickyUserDataResponse> createClearStickyUserDataResponse(
      ClearStickyUserDataResponse value) {
    return new JAXBElement<>(_ClearStickyUserDataResponse_QNAME, ClearStickyUserDataResponse.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link RegisterVisit }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "registerVisit")
  public JAXBElement<RegisterVisit> createRegisterVisit(RegisterVisit value) {
    return new JAXBElement<>(_RegisterVisit_QNAME, RegisterVisit.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link RegisterVisitByDistributor
   * }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "registerVisitByDistributor")
  public JAXBElement<RegisterVisitByDistributor> createRegisterVisitByDistributor(
      RegisterVisitByDistributor value) {
    return new JAXBElement<>(_RegisterVisitByDistributor_QNAME, RegisterVisitByDistributor.class,
        null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link RegisterVisitByDistributorResponse
   * }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "registerVisitByDistributorResponse")
  public JAXBElement<RegisterVisitByDistributorResponse> createRegisterVisitByDistributorResponse(
      RegisterVisitByDistributorResponse value) {
    return new JAXBElement<>(_RegisterVisitByDistributorResponse_QNAME,
        RegisterVisitByDistributorResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link RegisterVisitBySitekey }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "registerVisitBySitekey")
  public JAXBElement<RegisterVisitBySitekey> createRegisterVisitBySitekey(
      RegisterVisitBySitekey value) {
    return new JAXBElement<>(_RegisterVisitBySitekey_QNAME, RegisterVisitBySitekey.class, null,
        value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link RegisterVisitBySitekeyResponse
   * }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "registerVisitBySitekeyResponse")
  public JAXBElement<RegisterVisitBySitekeyResponse> createRegisterVisitBySitekeyResponse(
      RegisterVisitBySitekeyResponse value) {
    return new JAXBElement<>(_RegisterVisitBySitekeyResponse_QNAME,
        RegisterVisitBySitekeyResponse.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link RegisterVisitResponse }{@code >}}
   *
   */
  @XmlElementDecl(namespace = REGISTRATION_V2, name = "registerVisitResponse")
  public JAXBElement<RegisterVisitResponse> createRegisterVisitResponse(
      RegisterVisitResponse value) {
    return new JAXBElement<>(_RegisterVisitResponse_QNAME, RegisterVisitResponse.class, null,
        value);
  }

}
