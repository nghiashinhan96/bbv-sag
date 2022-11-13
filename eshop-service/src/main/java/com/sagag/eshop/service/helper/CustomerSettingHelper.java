package com.sagag.eshop.service.helper;

import com.sagag.eshop.repo.api.InvoiceTypeRepository;
import com.sagag.eshop.repo.api.PaymentMethodRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.services.common.contants.UserSettingConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class CustomerSettingHelper {

  @Autowired
  private PaymentMethodRepository paymentRepo;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  public CustomerSettings buildDefaultCustomerSetting() {
    final CustomerSettings customerSetting = new CustomerSettings();
    customerSetting.setAllocationId(UserSettingConstants.ALLOCATION_ID_DEFAULT);
    customerSetting.setCollectiveDelivery(UserSettingConstants.COLLECTIVE_DELIVERY_ID_DEFAULT);
    customerSetting.setDeliveryId(UserSettingConstants.DELIVERY_ID_DEFAULT);
    customerSetting.setPaymentMethod(
        paymentRepo.findById(UserSettingConstants.PAYMENT_METHOD_ID_DEFAULT).orElse(null));
    customerSetting.setInvoiceType(
        invoiceTypeRepo.findById(UserSettingConstants.INVOICE_TYPE_ID_DEFAULT).orElse(null));

    return customerSetting;
  }
}
