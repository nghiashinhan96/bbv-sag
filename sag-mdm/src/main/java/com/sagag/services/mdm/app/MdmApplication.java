package com.sagag.services.mdm.app;

import com.sagag.services.common.config.HttpProxyConfiguration;
import com.sagag.services.mdm.config.MdmConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Class for initialize MDM component.
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class },
 scanBasePackages = {"com.sagag"})
@Import(value = { MdmConfiguration.class, HttpProxyConfiguration.class })
public class MdmApplication {

  public static void main(String[] args) {
    SpringApplication.run(MdmApplication.class, args);
  }

}
