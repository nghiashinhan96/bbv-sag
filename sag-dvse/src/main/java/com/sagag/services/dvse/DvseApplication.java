package com.sagag.services.dvse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.sagag.eshop.repo.config.RepoConfiguration;
import com.sagag.services.common.executor.ExecutorConfiguration;
import com.sagag.services.elasticsearch.config.ElasticsearchConfiguration;
import com.sagag.services.hazelcast.app.HazelcastCoreConfig;

@SpringBootApplication
@ComponentScan(basePackages = "com.sagag")
@Import(
    value = {
        HazelcastCoreConfig.class, 
        RepoConfiguration.class, 
        ElasticsearchConfiguration.class,
        ExecutorConfiguration.class
    })
public class DvseApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(DvseApplication.class, args);
  }

}
