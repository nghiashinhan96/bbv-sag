package com.sagag.services.ax.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sagag.services.article.api.attachedarticle.AttachedArticleHandler;
import com.sagag.services.ax.article.DefaultAttachedArticleHandlerImpl;
import com.sagag.services.ax.availability.externalvendor.NonAxExternalVendorAvailabilityCalculator;
import com.sagag.services.ax.availability.externalvendor.VenAvailabilityCalculator;
import com.sagag.services.ax.availability.stock.ArticlesStockAvailabilitiesFilterContext;
import com.sagag.services.ax.availability.stock.DefaultArticlesStockAvailabilitiesFilterContext;
import com.sagag.services.ax.paymenttypes.AxPaymentTypeMapper;
import com.sagag.services.ax.paymenttypes.DefaultAxPaymentTypeMapperImpl;

@Configuration
public class AxCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(AttachedArticleHandler.class)
  public AttachedArticleHandler defaultAttachedArticleHandler() {
    return new DefaultAttachedArticleHandlerImpl();
  }

  @Bean
  @ConditionalOnMissingBean(AxPaymentTypeMapper.class)
  public AxPaymentTypeMapper defaultAxPaymentTypeMapper() {
    return new DefaultAxPaymentTypeMapperImpl();
  }

  @Bean
  @ConditionalOnMissingBean(ArticlesStockAvailabilitiesFilterContext.class)
  public ArticlesStockAvailabilitiesFilterContext defArticlesStockAvailabilitiesFilterContext() {
    return new DefaultArticlesStockAvailabilitiesFilterContext();
  }

  @Bean
  @ConditionalOnMissingBean(VenAvailabilityCalculator.class)
  public VenAvailabilityCalculator defaultVenAvailabilityCalculator() {
    return new NonAxExternalVendorAvailabilityCalculator();
  }
}
