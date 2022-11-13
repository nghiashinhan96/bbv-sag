package com.sagag.services.tools.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sagag.services.tools.ax.translator.AxInvoiceTypeTranslator;
import com.sagag.services.tools.ax.translator.AxPaymentTypeTranslator;
import com.sagag.services.tools.ax.translator.AxSalesOrderPoolTranslator;
import com.sagag.services.tools.ax.translator.AxSendMethodTranslator;
import com.sagag.services.tools.client.AxClient;
import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.ax.Address;
import com.sagag.services.tools.domain.ax.AxAddress;
import com.sagag.services.tools.domain.ax.AxAddressResourceSupport;
import com.sagag.services.tools.domain.ax.AxContact;
import com.sagag.services.tools.domain.ax.AxCustomer;
import com.sagag.services.tools.domain.ax.AxCustomerInfo;
import com.sagag.services.tools.domain.external.ContactInfo;
import com.sagag.services.tools.domain.external.Customer;
import com.sagag.services.tools.exception.AxCustomerException;
import com.sagag.services.tools.service.AxService;
import com.sagag.services.tools.support.SupportedAffiliate;
import com.sagag.services.tools.utils.AxAddressUtils;

/**
 * <p>
 * The service to get info from Ax Connection.
 * </p>
 *
 */
@Service
@OracleProfile
public class AxServiceImpl extends AxProcessor implements AxService {

  @Autowired
  private AxClient axClient;

  @Autowired
  private AxPaymentTypeTranslator axPaymentTypeTranslator;

  @Autowired
  private AxSendMethodTranslator axSendMethodTranslator;

  @Autowired
  private AxInvoiceTypeTranslator axInvoiceTypeTranslator;

  @Autowired
  private AxSalesOrderPoolTranslator axSalesOrderPoolTranslator;

  @Override
  public Optional<AxCustomerInfo> getActiveCustomerInfo(String compName, String custNr) throws AxCustomerException {
    if (StringUtils.isAnyBlank(compName, custNr)) {
      return Optional.empty();
    }
    final Optional<Customer> custOpt = findCustomerByNumber(compName, Long.valueOf(custNr));
    if (!custOpt.isPresent() || !custOpt.get().isActive()) {
      return Optional.empty();
    }

    final AxCustomerInfo info = new AxCustomerInfo();
    custOpt.ifPresent(info::setCustomer);
    info.setAddresses(searchCustomerAddresses(compName, custNr));

    return Optional.of(info);
  }


  @Override
  public List<Address> searchCustomerAddresses(String companyName, String customerNr) {
    ResponseEntity<AxAddressResourceSupport> addressesRes =
        axClient.getAddressesOfCustomer(getAxToken(), companyName, customerNr);
    if (!addressesRes.hasBody() || !addressesRes.getBody().hasAddresses()) {
      return Collections.emptyList();
    }

    return addressesRes.getBody().getAddresses().stream()
        .filter(AxAddressUtils.filterActiveDeliveryOrInvoiceAddress())
        .map(AxAddress::toAddressDto).collect(Collectors.toList());
  }


  private Optional<Customer> findCustomerByNumber(String companyName, Long customerNr) throws AxCustomerException {
    final Supplier<ResponseEntity<AxCustomer>> supplier =
        () -> axClient.getCustomerByNr(getAxToken(), companyName, String.valueOf(customerNr));
    final ResponseEntity<AxCustomer> customerRes = getOrThrow(supplier, AxCustomerException::new);
    final AxCustomer axCustomer = customerRes.getBody();
    final Customer customer = axCustomer.toCustomerDto();

    // Map Ax cash or credit type to Connect cash or credit type
    customer.setCashOrCreditTypeCode(
        axPaymentTypeTranslator.translateToConnect(customer.getAxPaymentType()).name());

    // Map Ax send method to Connect send method
    final String sendMethod =
        axSendMethodTranslator.translateToConnect(customer.getAxSendMethod());
    customer.setSendMethod(sendMethod);
    customer.setSendMethodCode(sendMethod);

    // Map Ax invoice type to Connect invoice type code
    customer.setInvoiceTypeCode(
        axInvoiceTypeTranslator.translateToConnect(customer.getAxInvoiceType()).name());

    // Map Ax sales order pool with affiliate info
    final SupportedAffiliate affiliate =
        axSalesOrderPoolTranslator.translateToConnect(customer.getAxSalesOrderPool());
    customer.setAffiliateShortName(affiliate.getAffiliate());
    customer.setAffiliateName(affiliate.getCompanyName());

    // Filter and update contact from AX ERP
    final List<AxContact> contacts = axCustomer.getContacts();
    final List<ContactInfo> phones =
        AxAddressUtils.findContactsByType(contacts, AxAddressUtils.PHONE_CONTACT_TYPE)
        .stream().map(AxContact::toDto).collect(Collectors.toList());
    final List<ContactInfo> faxs =
        AxAddressUtils.findContactsByType(contacts, AxAddressUtils.FAX_CONTACT_TYPE)
        .stream().map(AxContact::toDto).collect(Collectors.toList());
    final List<ContactInfo> emails =
        AxAddressUtils.findContactsByType(contacts, AxAddressUtils.EMAIL_CONTACT_TYPE)
        .stream().map(AxContact::toDto).collect(Collectors.toList());

    customer.setPhoneContacts(phones);
    customer.setFaxContacts(faxs);
    customer.setEmailContacts(emails);

    return Optional.of(customer);
  }

}
