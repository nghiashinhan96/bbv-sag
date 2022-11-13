package com.sagag.services.oauth2.settings;

import com.sagag.eshop.repo.api.CustomerSettingsRepository;
import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.eshop.repo.entity.*;
import com.sagag.eshop.service.api.AddressFilterService;
import com.sagag.eshop.service.api.CustomerSettingsService;
import com.sagag.eshop.service.api.InvoiceTypeService;
import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.ax.exception.AxCustomerException;
import com.sagag.services.common.enums.ErpAddressType;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.CustomerCacheService;
import com.sagag.services.oauth2.exception.BlockedCustomerException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class CustomerSettingsProcessor
  implements SettingsProcessor<EshopUser, UpdatedCustomerSettingsDto> {

  private final CustomerSettingsRepository customerSettingsRepo;

  private final CustomerSettingsService customerSettingsService;

  private final CustomerExternalService customerExtService;

  private final PaymentMethodRepository paymentRepo;

  private final DeliveryTypesRepository deliveryTypesRepo;

  private final CustomerCacheService customerCacheService;

  private final InvoiceTypeService invoiceTypeService;

  private final AddressFilterService addressFilterService;

  @Autowired
  public CustomerSettingsProcessor(CustomerSettingsRepository customerSettingsRepo,
    CustomerSettingsService customerSettingsService,
    CustomerExternalService customerExtService,
    CustomerCacheService customerCacheService,
    PaymentMethodRepository paymentRepo,
    DeliveryTypesRepository deliveryTypesRepo,
    InvoiceTypeService invoiceTypeService,
    AddressFilterService addressFilterService) {
    this.customerSettingsRepo = customerSettingsRepo;
    this.customerSettingsService = customerSettingsService;
    this.customerExtService = customerExtService;
    this.customerCacheService = customerCacheService;
    this.paymentRepo = paymentRepo;
    this.deliveryTypesRepo = deliveryTypesRepo;
    this.invoiceTypeService = invoiceTypeService;
    this.addressFilterService = addressFilterService;
  }


  @Override
  public Optional<UpdatedCustomerSettingsDto> process(EshopUser foundUser) {
    final Optional<Organisation> org = foundUser.firstOrganisation();
    if (!org.isPresent()) {
      return Optional.empty();
    }
    final String companyName = foundUser.companyName(foundUser.getAffiliate());
    final Organisation userOrg = org.get();
    final Customer erpCustomer = getCachedCustomer(companyName, userOrg.getCustomerNrByOrg());

    UpdatedCustomerSettingsDto updatedCustSettingsDto = new UpdatedCustomerSettingsDto();
    updatedCustSettingsDto.setCustomerSettings(updateCustomerSettings(erpCustomer, userOrg));
    Optional.of(erpCustomer).ifPresent(updatedCustSettingsDto::setErpCustomer);

    return Optional.of(updatedCustSettingsDto);
  }

  private Customer getCachedCustomer(String companyName, String customerNr) {
    try {
      return customerCacheService.getCachedCustomer(customerNr, companyName)
      .orElseThrow(IllegalStateException::new);
    } catch (AxCustomerException e) {
      log.warn("Customer number = {} is not active", customerNr);
      throw new BlockedCustomerException();
    }
  }

  /**
   * Updates customer settings
   *
   * @param customer
   * @param organisation
   * @return CustomerSettings
   */
  private CustomerSettings updateCustomerSettings(@NonNull Customer customer,
      @NonNull Organisation organisation) {
    final CustomerSettings cusSettings =
        customerSettingsService.findSettingsByOrgId(organisation.getId());
    Assert.notNull(cusSettings, "Customer Settings");

    if (addressFilterService.updateAddressSettings()) {
      AtomicBoolean isAddressUpdated = new AtomicBoolean(false);
      // Always update new customer setting delivery/billing address id
      List<Address> addresses = customerExtService
          .searchCustomerAddresses(customer.getAffiliateName(), customer.getNr());
      addressFilterService.filterAddress(addresses, StringUtils.EMPTY, ErpAddressType.INVOICE)
          .ifPresent(add -> {
            if (!add.getId().equalsIgnoreCase(String.valueOf(cusSettings.getBillingAddressId()))) {
              cusSettings.setBillingAddressId(Long.valueOf(add.getId()));
              isAddressUpdated.set(true);
            }
          });
      addressFilterService.filterAddress(addresses, StringUtils.EMPTY, ErpAddressType.DELIVERY)
          .ifPresent(add -> {
            if (!add.getId().equalsIgnoreCase(String.valueOf(cusSettings.getDeliveryAddressId()))) {
              cusSettings.setDeliveryAddressId(Long.valueOf(add.getId()));
              isAddressUpdated.set(true);
            }
          });
      if (isAddressUpdated.get()) {
        customerSettingsRepo.save(cusSettings);
      }
    }

    final PaymentMethod paymentMethod =
        paymentRepo.findOneByDescCode(customer.getCashOrCreditTypeCode())
            .orElseThrow(() -> new UnsupportedOperationException(String
                .format("Payment method %s is not supported", customer.getCashOrCreditTypeCode())));

    final DeliveryType deliveryType = deliveryTypesRepo.findOneByDescCode(customer.getSendMethod())
        .orElseThrow(() -> new UnsupportedOperationException(
            String.format("Delivery Type %s is not supported", customer.getSendMethod())));

    final InvoiceType invoiceType =
        invoiceTypeService.getInvoiceTypeByCode(customer.getInvoiceTypeCode())
            .orElseThrow(() -> new UnsupportedOperationException(
                String.format("Invoice Type %s is not supported", customer.getInvoiceTypeCode())));

    // Avoid hitting database if settings are not changed
    if (paymentMethod.equals(cusSettings.getPaymentMethod())
        && deliveryType.getId() == cusSettings.getDeliveryId()
        && invoiceType.equals(cusSettings.getInvoiceType())) {
      return cusSettings;
    }
    cusSettings.setPaymentMethod(paymentMethod);
    cusSettings.setDeliveryId(deliveryType.getId());
    cusSettings.setInvoiceType(invoiceType);
    return customerSettingsRepo.save(cusSettings);
  }

}
