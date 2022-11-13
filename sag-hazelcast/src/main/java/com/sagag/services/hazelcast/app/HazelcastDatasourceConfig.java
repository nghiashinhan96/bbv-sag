package com.sagag.services.hazelcast.app;

import com.sagag.services.common.config.AppProperties;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
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
 * Hazelcast datasource configuration class.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "hzEntityManagerFactory",
    transactionManagerRef = "hzTransactionManager",
    basePackages = { "com.sagag.services.hazelcast.repo.api" })
public class HazelcastDatasourceConfig {

  @Autowired
  private AppProperties appProps;

  @Bean(name = "hzHikariDatasource")
  public DataSource hzHikariDatasource(HazelcastDatasourceProperties hzDatasourceProperties) {
    return new HikariDataSource(hzDatasourceProperties.getHikari());
  }

  @Bean(name = "hzEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean hzEntityManagerFactory(
      @Qualifier("hzHikariDatasource") DataSource dataSource, JpaProperties jpaProperties) {
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setShowSql(appProps.getDb().isShowSql());
    final LocalContainerEntityManagerFactoryBean factory =
        new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setDataSource(dataSource);
    factory.setPersistenceUnitName("hz-pu");
    factory.setPackagesToScan("com.sagag.eshop.repo.hz.entity");
    factory.setJpaPropertyMap(jpaProperties.getProperties());
    return factory;
  }

  @Bean(name = "hzTransactionManager")
  public PlatformTransactionManager hzTransactionManager(
      @Qualifier("hzEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    final JpaTransactionManager jtm = new JpaTransactionManager(entityManagerFactory);
    jtm.setDefaultTimeout(120); // 2 minutes.
    return new JpaTransactionManager(entityManagerFactory);
  }

}
