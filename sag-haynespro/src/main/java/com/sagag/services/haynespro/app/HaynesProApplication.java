package com.sagag.services.haynespro.app;

import static org.springframework.boot.SpringApplication.run;

import com.sagag.services.common.config.HttpProxyConfiguration;
import com.sagag.services.haynespro.config.HaynesProConfiguration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
@ComponentScan(basePackages = "com.sagag")
@Import(value = { HaynesProConfiguration.class, HttpProxyConfiguration.class })
public class HaynesProApplication {

  public static void main(String[] args) {
    run(HaynesProApplication.class, args);
  }

}
