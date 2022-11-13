package com.sagag.services.thule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(scanBasePackages = "com.sagag.services",
  exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
      DataSourceTransactionManagerAutoConfiguration.class })
public class ThuleApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThuleApplication.class, args);
  }

}
