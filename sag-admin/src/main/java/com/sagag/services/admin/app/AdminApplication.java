package com.sagag.services.admin.app;

import com.sagag.eshop.repo.config.RepoConfiguration;
import com.sagag.services.elasticsearch.config.ElasticsearchConfiguration;
import com.sagag.services.mdm.config.MdmConfiguration;
import com.sagag.services.service.config.SagServiceCoreConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@ComponentScan(value = "com.sagag")
@EnableAutoConfiguration
// @formatter:off
@Import(value =
    {
      AdminCoreConfiguration.class,
      AdminSecurityConfiguration.class,
      SwaggerConfiguration.class,
      RepoConfiguration.class,
      ElasticsearchConfiguration.class,
      MdmConfiguration.class,
      SagServiceCoreConfiguration.class
    }) // @formatter:on
@EnableSwagger2
public class AdminApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(AdminApplication.class);
  }

}
