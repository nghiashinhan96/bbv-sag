package com.sagag.services.tools;

import com.sagag.services.tools.config.MdmConfiguration;
import com.sagag.services.tools.config.MdmProxyConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Spring boot application initialization.
 */
@SpringBootApplication(scanBasePackages = { "com.sagag.services.tools" },
  exclude = { HibernateJpaAutoConfiguration.class })
@Import(value = { MdmConfiguration.class, MdmProxyConfiguration.class })
public class ToolsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ToolsApplication.class, args);
  }
}
