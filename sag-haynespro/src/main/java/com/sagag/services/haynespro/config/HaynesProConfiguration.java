package com.sagag.services.haynespro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

@Configuration
@HaynesProProfile
public class HaynesProConfiguration {

  @Autowired
  private HaynesProProperties hpProps;

  @Autowired
  private HttpComponentsMessageSender httpComponentsMessageSender;

  @Bean
  public Jaxb2Marshaller haynesProMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.sagag.services.haynespro.domain");
    return marshaller;
  }

  @Bean(name = "haynesProWebServiceTemplate")
  public WebServiceTemplate webServiceTemplate() throws SOAPException {
    final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMessageSender(httpComponentsMessageSender);
    webServiceTemplate.setDefaultUri(hpProps.getWsdl());
    webServiceTemplate.setMarshaller(haynesProMarshaller());
    webServiceTemplate.setUnmarshaller(haynesProMarshaller());
    webServiceTemplate.setMessageFactory(new SaajSoapMessageFactory(MessageFactory
        .newInstance(SOAPConstants.SOAP_1_2_PROTOCOL)));
    webServiceTemplate.afterPropertiesSet();
    return webServiceTemplate;
  }

}
