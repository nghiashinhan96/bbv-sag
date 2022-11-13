package com.sagag.services.stakis.erp.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sagag.services.article.api.domain.customer.CustomerInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.stakis.erp.client.StakisErpCisClient;
import com.sagag.services.stakis.erp.converter.CisCustomerDataConverter;
import com.sagag.services.stakis.erp.domain.TmUserCredentials;
import com.sagag.services.stakis.erp.enums.StakisCustomerDataType;
import com.sagag.services.stakis.erp.wsdl.cis.OutCustomer;

public class StakisProcessor {

  @Autowired
  private StakisErpCisClient stakisErpCisClient;

  @Autowired
  private List<CisCustomerDataConverter<?>> customerDataConverters;

  protected Optional<CustomerInfo> getCustomerInfoByTypes(String compName, String custNr,
      StakisCustomerDataType... types) {
    if (ArrayUtils.isEmpty(types)) {
      return Optional.empty();
    }
    final OutCustomer response =
        stakisErpCisClient.getCustomer(StringUtils.EMPTY, custNr, Locale.ENGLISH.getLanguage());

    final List<StakisCustomerDataType> customerDataTypes = Arrays.asList(types);
    final CustomerInfo customerInfo = new CustomerInfo();
    customerDataConverters.stream()
        .filter(converter -> customerDataTypes.contains(converter.type()))
        .forEach(converter -> converter.accept(customerInfo, response));

    // Update supported affiliate of customer
    SupportedAffiliate.fromCompanyNameSafely(compName).ifPresent(updateCustomerInfo(customerInfo));

    return Optional.of(customerInfo);
  }

  private static Consumer<SupportedAffiliate> updateCustomerInfo(final CustomerInfo customerInfo) {
    return aff -> {
      if (customerInfo.getCustomer() != null) {
        customerInfo.getCustomer().setAffiliateName(aff.getCompanyName());
        customerInfo.getCustomer().setAffiliateShortName(aff.getAffiliate());
      }
    };
  }

  protected TmUserCredentials buildTmUserCredentials(String username, String pass, String custId,
      String language) {
    final TmUserCredentials credentials = new TmUserCredentials();
    credentials.setUsername(username);
    credentials.setPassword(pass);
    credentials.setCustomerId(custId);
    credentials.setLang(language);
    return credentials;
  }

  protected TmUserCredentials buildTmUserCredentialsWithSalesAdvisor(String username, String pass,
      String custId, String language, String saleOriginId, String salesUsername) {
    TmUserCredentials credentialWithSaleOrigin =
        buildTmUserCredentials(username, pass, custId, language);
    credentialWithSaleOrigin.setSaleOriginId(saleOriginId);
    credentialWithSaleOrigin.setSalesUsername(salesUsername);
    return credentialWithSaleOrigin;
  }

}
