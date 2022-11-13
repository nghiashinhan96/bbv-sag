package com.sagag.services.gtmotive.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagag.services.gtmotive.config.GtmotiveProfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

@Configuration
@GtmotiveProfile
public class GtmotiveConfiguration {

  @Autowired
  private ClientHttpRequestFactory httpRequestFactory;

  @Autowired
  private HttpComponentsMessageSender httpComponentsMessageSender;

  @Bean
  public RestTemplate restTemplate() {
    final RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
    final List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
    final ObjectMapper objectMapper = new ObjectMapper();
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter jsonConverter =
            (MappingJackson2HttpMessageConverter) converter;
        jsonConverter.setObjectMapper(objectMapper);
      }
    }
    return restTemplate;
  }

  @Bean
  public Jaxb2Marshaller gtMotiveVehicleMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.sagag.services.gtmotive.wsdl.vehicle");
    return marshaller;
  }

  @Bean(name = "gtmotiveVehicleInfoWebServiceTemplate")
  public WebServiceTemplate gtmotiveVehicleInfoWebServiceTemplate() throws SOAPException {
    final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMessageSender(httpComponentsMessageSender);
    webServiceTemplate
        .setDefaultUri("http://wsgti.gtestimate.com/WSGtVehicleInformationService.svc?wsdl=wsdl0");
    webServiceTemplate.setMarshaller(gtMotiveVehicleMarshaller());
    webServiceTemplate.setUnmarshaller(gtMotiveVehicleMarshaller());
    webServiceTemplate.setMessageFactory(new SaajSoapMessageFactory(MessageFactory
        .newInstance(SOAPConstants.SOAP_1_1_PROTOCOL)));
    webServiceTemplate.afterPropertiesSet();
    return webServiceTemplate;
  }
}
