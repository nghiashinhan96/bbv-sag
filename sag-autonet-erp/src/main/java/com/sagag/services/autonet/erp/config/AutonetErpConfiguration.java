package com.sagag.services.autonet.erp.config;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import com.sagag.services.autonet.erp.wsdl.object_factory.ObjectFactory;
import com.sagag.services.common.profiles.AutonetProfile;

@Configuration
@AutonetProfile
public class AutonetErpConfiguration {

  @Autowired
  private AutonetErpProperties autonetErpProps;

  @Autowired
  private HttpComponentsMessageSender httpComponentsMessageSender;

  @Bean
  public Jaxb2Marshaller autonetErpMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath(autonetErpProps.getContextPath());
    return marshaller;
  }

  @Bean(name = "autonetErpWebServiceTemplate")
  public WebServiceTemplate autonetErpWebServiceTemplate() throws SOAPException {
    final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMessageSender(httpComponentsMessageSender);
    webServiceTemplate.setDefaultUri(autonetErpProps.getWsdl());
    webServiceTemplate.setMarshaller(autonetErpMarshaller());
    webServiceTemplate.setUnmarshaller(autonetErpMarshaller());
    webServiceTemplate.setMessageFactory(
        new SaajSoapMessageFactory(MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL)));
    webServiceTemplate.afterPropertiesSet();
    return webServiceTemplate;
  }

  @Bean(name = "autonetErpObjectFactory")
  public ObjectFactory autonetErpObjectFactory() {
    return new ObjectFactory();
  }

  @Bean(name = "autonetErpTmConnectObjectFactory")
  public com.sagag.services.autonet.erp.wsdl.tmconnect.ObjectFactory autonetErpTmConnectObjectFactory() {
    return new com.sagag.services.autonet.erp.wsdl.tmconnect.ObjectFactory();
  }

  @Bean(name = "autonetErpUserObjectFactory")
  public com.sagag.services.autonet.erp.wsdl.user.ObjectFactory autonetErpUserObjectFactory() {
    return new com.sagag.services.autonet.erp.wsdl.user.ObjectFactory();
  }
}
