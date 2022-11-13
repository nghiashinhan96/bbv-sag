package com.sagag.services.stakis.erp.converter.impl.customer;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.domain.customer.CreditLimitInfo;
import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.stakis.erp.converter.CisCustomerDataConverter;
import com.sagag.services.stakis.erp.enums.StakisCustomerDataType;
import com.sagag.services.stakis.erp.wsdl.cis.Account;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

@Component
@Order(3)
@CzProfile
public class CisCustomerCreditLimitConverterImpl implements CisCustomerDataConverter<CreditLimitInfo> {

  @Override
  public CreditLimitInfo apply(OutCustomer outCustomer) {
    final Account account = outCustomer.getCustomer().getValue().getAccount().getValue();
    final CreditLimitInfo creditLimit = new CreditLimitInfo();
    getValueOpt(account.getCreditLimit()).map(NumberUtils::createDouble)
    .ifPresent(creditLimit::setAvailableCredit);
    getValueOpt(account.getUsedCreditLimit()).map(NumberUtils::createDouble)
    .ifPresent(creditLimit::setAlreadyUsedCredit);
    return creditLimit;
  }

  @Override
  public StakisCustomerDataType type() {
    return StakisCustomerDataType.CUSTOMER_CREDIT_LIMIT;
  }

  @Override
  public void accept(CustomerInfo customerInfo, OutCustomer outCustomer) {
    customerInfo.setCreditLimitInfo(apply(outCustomer));
  }

}
