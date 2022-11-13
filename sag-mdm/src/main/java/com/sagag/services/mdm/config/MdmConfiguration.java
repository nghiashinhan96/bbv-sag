package com.sagag.services.mdm.config;

import com.sagag.services.common.config.HttpProxyConfiguration;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import java.util.concurrent.TimeUnit;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;

/**
 * Class for import core MDM configuration.
 *
 */
@Configuration
public class MdmConfiguration {

  @Autowired
  private MdmAccountConfigData mdmAccConfigData;

  @Autowired
  private HttpProxyConfiguration httpProxyConfig;

  @Bean
  public Jaxb2Marshaller mdmClientMarshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.sagag.services.mdm.wsdl");
    return marshaller;
  }

  @Bean(name = "mdmWebServiceTemplate")
  public WebServiceTemplate webServiceTemplate() throws SOAPException {
    final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMessageSender(mdmHttpComponentsMessageSender());
    webServiceTemplate.setDefaultUri(mdmAccConfigData.getUri());
    webServiceTemplate.setMarshaller(mdmClientMarshaller());
    webServiceTemplate.setUnmarshaller(mdmClientMarshaller());

    // Create SOAP 1.1 protocol for MDM web services to avoid
    // override common configuration of whole application.
    webServiceTemplate.setMessageFactory(new SaajSoapMessageFactory(MessageFactory
        .newInstance(SOAPConstants.SOAP_1_1_PROTOCOL)));
    webServiceTemplate.afterPropertiesSet();
    return webServiceTemplate;
  }

  @Bean(name = "mdmHttpComponentsMessageSender")
  public HttpComponentsMessageSender mdmHttpComponentsMessageSender() {
    return new HttpComponentsMessageSender(mdmHttpClient());
  }

  @Bean(name = "mdmHttpClient")
  public HttpClient mdmHttpClient() {
    final HttpClientBuilder builder = HttpClientBuilder.create();
    builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
    builder.setMaxConnPerRoute(httpProxyConfig.getMaxConnectionPerRoute());
    builder.setMaxConnTotal(httpProxyConfig.getMaxConnectionTotal());
    builder.setConnectionTimeToLive(httpProxyConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS);
    return builder.build();
  }
}
