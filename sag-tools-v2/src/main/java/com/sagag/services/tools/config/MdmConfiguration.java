package com.sagag.services.tools.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

/**
 * Class for import core MDM configuration.
 *
 */
@Configuration
public class MdmConfiguration {

  @Value("${external.webservice.dvse.uri}")
  private String mdnUri;

  @Autowired
  private HttpComponentsMessageSender httpComponentsMessageSender;

  @Bean
  public Jaxb2Marshaller mdmClientMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.sagag.services.tools.mdm.wsdl");
    return marshaller;
  }

  @Bean(name = "mdmWebServiceTemplate")
  public WebServiceTemplate webServiceTemplate() throws SOAPException {
    final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMessageSender(httpComponentsMessageSender);
    webServiceTemplate.setDefaultUri(mdnUri);
    webServiceTemplate.setMarshaller(mdmClientMarshaller());
    webServiceTemplate.setUnmarshaller(mdmClientMarshaller());

    // Create SOAP 1.1 protocol for MDM web services to avoid override common configuration of whole application.
    webServiceTemplate.setMessageFactory(new SaajSoapMessageFactory(
        MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL)));
    webServiceTemplate.afterPropertiesSet();
    return webServiceTemplate;
  }

}
