package com.sagag.services.rest.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger APIs configuration class.
 */
@Configuration
@ComponentScan("com.sagag.services.rest")
public class SwaggerConfiguration {

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("Central SAG RESTful API documentation")
        .description("Webshop Business Service APIs").build();
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .groupName("All APIs Document")
      .select().apis(RequestHandlerSelectors.basePackage("com.sagag.services.rest.controller"))
      .build();
  }
}
