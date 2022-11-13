package com.sagag.eshop.repo.config;

import com.sagag.services.common.config.AppProperties;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Default Spring repository configuration class.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager", basePackages = { "com.sagag.eshop.repo.api" })
public class RepoConfiguration {

  @Autowired
  private AppProperties appProps;

  @Primary
  @Bean(name = "datasource")
  public DataSource datasource(final GlobalDatasourceProperties globalDatasourceProperties) {
    return new HikariDataSource(globalDatasourceProperties.getHikari());
  }

  @Primary
  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      final EntityManagerFactoryBuilder builder,
      final @Qualifier("datasource") DataSource dataSource, final JpaProperties jpaProperties) {
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(appProps.getDb().isShowSql());
    final LocalContainerEntityManagerFactoryBean factory =
        new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setDataSource(dataSource);
    factory.setPersistenceUnitName("default-pu");
    factory.setPackagesToScan("com.sagag.eshop.repo.entity");
    factory.setJpaPropertyMap(jpaProperties.getProperties());
    return factory;
  }

  @Primary
  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager(
      @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
    final JpaTransactionManager jtm = new JpaTransactionManager(entityManagerFactory);
    jtm.setDefaultTimeout(120); // 2 minutes.
    return jtm;
  }

  @Bean
  @Primary
  public MBeanExporter exporter() {
    final MBeanExporter exporter = new AnnotationMBeanExporter();
    exporter.setAutodetect(true);
    exporter.setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
    exporter.setExcludedBeans("dataSource");
    return exporter;
  }
}
