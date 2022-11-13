package com.sagag.services.ivds.app;

import com.sagag.eshop.repo.config.RepoConfiguration;
import com.sagag.services.elasticsearch.config.ElasticsearchConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
    scanBasePackages = "com.sagag.services",
    exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class})
@Import(value =
    {
      ElasticsearchConfiguration.class,
      RepoConfiguration.class
    })
public class IvdsApplication {

  public static void main(String[] args) {
    SpringApplication.run(IvdsApplication.class, args);
  }
}
