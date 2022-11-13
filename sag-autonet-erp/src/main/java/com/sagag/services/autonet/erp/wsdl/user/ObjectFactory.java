
package com.sagag.services.autonet.erp.wsdl.user;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.sagag.services.autonet.erp.wsdl.tmconnect.GetUserReplyType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.SidRequestType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sagag.services.autonet.erp.wsdl.user package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SetSidRequest_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "SetSidRequest");
    private final static QName _GetUserResponseBody_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "GetUserResponseBody");
    private final static QName _SetSidRequestBody_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "SetSidRequestBody");
    private final static QName _GetUserResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "GetUserResponse");
    private final static QName _GetVersionRequestBody_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "GetVersionRequestBody");
    private final static QName _SetSidRequestTypeBody_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "Body");
    private final static QName _SetSidRequestBodyTypeRequest_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "request");
    private final static QName _GetUserResponseBodyTypeGetUserResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/", "GetUserResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sagag.services.autonet.erp.wsdl.user
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SetSidRequestType }
     * 
     */
    public SetSidRequestType createSetSidRequestType() {
        return new SetSidRequestType();
    }

    /**
     * Create an instance of {@link GetVersionRequestBodyType }
     * 
     */
    public GetVersionRequestBodyType createGetVersionRequestBodyType() {
        return new GetVersionRequestBodyType();
    }

    /**
     * Create an instance of {@link GetUserResponseType }
     * 
     */
    public GetUserResponseType createGetUserResponseType() {
        return new GetUserResponseType();
    }

    /**
     * Create an instance of {@link SetSidRequestBodyType }
     * 
     */
    public SetSidRequestBodyType createSetSidRequestBodyType() {
        return new SetSidRequestBodyType();
    }

    /**
     * Create an instance of {@link GetUserResponseBodyType }
     * 
     */
    public GetUserResponseBodyType createGetUserResponseBodyType() {
        return new GetUserResponseBodyType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSidRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "SetSidRequest")
    public JAXBElement<SetSidRequestType> createSetSidRequest(SetSidRequestType value) {
        return new JAXBElement<SetSidRequestType>(_SetSidRequest_QNAME, SetSidRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponseBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "GetUserResponseBody")
    public JAXBElement<GetUserResponseBodyType> createGetUserResponseBody(GetUserResponseBodyType value) {
        return new JAXBElement<GetUserResponseBodyType>(_GetUserResponseBody_QNAME, GetUserResponseBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSidRequestBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "SetSidRequestBody")
    public JAXBElement<SetSidRequestBodyType> createSetSidRequestBody(SetSidRequestBodyType value) {
        return new JAXBElement<SetSidRequestBodyType>(_SetSidRequestBody_QNAME, SetSidRequestBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "GetUserResponse")
    public JAXBElement<GetUserResponseType> createGetUserResponse(GetUserResponseType value) {
        return new JAXBElement<GetUserResponseType>(_GetUserResponse_QNAME, GetUserResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionRequestBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "GetVersionRequestBody")
    public JAXBElement<GetVersionRequestBodyType> createGetVersionRequestBody(GetVersionRequestBodyType value) {
        return new JAXBElement<GetVersionRequestBodyType>(_GetVersionRequestBody_QNAME, GetVersionRequestBodyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetSidRequestBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "Body", scope = SetSidRequestType.class)
    public JAXBElement<SetSidRequestBodyType> createSetSidRequestTypeBody(SetSidRequestBodyType value) {
        return new JAXBElement<SetSidRequestBodyType>(_SetSidRequestTypeBody_QNAME, SetSidRequestBodyType.class, SetSidRequestType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SidRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "request", scope = SetSidRequestBodyType.class)
    public JAXBElement<SidRequestType> createSetSidRequestBodyTypeRequest(SidRequestType value) {
        return new JAXBElement<SidRequestType>(_SetSidRequestBodyTypeRequest_QNAME, SidRequestType.class, SetSidRequestBodyType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserResponseBodyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "Body", scope = GetUserResponseType.class)
    public JAXBElement<GetUserResponseBodyType> createGetUserResponseTypeBody(GetUserResponseBodyType value) {
        return new JAXBElement<GetUserResponseBodyType>(_SetSidRequestTypeBody_QNAME, GetUserResponseBodyType.class, GetUserResponseType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserReplyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/", name = "GetUserResult", scope = GetUserResponseBodyType.class)
    public JAXBElement<GetUserReplyType> createGetUserResponseBodyTypeGetUserResult(GetUserReplyType value) {
        return new JAXBElement<GetUserReplyType>(_GetUserResponseBodyTypeGetUserResult_QNAME, GetUserReplyType.class, GetUserResponseBodyType.class, value);
    }

}
