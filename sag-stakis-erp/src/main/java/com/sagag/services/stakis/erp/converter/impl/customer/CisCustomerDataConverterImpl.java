package com.sagag.services.stakis.erp.converter.impl.customer;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.enums.PriceDisplaySelection;
import com.sagag.services.common.enums.PriceDisplayStrategy;
import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.stakis.erp.converter.CisCustomerDataConverter;
import com.sagag.services.stakis.erp.dto.StakisCustomer;
import com.sagag.services.stakis.erp.enums.StakisContactType;
import com.sagag.services.stakis.erp.enums.StakisCustomerDataType;
import com.sagag.services.stakis.erp.translator.CisContactTranslator;
import com.sagag.services.stakis.erp.translator.CisCustomerBranchTranslator;
import com.sagag.services.stakis.erp.translator.CisDeliveryInfoTranslator;
import com.sagag.services.stakis.erp.translator.CisInvoiceTypeTranslator;
import com.sagag.services.stakis.erp.translator.CisPaymentDataTranslator;
import com.sagag.services.stakis.erp.wsdl.cis.Account;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfContact;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfNote;
import com.sagag.services.stakis.erp.wsdl.cis.CustomerDetails;
import com.sagag.services.stakis.erp.wsdl.cis.CustomerItem;
import com.sagag.services.stakis.erp.wsdl.cis.CustomerState;
import com.sagag.services.stakis.erp.wsdl.cis.DispatchTypeList;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;
import com.sagag.services.stakis.erp.wsdl.cis.SalesOutletList;

@Component
@Order(1)
@CzProfile
public class CisCustomerDataConverterImpl implements CisCustomerDataConverter<Customer> {

  private static final int CUSTOMER_ACTIVE_STATE = 0;

  @Autowired
  private CisPaymentDataTranslator paymentDataTranslator;

  @Autowired
  private CisInvoiceTypeTranslator invoiceTypeTranslator;

  @Autowired
  private CisDeliveryInfoTranslator deliveryInfoTranslator;

  @Autowired
  private CisCustomerBranchTranslator custBranchTranslator;

  @Autowired
  private CisContactTranslator contactTranslator;

  @Override
  public Customer apply(OutCustomer outCustomer) {
    final Optional<CustomerDetails> custDetailsOpt = getValueOpt(outCustomer.getCustomer());

    // Common customer information
    final CustomerDetails customerDetails = custDetailsOpt
        .orElseThrow(() -> new NoSuchElementException("Not found customer details element."));

    final StakisCustomer customer = new StakisCustomer();
    this.transferCommonCustomerInfo(customer, customerDetails);

    // Customer State
    final Optional<CustomerState> custStateOpt = getValueOpt(customerDetails.getState());
    if (!custStateOpt.isPresent()) {
      customer.setActive(true); // Always set active if ERP no return this object(no clarify)
    } else {
      custStateOpt.ifPresent(custState -> this.trasferCustomerStateInfo(customer, custState));
    }


    // Customer Contact Information
    final Optional<ArrayOfContact> arrayOfContactOpt = getValueOpt(customerDetails.getContacts());
    arrayOfContactOpt.ifPresent(aOContact -> this.transferContactInfo(customer, aOContact));

    // Customer tour information
    final Optional<String> tourInfoOpt = getValueOpt(customerDetails.getTourInfo());
    tourInfoOpt.ifPresent(tourInfo -> this.transferCustomerTourInfo(customer, tourInfo));

    // Customer Payment information
    final Optional<ArrayOfNote> arrOfNoteOpt = getValueOpt(customerDetails.getNotes());
    arrOfNoteOpt.ifPresent(arrOfNote -> this.transferPaymentInfo(customer, arrOfNote));

    // Customer Invoice information
    final Optional<CustomerItem> invoiceMethodCustomerItemOpt = Optional.empty();
    invoiceMethodCustomerItemOpt.ifPresent(cItem -> this.transferInvoiceInfo(customer, cItem));

    // Customer Branch information
    final Optional<SalesOutletList> salesOutletListOpt =
        getValueOpt(customerDetails.getSalesOutletList());
    salesOutletListOpt.ifPresent(sOList -> this.transferCustomerBranchInfo(customer, sOList));

    // Customer delivery information
    final Optional<DispatchTypeList> dispatchTypeListOpt =
        getValueOpt(customerDetails.getDispatchTypeList());
    dispatchTypeListOpt.ifPresent(dTypeList -> this.transferDeliveryInfo(customer, dTypeList));

    // #1574: [ERP Module Integration] Get Customer: customer info, addresses, etc.
    // From @Simon: There is no such concept here, so the check can be removed.
    customer.setSalesRepPersonalNumber(StringUtils.EMPTY);

    return customer;
  }

