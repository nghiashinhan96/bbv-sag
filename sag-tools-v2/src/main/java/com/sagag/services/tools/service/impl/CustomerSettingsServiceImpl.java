package com.sagag.services.tools.service.impl;

import com.sagag.services.tools.batch.migration.customer_settings.PriceDisplayTypeEnum;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.external.Customer;
import com.sagag.services.tools.domain.target.CustomerSettings;
import com.sagag.services.tools.domain.target.DeliveryType;
import com.sagag.services.tools.domain.target.InvoiceType;
import com.sagag.services.tools.domain.target.PaymentMethod;
import com.sagag.services.tools.domain.target.PriceDisplayType;
import com.sagag.services.tools.repository.target.CollectiveDeliveryRepository;
import com.sagag.services.tools.repository.target.DeliveryTypesRepository;
import com.sagag.services.tools.repository.target.InvoiceTypeRepository;
import com.sagag.services.tools.repository.target.PaymentMethodRepository;
import com.sagag.services.tools.repository.target.PriceDisplayTypeRepository;
import com.sagag.services.tools.service.CustomerSettingsService;
import com.sagag.services.tools.support.ErpAddressType;
import com.sagag.services.tools.support.ErpInvoiceTypeCode;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@OracleProfile
public class CustomerSettingsServiceImpl implements CustomerSettingsService {

  @Autowired
  private PaymentMethodRepository paymentMethodRepo;

  @Autowired
  private DeliveryTypesRepository deliveryTypesRepo;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  @Autowired
  private CollectiveDeliveryRepository collectiveDeliveryRepo;
  
  private static final PriceDisplayTypeEnum defaultPriceType = PriceDisplayTypeEnum.UVPE_OEP_GROSS;
  
  @Autowired
  private PriceDisplayTypeRepository priceDisplayTypeRepo;

  @Override
  public CustomerSettings createCustomerSettings(Customer customer, List<Address> addresses) {
    Assert.notNull(customer, "The given customer must not be null");
    Assert.notEmpty(addresses, "The given addresses must not be empty");
    final String sendMethodCode = customer.getSendMethodCode();
    DeliveryType deliveryTypeDefault = deliveryTypesRepo.findOneById(1).get();
    final Optional<DeliveryType> deliveryType = deliveryTypesRepo.findOneByDescCode(sendMethodCode);
    if (deliveryType.isPresent()) {
      deliveryTypeDefault = deliveryType.get();
    }
    final CustomerSettings customerSettings = new CustomerSettings();
    customerSettings.setDeliveryType(deliveryTypeDefault);
    customerSettings.setAllocationId(1);
    customerSettings.setCollectiveDelivery(collectiveDeliveryRepo.findOneById(1).get());
    PriceDisplayType priceDisplayType =
        priceDisplayTypeRepo.findByType(defaultPriceType.name()).orElseThrow(() -> new IllegalArgumentException("Invalid price display setting"));
    customerSettings.setPriceDisplayId(priceDisplayType.getId());
    ErpInvoiceTypeCode invoiceTypeCode;
    if (!ErpInvoiceTypeCode.contains(customer.getInvoiceTypeCode())) {
      log.debug("The invoice type code " + customer.getInvoiceTypeCode() + " was not defined!"
          + " Set it as " + ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE.name());
      invoiceTypeCode = ErpInvoiceTypeCode.TWO_WEEKLY_INVOICE;
    } else {
      invoiceTypeCode = ErpInvoiceTypeCode.valueOf(customer.getInvoiceTypeCode());
    }

    final Optional<InvoiceType> optInvoiceType =
        invoiceTypeRepo.findOneByInvoiceTypeCode(invoiceTypeCode.name());
    optInvoiceType.ifPresent(customerSettings::setInvoiceType);

    customerSettings.setNetPriceView(false);
    customerSettings.setNetPriceConfirm(false);
    customerSettings.setShowDiscount(false);
    customerSettings.setAllowNetPriceChanged(true);
    final Optional<PaymentMethod> paymentMethod =
        paymentMethodRepo.findOneByDescCode(customer.getCashOrCreditTypeCode());
    paymentMethod.ifPresent(customerSettings::setPaymentMethod);
    customerSettings.setSessionTimeoutSeconds(3600);

    String billingAddressDefaultId = null;
    String deliveryAddressDefaultId = null;
    List<Address> billingAddresses = addresses.stream()
        .filter(d -> d.getAddressTypeCode().equalsIgnoreCase(ErpAddressType.INVOICE.name()))
        .collect(Collectors.toList());
    List<Address> defaultAddresses = addresses.stream()
        .filter(d -> d.getAddressTypeCode().equalsIgnoreCase(ErpAddressType.DEFAULT.name()))
        .collect(Collectors.toList());
    List<Address> deliveryAddresses = addresses.stream()
        .filter(d -> d.getAddressTypeCode().equalsIgnoreCase(ErpAddressType.DELIVERY.name()))
        .collect(Collectors.toList());
    Optional<Address> defaultAddressOpt = defaultAddresses.stream().findFirst();
    Optional<Address> billingAddressOpt = billingAddresses.stream().findFirst();
    Optional<Address> deliveryAddressOpt = deliveryAddresses.stream().findFirst();
    if (billingAddressOpt.isPresent()) {
      billingAddressDefaultId = billingAddressOpt.get().getId();
    }
    if (deliveryAddressOpt.isPresent()) {
      deliveryAddressDefaultId = deliveryAddressOpt.get().getId();
    }
    if (StringUtils.isBlank(billingAddressDefaultId) && defaultAddressOpt.isPresent()) {
      billingAddressDefaultId = defaultAddressOpt.get().getId();
    }
    if (StringUtils.isBlank(deliveryAddressDefaultId) && defaultAddressOpt.isPresent()) {
      deliveryAddressDefaultId = defaultAddressOpt.get().getId();
    }
    customerSettings.setBillingAddressId(Long.valueOf(billingAddressDefaultId));
    customerSettings.setDeliveryAddressId(Long.valueOf(deliveryAddressDefaultId));

    return customerSettings;

  }
}
