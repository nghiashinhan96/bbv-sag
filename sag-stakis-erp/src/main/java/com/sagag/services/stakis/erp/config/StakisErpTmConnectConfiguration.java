package com.sagag.services.stakis.erp.config;

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

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ObjectFactory;

@Configuration
@CzProfile
public class StakisErpTmConnectConfiguration {

  @Autowired
  private StakisErpProperties stakisErpProps;

  @Autowired
  private HttpComponentsMessageSender httpComponentsMessageSender;

  @Bean
  public Jaxb2Marshaller stakisErpTmConnectMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath(stakisErpProps.getTmConnectContextPath());
    return marshaller;
  }

  @Bean(name = "stakisErpTmConnectWebServiceTemplate")
  public WebServiceTemplate stakisErpTmConnectWebServiceTemplate() throws SOAPException {
    final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMessageSender(httpComponentsMessageSender);
    webServiceTemplate.setMarshaller(stakisErpTmConnectMarshaller());
    webServiceTemplate.setUnmarshaller(stakisErpTmConnectMarshaller());
    webServiceTemplate.setDefaultUri(stakisErpProps.getTmConnectSvc());
    webServiceTemplate.setMessageFactory(
        new SaajSoapMessageFactory(MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL)));
    webServiceTemplate.afterPropertiesSet();
    return webServiceTemplate;
  }

  @Bean("tmConnectObjectFactory")
  public ObjectFactory tmConnectObjectFactory() {
    return new ObjectFactory();
  }

}
