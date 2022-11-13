package com.sagag.services.service;

import com.sagag.eshop.repo.config.RepoConfiguration;
import com.sagag.services.common.executor.ExecutorConfiguration;
import com.sagag.services.elasticsearch.config.ElasticsearchConfiguration;
import com.sagag.services.gtmotive.app.GtmotiveConfiguration;
import com.sagag.services.haynespro.config.HaynesProConfiguration;
import com.sagag.services.hazelcast.app.HazelcastCoreConfig;
import com.sagag.services.incentive.config.IncentiveConfiguration;
import com.sagag.services.mdm.config.MdmConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.sagag.services")
@Import(value = {
  RepoConfiguration.class,
  ElasticsearchConfiguration.class,
  HazelcastCoreConfig.class,
  GtmotiveConfiguration.class,
  HaynesProConfiguration.class,
  MdmConfiguration.class,
  ExecutorConfiguration.class,
  IncentiveConfiguration.class
})
public class SagServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SagServiceApplication.class, args);
  }

}
