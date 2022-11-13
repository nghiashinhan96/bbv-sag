package com.sagag.services.stakis.erp.api.impl;

import com.sagag.services.article.api.CustomerExternalService;
import com.sagag.services.article.api.EmployeeExternalService;
import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.sag.erp.Address;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.external.Employee;
import com.sagag.services.stakis.erp.enums.StakisCustomerDataType;
import com.sagag.services.stakis.erp.translator.CisPaymentDataTranslator;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfNote;
import com.sagag.services.stakis.erp.wsdl.cis.Note;
import com.sagag.services.stakis.erp.wsdl.cis.ObjectFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@CzProfile
public class StakisErpCustomerExternalServiceImpl
    extends StakisProcessor implements CustomerExternalService, EmployeeExternalService {

  private static final String DF_SALES_NUMBER = "CZDF0000001";

  @Autowired
  @Qualifier("cisObjectFactory")
  private ObjectFactory cisObjectFactory;

  @Override
  public Optional<Customer> findCustomerByNumber(String companyName, String customerNr) {
    return getCustomerInfoByTypes(companyName, customerNr, StakisCustomerDataType.CUSTOMER_INFO)
        .map(CustomerInfo::getCustomer);
  }

  @Override
  public List<Address> searchCustomerAddresses(String companyName, String customerNr) {
    return getCustomerInfoByTypes(companyName, customerNr,
        StakisCustomerDataType.CUSTOMER_ADDRESSES).map(CustomerInfo::getAddresses)
        .orElseGet(Collections::emptyList);
  }

  @Override
  public Optional<Address> findCustomerAddressById(String company, String customerNr,
      String addressId) {
    return searchCustomerAddresses(company, customerNr).stream()
        .filter(address -> StringUtils.equals(address.getId(), addressId))
        .findFirst();
  }

  @Override
  public Optional<CustomerBranch> searchBranchById(String companyName, String branchId) {
    return Optional.empty();
  }

  @Override
  public List<CustomerBranch> getCustomerBranches(String companyName, String defaultBranchId,
          boolean isSaleOnBehalf) {
    return Collections.emptyList();
  }

  @Override
  public Optional<CreditLimitInfo> getCreditLimitInfoByCustomer(String compName, String custNr) {
    return getCustomerInfoByTypes(compName, custNr, StakisCustomerDataType.CUSTOMER_CREDIT_LIMIT)
        .map(CustomerInfo::getCreditLimitInfo);
  }

  @Override
  public Optional<CustomerInfo> getActiveCustomerInfo(String compName, String custNr) {
    return getCustomerInfoByTypes(compName, custNr, StakisCustomerDataType.values())
        .filter(CustomerInfo::isActivedCustomer);
  }

  @Override
  public Optional<Employee> findEmployee(String companyName, String username) {
    if (StringUtils.isAnyBlank(companyName, username)) {
      return Optional.empty();
    }
    final Employee employee = new Employee();
    employee.setId(DF_SALES_NUMBER);
    employee.setName(username);
    employee.setAbbreviation(StringUtils.EMPTY);
    employee.setContacts(Collections.emptyList());
    return Optional.of(employee);
  }

  @Override
  public PaymentMethodType getConnectPaymentForSales(String paymentType, boolean isValidKSL) {
    final Note note = cisObjectFactory.createNote();
    note.setDescription(cisObjectFactory.createNoteDescription(
        CisPaymentDataTranslator.METHOD_OF_PAYMENT_TXT));
    note.setText(cisObjectFactory.createNoteText(paymentType));

    final ArrayOfNote arrOfNote = cisObjectFactory.createArrayOfNote();
    arrOfNote.getNote().add(note);

    return PaymentMethodType.valueOf(paymentType);
  }

}
