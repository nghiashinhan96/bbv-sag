package com.sagag.services.copydb.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
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
@CopyDbProfile
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "sourceEntityManager", transactionManagerRef = "sourceTransactionManager",
    basePackages = "com.sagag.services.copydb.repo.src")
public class CopyDbSourceConfiguration {

  @Bean(name = "sourceDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.source")
  public DataSource sourceDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "sourceEntityManager")
  public LocalContainerEntityManagerFactoryBean sourceEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(sourceDataSource());
    em.setPackagesToScan("com.sagag.services.copydb.domain.src");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setPersistenceUnitName("pu-source");
    return em;
  }

  @Bean(name = "sourceTransactionManager")
  public PlatformTransactionManager sourceTransactionManager(@Qualifier("sourceEntityManager") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
  
}
