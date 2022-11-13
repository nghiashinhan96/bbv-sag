package com.sagag.services.hazelcast.app;

import com.sagag.eshop.repo.config.RepoConfiguration;
import com.sagag.services.elasticsearch.config.ElasticsearchConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Class to boot the SAG Caching application.
 */
@SpringBootApplication
// @formatter:off
@EnableAutoConfiguration(exclude = { 
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class})
@ComponentScan(basePackages = {"com.sagag.services", "com.sagag.eshop"})
@Import(value =
    {
      ElasticsearchConfiguration.class, 
      RepoConfiguration.class,
      HazelcastCoreConfig.class,
      HazelcastDatasourceConfig.class
    })
@Configuration
public class HazelcastApplication {

  public static void main(String[] args) {
    SpringApplication.run(HazelcastApplication.class, args);
  }
}
