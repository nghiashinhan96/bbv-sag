package com.sagag.services.tools.config;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 */
@Configuration
@ConditionalOnProperty(name = "sagsys.enabled", havingValue = "true")
@SagsysProfile
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "sourceEntityManager",
  transactionManagerRef = "sourceTransactionManager",
  basePackages = "com.sagag.services.tools.repository.sagsys")
public class SagsysDataSourceConfiguration {

  private static final String ENTITY_PACKAGE = "com.sagag.services.tools.domain.sagsys";

  @Bean
  @ConfigurationProperties(prefix = "spring.sagsys.datasource")
  public DataSource sourceDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "sourceEntityManager")
  public LocalContainerEntityManagerFactoryBean sourceEntityManager() {
    LocalContainerEntityManagerFactoryBean em
    = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(sourceDataSource());
    em.setPackagesToScan(new String[] { ENTITY_PACKAGE });
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setJpaProperties(hibernateProperties());
    return em;
  }

  @Bean(name = "sourceTransactionManager")
  public PlatformTransactionManager sourceTransactionManager(
    @Qualifier("sourceEntityManager") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

  @Bean
  public Properties hibernateProperties() {
    return new Properties() {
      private static final long serialVersionUID = 4060173295275148344L;
      {
         setProperty("hibernate.hbm2ddl.auto", "none");
         setProperty("hibernate.show_sql", "true");
         setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect");
         setProperty("hibernate.format_sql", "false");
       }
    };
 }
}
