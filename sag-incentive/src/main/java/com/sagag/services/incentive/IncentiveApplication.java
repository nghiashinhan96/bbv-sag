package com.sagag.services.incentive;

import com.sagag.services.common.config.HttpProxyConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Incentive Application class.
 */
@SpringBootApplication
@EnableAutoConfiguration(
    exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class
    })
@ComponentScan(value = "com.sagag")
@Import(value = { HttpProxyConfiguration.class })
public class IncentiveApplication {

  public static void main(String[] args) {
    SpringApplication.run(IncentiveApplication.class, args);
  }
}
