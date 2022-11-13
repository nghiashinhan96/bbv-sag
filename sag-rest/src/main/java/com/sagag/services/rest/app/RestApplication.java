package com.sagag.services.rest.app;

import com.sagag.eshop.repo.config.RepoConfiguration;
import com.sagag.services.common.executor.ExecutorConfiguration;
import com.sagag.services.elasticsearch.config.ElasticsearchConfiguration;
import com.sagag.services.hazelcast.app.HazelcastCoreConfig;
import com.sagag.services.hazelcast.app.HazelcastDatasourceConfig;
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

import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Restful Application configuration class.
 */
@SpringBootApplication
@Configuration
@ComponentScan(value = "com.sagag")
@EnableAutoConfiguration
// @formatter:off
@Import(value =
    {
      RestCoreConfiguration.class,
      RestSecurityConfiguration.class,
      SwaggerConfiguration.class,
      RepoConfiguration.class,
      ElasticsearchConfiguration.class,
      HazelcastCoreConfig.class,
      HazelcastDatasourceConfig.class,
      MdmConfiguration.class,
      ExecutorConfiguration.class,
      AxScheduleConfiguration.class,
      SagServiceCoreConfiguration.class,
    }) // @formatter:on
@EnableSwagger2
@EnableAsync
public class RestApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(RestApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(RestApplication.class);
  }

}
