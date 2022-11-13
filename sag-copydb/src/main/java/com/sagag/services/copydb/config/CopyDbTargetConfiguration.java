package com.sagag.services.copydb.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "targetEntityManager", transactionManagerRef = "targetTransactionManager",
    basePackages = "com.sagag.services.copydb.repo.dest")
@CopyDbProfile
public class CopyDbTargetConfiguration {

  @Bean(name = "targetDataSource")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.target")
  public DataSource targetDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "targetEntityManager")
  @Primary
  public LocalContainerEntityManagerFactoryBean targetEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(targetDataSource());
    em.setPackagesToScan("com.sagag.services.copydb.domain.dest");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    em.setPersistenceUnitName("pu-target");
    return em;
  }

  @Bean(name = "targetTransactionManager")
  @Primary
  public PlatformTransactionManager targetTransactionManager(@Qualifier("targetEntityManager") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

}