  /**
   * Transfers the common customer information from CIS customer.
   *
   * @param customer the target customer information
   * @param customerDetails the CIS customer details
   */
  private void transferCommonCustomerInfo(final StakisCustomer customer,
      final CustomerDetails customerDetails) {
    customer.setId(getValueOpt(customerDetails.getId()).orElse(StringUtils.EMPTY));
    Optional.ofNullable(customer.getId()).filter(StringUtils::isNotBlank)
    .map(NumberUtils::toLong).ifPresent(customer::setNr);

    final String compName = getValueOpt(customerDetails.getCompanyName())
        .map(StringUtils::trim).orElse(StringUtils.EMPTY);
    customer.setCompanyName(compName);
    customer.setName(compName);
    customer.setLanguage(LocaleContextHolder.getLocale().getLanguage());
    customer.setLastName(StringUtils.EMPTY); // N/A
    customer.setPriceDisplayStrategy(PriceDisplayStrategy.NET_GROSS_DISCOUNT);
    customer.setPriceDisplaySelection(PriceDisplaySelection.INHERIT_FROM_GLOBAL);
    final Optional<Account> custAccountOpt = getValueOpt(customerDetails.getAccount());
    custAccountOpt.ifPresent(account -> {
      customer.setCurrency(getValueOpt(account.getCurrencyCode()).orElse(StringUtils.EMPTY));
      customer.setVatNr(getValueOpt(account.getInformation()).orElse(StringUtils.EMPTY));
    });
  }

  /**
   * Transfers the customer state information from CIS customer.
   *
   * @param customer the target customer information
   * @param custState the CIS customer state
   */
  private void trasferCustomerStateInfo(final Customer customer,
      final CustomerState custState) {
    customer.setActive(activeCustomerPredicate().test(custState));
    if (!customer.isActive()) {
      customer.setBlockReason(custState.getDescription().getValue());
    }
  }

  private static Predicate<CustomerState> activeCustomerPredicate() {
    return bCustomer -> bCustomer == null || bCustomer.getState() == CUSTOMER_ACTIVE_STATE;
  }

  /**
   * Transfers the customer contact information from CIS customer.
   *
   * @param customer the target customer information
   * @param arrayOfContact the CIS array of contact of customer
   */
  private void transferContactInfo(final Customer customer,
      final ArrayOfContact arrayOfContact) {
    final Map<StakisContactType, List<ContactInfo>> contactMap =
        contactTranslator.translateToConnect(arrayOfContact);
    customer.setPhoneContacts(contactMap.get(StakisContactType.PHONE));
    customer.setEmailContacts(contactMap.get(StakisContactType.EMAIL));
    customer.setFaxContacts(contactMap.get(StakisContactType.FAX));
  }

  /**
   * Transfers the payment information from CIS customer.
   *
   * @param customer the target customer information
   * @param arrOfNote the CIS array of note of customer
   */
  private void transferPaymentInfo(final Customer customer,
      final ArrayOfNote arrOfNote) {
    final PaymentMethodType paymentMethod = paymentDataTranslator.translateToConnect(arrOfNote);
    customer.setCashOrCreditTypeCode(paymentMethod.name());
    customer.setAxPaymentType(paymentMethod.name());
  }

  /**
   * Transfers the invoice info from CIS customer.
   *
   * @param customer the target customer information
   * @param invoiceMethodCustomerItem the CIS invoice method customer item
   */
  private void transferInvoiceInfo(final Customer customer,
      final CustomerItem invoiceMethodCustomerItem) {
    final ErpInvoiceTypeCode invoiceTypeCode =
        invoiceTypeTranslator.translateToConnect(invoiceMethodCustomerItem);
    customer.setInvoiceTypeCode(invoiceTypeCode.name());
  }

  /**
   * Transfers the delivery info from CIS customer.
   *
   * @param customer the target customer information
   * @param dispatchType the CIS dispatch type
   */
	private void transferDeliveryInfo(final Customer customer, final DispatchTypeList dispatchTypeList) {
		final Map<SendMethodType, List<DeliveryTypeDto>> deliveryTypesByDefaultValue = deliveryInfoTranslator
				.translateToConnect(dispatchTypeList);
		Optional<SendMethodType> defaultSendMethodType = deliveryTypesByDefaultValue.entrySet().stream()
				.map(Entry::getKey).findFirst();
		List<DeliveryTypeDto> deliveryTypes = deliveryTypesByDefaultValue.entrySet().stream().map(Entry::getValue)
				.findFirst().orElseGet(ArrayList::new);
		customer.setSendMethod(defaultSendMethodType.map(SendMethodType::name).orElse(StringUtils.EMPTY));
		customer.setSendMethodCode(defaultSendMethodType.map(SendMethodType::name).orElse(StringUtils.EMPTY));
		customer.setStakisDeliveryTypes(deliveryTypes);
	}

  /**
   * Transfers the customer branch information from CIS customer.
   *
   * @param customer the target customer information
   * @param salesOutlet the CIS sales outlet of customer
   */
  private void transferCustomerBranchInfo(final Customer customer,
      final SalesOutletList salesOutletList) {
    custBranchTranslator.translateToConnect(salesOutletList)
    .ifPresent(custBranch -> {
      customer.setDefaultBranchId(custBranch.getBranchId());
      customer.setBranch(custBranch);
    });
  }

  /**
   * Transfers the customer tour information from CIS customer.
   *
   * @param customer the target customer information
   */
  private void transferCustomerTourInfo(final StakisCustomer customer, final String tourInfo) {
    customer.setClosetTour(tourInfo);
  }

  @Override
  public StakisCustomerDataType type() {
    return StakisCustomerDataType.CUSTOMER_INFO;
  }

  @Override
  public void accept(CustomerInfo customerInfo, OutCustomer outCustomer) {
    customerInfo.setCustomer(apply(outCustomer));
  }

}
