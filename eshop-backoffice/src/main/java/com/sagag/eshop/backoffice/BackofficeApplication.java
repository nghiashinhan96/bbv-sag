package com.sagag.eshop.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.sagag.eshop", exclude = {
  DataSourceAutoConfiguration.class,
  HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class
})
public class BackofficeApplication extends SpringBootServletInitializer {

  public BackofficeApplication() {
    super();
    setRegisterErrorPageFilter(false);
  }

  public static void main(String[] args) {
    SpringApplication.run(BackofficeApplication.class, args);
  }
}
