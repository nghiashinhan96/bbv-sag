package com.sagag.services.admin.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan("com.sagag.services.admin")
public class SwaggerConfiguration {

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("Central SAG RESTful Admin Mangement API documentation")
        .description("Admin Webshop Business Service APIs").build();
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .groupName("All APIs Document")
      .select().apis(RequestHandlerSelectors.basePackage("com.sagag.services.admin.controller"))
      .build();
  }

}
