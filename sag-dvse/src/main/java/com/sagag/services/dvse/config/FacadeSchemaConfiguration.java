package com.sagag.services.dvse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;

@Configuration
public class FacadeSchemaConfiguration {

  @Autowired
  private SoapProperties soapProps;

  @Bean(name = "ShopFacade")
  public SimpleWsdl11Definition simpleWsdl11Definition() {
    return new SimpleWsdl11Definition(new ClassPathResource(soapProps.getFacade().getWsdlName()));
  }

  /**
   * to adapt the given current wsdl structure with nested imports
   *
   * @return
   */
  @Bean(name = "ShopFacade1")
  public SimpleWsdl11Definition simpleWsdl11Definition2() {
    return new SimpleWsdl11Definition(new ClassPathResource(soapProps.getFacade1().getWsdlName()));
  }

  @Bean(name = "xsd1")
  public SimpleXsdSchema simpleXsdSchema1() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd1.xsd"));
  }

  @Bean(name = "xsd2")
  public SimpleXsdSchema simpleXsdSchema2() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd2.xsd"));
  }

  @Bean(name = "xsd3")
  public SimpleXsdSchema simpleXsdSchema3() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd3.xsd"));
  }

  @Bean(name = "xsd4")
  public SimpleXsdSchema simpleXsdSchema4() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd4.xsd"));
  }

  @Bean(name = "xsd5")
  public SimpleXsdSchema simpleXsdSchema5() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd5.xsd"));
  }

  @Bean(name = "xsd6")
  public SimpleXsdSchema simpleXsdSchema6() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd6.xsd"));
  }

  @Bean(name = "xsd7")
  public SimpleXsdSchema simpleXsdSchema7() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd7.xsd"));
  }

  @Bean(name = "xsd8")
  public SimpleXsdSchema simpleXsdSchema8() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd8.xsd"));
  }

  @Bean(name = "xsd9")
  public SimpleXsdSchema simpleXsdSchema9() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd9.xsd"));
  }

  @Bean(name = "xsd10")
  public SimpleXsdSchema simpleXsdSchema10() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd10.xsd"));
  }

  @Bean(name = "xsd11")
  public SimpleXsdSchema simpleXsdSchema11() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd11.xsd"));
  }

  @Bean(name = "xsd12")
  public SimpleXsdSchema simpleXsdSchema12() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd12.xsd"));
  }

  @Bean(name = "xsd13")
  public SimpleXsdSchema simpleXsdSchema13() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd13.xsd"));
  }

  @Bean(name = "xsd14")
  public SimpleXsdSchema simpleXsdSchema14() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd14.xsd"));
  }

  @Bean(name = "xsd15")
  public SimpleXsdSchema simpleXsdSchema15() {
      return new SimpleXsdSchema(new ClassPathResource("xsd/xsd15.xsd"));
  }

}
