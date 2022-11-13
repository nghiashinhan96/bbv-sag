package com.sagag.services.dvse.config;

import java.util.Properties;

import javax.xml.soap.SOAPConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;

/**
 * <p>
 * The configuration to start web services.
 * </p>
 *
 *
 */
@Configuration
@EnableWs
public class DvseWebServiceConfiguration extends WsConfigurerAdapter {

  @Autowired
  private SoapProperties soapProps;

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
      ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setMessageFactoryBeanName("wsMessageFactory");
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, soapProps.getUrlMapping());
  }

  @Bean(name = "wsMessageFactory")
  public WebServiceMessageFactory wsMessageFactory() {
    final String[] protocols = new String[] { SOAPConstants.SOAP_1_1_PROTOCOL,
        SOAPConstants.SOAP_1_2_PROTOCOL };
    return new DualProtocolSoapMessageFactory(protocols);
  }

  @Bean
  public SoapFaultMappingExceptionResolver exceptionResolver() {
    SoapFaultMappingExceptionResolver exceptionResolver = new DvseFaultMappingExeptionResolver();
    SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
    faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
    exceptionResolver.setDefaultFault(faultDefinition);

    Properties errorMappings = new Properties();
    errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
    errorMappings.setProperty(RuntimeException.class.getName(), SoapFaultDefinition.SERVER.toString());
    exceptionResolver.setExceptionMappings(errorMappings);
    exceptionResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return exceptionResolver;
  }

}
