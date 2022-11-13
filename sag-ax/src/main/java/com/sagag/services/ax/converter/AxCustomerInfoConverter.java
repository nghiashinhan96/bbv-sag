package com.sagag.services.ax.converter;

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.ax.domain.AxCustomer;
import com.sagag.services.ax.enums.CustomerBlockStatus;
import com.sagag.services.ax.translator.AxInvoiceTypeTranslator;
import com.sagag.services.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.ax.translator.AxSalesOrderPoolTranslator;
import com.sagag.services.ax.translator.AxSendMethodTranslator;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.external.CustomerBranch.CustomerBranchBuilder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AxProfile
public class AxCustomerInfoConverter implements Function<AxCustomer, Customer> {

  @Autowired
  private AxSendMethodTranslator axSendMethodTranslator;
  @Autowired
  private AxInvoiceTypeTranslator axInvoiceTypeTranslator;
  @Autowired
  private AxSalesOrderPoolTranslator axSalesOrderPoolTranslator;
  @Autowired
  private AxPaymentTypeTranslator axPaymentTypeTranslator;
  @Autowired
  private CustomerExternalService customerExtService;

  @Override
  public Customer apply(AxCustomer axCustomer) {
    final Customer customer = axCustomer.toCustomerDto();
    customer.setActive(isActiveCustomer(axCustomer.getBlockedStatus()));
    customer.setBlockReason(axCustomer.getBlockedReason());

    // Map Ax cash or credit type to Connect cash or credit type
    customer.setCashOrCreditTypeCode(
        axPaymentTypeTranslator.translateToConnect(customer.getAxPaymentType()).name());

    // Map Ax send method to Connect send method
    final String sendMethod = axSendMethodTranslator.translateToConnect(customer.getAxSendMethod());
    customer.setSendMethod(sendMethod);
    customer.setSendMethodCode(sendMethod);

    // Map Ax invoice type to Connect invoice type code
    customer.setInvoiceTypeCode(
        axInvoiceTypeTranslator.translateToConnect(customer.getAxInvoiceType()).name());

    // Map Ax sales order pool with affiliate info
    final SupportedAffiliate affiliate = axSalesOrderPoolTranslator.translateToConnect(
        customer.getAxSalesOrderPool());
    customer.setAffiliateShortName(affiliate.getAffiliate());
    customer.setAffiliateName(affiliate.getCompanyName());

    final String defaultBranchId = axCustomer.getDefaultBranchId();
    if (StringUtils.isNotBlank(defaultBranchId)) {
      CustomerBranchBuilder branchBuilder = CustomerBranch.builder().branchId(defaultBranchId);
      try {
        final Optional<CustomerBranch> defaultBranch =
            customerExtService.searchBranchById(affiliate.getCompanyName(), defaultBranchId);
        defaultBranch.ifPresent(
            customerBranch -> customer
                .setBranch(branchBuilder.branchName(customerBranch.getBranchName()).build()));

      } catch (RestClientException ex) {
        log.error("Ax API external has error: ", ex);
        // Set as default if throw exception
        customer.setBranch(branchBuilder.branchName(StringUtils.EMPTY).build());
      }
    }
    return customer;
  }

  private static boolean isActiveCustomer(CustomerBlockStatus blockedStatus) {
    return blockedStatus != null && !blockedStatus.isBlocked();
  }
}
