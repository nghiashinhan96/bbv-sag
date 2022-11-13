package com.sagag.eshop.service.config;

import com.sagag.eshop.service.affiliate.SupportedAffiliatePredicate;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.DeliveryTypeAdditionalService;
import com.sagag.eshop.service.api.MailService;
import com.sagag.eshop.service.api.PriceDisplaySettingService;
import com.sagag.eshop.service.api.PriceDisplayTypeService;
import com.sagag.eshop.service.api.impl.DefaultDeliveryTypeAdditionalServiceImpl;
import com.sagag.eshop.service.api.impl.payment.DefaultPaymentMethodOptionBuilder;
import com.sagag.eshop.service.api.impl.payment.PaymentMethodOptionBuilder;
import com.sagag.eshop.service.dto.EmailAttachment;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.domain.sag.erp.Address;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Configuration
@Slf4j
public class EshopServiceCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(MailService.class)
  public MailService defaultMailService() {
    return new MailService() {
      @Override
      public void sendEmail(String from, String to, String subject, String body, boolean isHtmlMode,
                            EmailAttachment... attachments) {
        log.debug("Disables email sender, so you can't receive any emails from Connect, "
                + "from = {}, to = {}, subject = {}", from, to, subject);
      }

      @Override
      public void sendEmailIncludeCc(String from, String to, String[] cc, String subject, String body,
                                     boolean isHtmlMode, EmailAttachment... attachments) {
        log.debug("Disables email sender, so you can't receive any emails from Connect, "
                + "from = {}, to = {}, Cc = {}, subject = {}", from, to, cc, subject);
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean(SupportedAffiliatePredicate.class)
  public SupportedAffiliatePredicate defaultSupportedAffiliatePredicate() {
    return Objects::nonNull;
  }

  @Bean
  @ConditionalOnMissingBean(AddressFilterService.class)
  public AddressFilterService defaultAddressFilterService() {
    return new AddressFilterService() {

      @Override
      public List<Address> getBillingAddresses(List<Address> addresses) {
        return ListUtils.emptyIfNull(addresses);
      }

      @Override
      public Optional<Address> filterAddress(List<Address> addresses,
          String currentAddressId, ErpAddressType type) {
        if (CollectionUtils.isEmpty(addresses) || type == null) {
          throw new IllegalStateException();
        }

        if (addresses.size() == 1) {
          return Optional.of(addresses.get(0));
        }

        // Always get lastest address information from external system(ERP)
        return ListUtils.emptyIfNull(addresses).stream()
            .filter(e -> type.name().equalsIgnoreCase(e.getAddressTypeCode()))
            .filter(address -> Objects.nonNull(address.getId()))
            .findFirst();
      }

      @Override
      public List<Address> getDeliveryAddresses(List<Address> addresses) {
        return ListUtils.emptyIfNull(addresses);
      }

    };
  }

  @Bean
  @ConditionalOnMissingBean(PriceDisplaySettingService.class)
  public PriceDisplaySettingService defaultPriceDisplaySettingService() {
    return customerSettings -> Collections.emptyList();
  }

  @Bean
  @ConditionalOnMissingBean(PaymentMethodOptionBuilder.class)
  public PaymentMethodOptionBuilder defaultPaymentMethodOptionBuilder() {
    return new DefaultPaymentMethodOptionBuilder();
  }

  @Bean
  @ConditionalOnMissingBean(PriceDisplayTypeService.class)
  public PriceDisplayTypeService defaultPriceDisplayTypeService() {
    return new PriceDisplayTypeService() {

      @Override
      public PriceDisplayTypeEnum getDefaultPriceDisplayType() {
        return PriceDisplayTypeEnum.UVPE_OEP_GROSS;
      }
    };
  }

  @Bean
  @ConditionalOnMissingBean(DeliveryTypeAdditionalService.class)
  public DeliveryTypeAdditionalService defaultDeliveryTypeAdditionalService() {
    return new DefaultDeliveryTypeAdditionalServiceImpl();
  }
}
