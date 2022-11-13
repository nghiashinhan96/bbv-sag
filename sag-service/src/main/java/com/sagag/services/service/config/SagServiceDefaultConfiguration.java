package com.sagag.services.service.config;

import com.sagag.services.article.api.attachedarticle.AttachedArticleRequestBuilder;
import com.sagag.services.service.cart.attachedarticle.builder.DefaultAttachedArticleRequestBuilderImpl;
import com.sagag.services.service.category.converter.AbstractCategoryConverter;
import com.sagag.services.service.category.converter.impl.DefaultCategoryConverterV2;
import com.sagag.services.service.credit.CustomerCreditValidator;
import com.sagag.services.service.credit.DefaultCustomerCreditValidator;
import com.sagag.services.service.order.DefaultOrderHandlerImpl;
import com.sagag.services.service.order.handler.OrderHandler;
import com.sagag.services.service.invoice.InvoiceArchiveDocumentStream;
import com.sagag.services.service.invoice.impl.DefaultInvoiceArchiveDocumentStreamImpl;
import com.sagag.services.service.order.history.DefaultOrderHistoryHandler;
import com.sagag.services.service.order.history.OrderHistoryHandler;
import com.sagag.services.service.order.ordercondition.DefaultOrderConditionInitializer;
import com.sagag.services.service.order.ordercondition.OrderConditionInitializer;
import com.sagag.services.service.order.processor.AbstractOrderProcessor;
import com.sagag.services.service.order.processor.EmptyOrderProcessor;
import com.sagag.services.service.order.steps.orderrequest.AbstractOrderRequestStepHandlerV2Impl;
import com.sagag.services.service.order.steps.orderrequest.EmptyOrderRequestStepHandlerV2Impl;
import com.sagag.services.service.payment.UserPaymentSettingsFormBuilder;
import com.sagag.services.service.payment.impl.DefaultUserPaymentSettingsFormBuilderImpl;
import com.sagag.services.service.user.handler.DefaultEshopSelfManageDataHandler;
import com.sagag.services.service.user.handler.EshopSelfManageDataHandler;
import com.sagag.services.service.validator.CustomerValidator;
import com.sagag.services.service.validator.DefaultCustomerValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SagServiceDefaultConfiguration {

  @Bean
  @ConditionalOnMissingBean(AbstractOrderProcessor.class)
  public AbstractOrderProcessor defaultOrderProcessor() {
    return new EmptyOrderProcessor();
  }

  @Bean
  @ConditionalOnMissingBean(AttachedArticleRequestBuilder.class)
  public AttachedArticleRequestBuilder defaultArtRequestBuilder() {
    return new DefaultAttachedArticleRequestBuilderImpl();
  }

  @Bean
  @ConditionalOnMissingBean(EshopSelfManageDataHandler.class)
  public EshopSelfManageDataHandler defaultDefaultEshopSelfManageDataHandler() {
    return new DefaultEshopSelfManageDataHandler();
  }

  @Bean
  @ConditionalOnMissingBean(CustomerValidator.class)
  public CustomerValidator defaultCustomerValidator() {
    return new DefaultCustomerValidator();
  }

  @Bean
  @ConditionalOnMissingBean(AbstractCategoryConverter.class)
  public AbstractCategoryConverter defaultCategoryConverter() {
    return new DefaultCategoryConverterV2();
  }

  @Bean
  @ConditionalOnMissingBean(OrderConditionInitializer.class)
  public OrderConditionInitializer defaultOrderConditionInitializer() {
    return new DefaultOrderConditionInitializer();
  }

  @Bean
  @ConditionalOnMissingBean(OrderHistoryHandler.class)
  public OrderHistoryHandler defaultOrderHistoryHandler() {
    return new DefaultOrderHistoryHandler();
  }

  @Bean
  @ConditionalOnMissingBean(AbstractOrderRequestStepHandlerV2Impl.class)
  public AbstractOrderRequestStepHandlerV2Impl emptyOrderRequestStepHandler() {
    return new EmptyOrderRequestStepHandlerV2Impl();
  }

  @Bean
  @ConditionalOnMissingBean(OrderHandler.class)
  public OrderHandler defaultOrderHandler() {
    return new DefaultOrderHandlerImpl();
  }

  @Bean
  @ConditionalOnMissingBean(CustomerCreditValidator.class)
  public CustomerCreditValidator defaultCustomerCreditValidator() {
    return new DefaultCustomerCreditValidator();
  }

  @Bean
  @ConditionalOnMissingBean(UserPaymentSettingsFormBuilder.class)
  public UserPaymentSettingsFormBuilder defaultUserPaymentSettingsFormBuilder() {
    return new DefaultUserPaymentSettingsFormBuilderImpl();
  }

  @Bean
  @ConditionalOnMissingBean(InvoiceArchiveDocumentStream.class)
  public InvoiceArchiveDocumentStream defaultInvoiceArchiveDocStream() {
    return new DefaultInvoiceArchiveDocumentStreamImpl();
  }
}
