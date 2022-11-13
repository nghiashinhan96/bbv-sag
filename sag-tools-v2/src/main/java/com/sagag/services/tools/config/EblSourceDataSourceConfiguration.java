package com.sagag.services.tools.config;

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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 *
 */
@Configuration
@ConditionalOnProperty(name = "ebl.enabled", havingValue = "true")
@OracleProfile
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "sourceEntityManager",
  transactionManagerRef = "sourceTransactionManager",
  basePackages = "com.sagag.services.tools.repository.source")
public class EblSourceDataSourceConfiguration {

  private static final String ENTITY_PACKAGE = "com.sagag.services.tools.domain.source";

  @Bean
  @ConfigurationProperties(prefix = "spring.source.datasource")
  public DataSource sourceDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "sourceEntityManager")
  @ConfigurationProperties(prefix = "spring.source.datasource")
  public LocalContainerEntityManagerFactoryBean sourceEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(sourceDataSource());
    em.setPackagesToScan(new String[] { ENTITY_PACKAGE });
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(true);
    em.setJpaVendorAdapter(vendorAdapter);
    return em;
  }

  @Bean(name = "sourceTransactionManager")
  public PlatformTransactionManager sourceTransactionManager(
    @Qualifier("sourceEntityManager") EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }
}
