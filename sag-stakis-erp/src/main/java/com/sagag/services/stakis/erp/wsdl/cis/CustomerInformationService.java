
package com.sagag.services.stakis.erp.wsdl.cis;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CustomerInformationService", targetNamespace = "http://tempuri.org/", wsdlLocation = "file:/C:/dev/soap/SOAP_UI_STAKIS/cis.wsdl")
public class CustomerInformationService
    extends Service
{

    private final static URL CUSTOMERINFORMATIONSERVICE_WSDL_LOCATION;
    private final static WebServiceException CUSTOMERINFORMATIONSERVICE_EXCEPTION;
    private final static QName CUSTOMERINFORMATIONSERVICE_QNAME = new QName("http://tempuri.org/", "CustomerInformationService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/C:/dev/soap/SOAP_UI_STAKIS/cis.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CUSTOMERINFORMATIONSERVICE_WSDL_LOCATION = url;
        CUSTOMERINFORMATIONSERVICE_EXCEPTION = e;
    }

    public CustomerInformationService() {
        super(__getWsdlLocation(), CUSTOMERINFORMATIONSERVICE_QNAME);
    }

    public CustomerInformationService(WebServiceFeature... features) {
        super(__getWsdlLocation(), CUSTOMERINFORMATIONSERVICE_QNAME, features);
    }

    public CustomerInformationService(URL wsdlLocation) {
        super(wsdlLocation, CUSTOMERINFORMATIONSERVICE_QNAME);
    }

    public CustomerInformationService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CUSTOMERINFORMATIONSERVICE_QNAME, features);
    }

    public CustomerInformationService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CustomerInformationService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TMCustomerIdentificationProSvcSoap
     */
    @WebEndpoint(name = "BasicHttpBinding_dvse")
    public TMCustomerIdentificationProSvcSoap getBasicHttpBindingDvse() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_dvse"), TMCustomerIdentificationProSvcSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TMCustomerIdentificationProSvcSoap
     */
    @WebEndpoint(name = "BasicHttpBinding_dvse")
    public TMCustomerIdentificationProSvcSoap getBasicHttpBindingDvse(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_dvse"), TMCustomerIdentificationProSvcSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CUSTOMERINFORMATIONSERVICE_EXCEPTION!= null) {
            throw CUSTOMERINFORMATIONSERVICE_EXCEPTION;
        }
        return CUSTOMERINFORMATIONSERVICE_WSDL_LOCATION;
    }

}
