package com.sagag.services.tools.batch.sales.sso.importing;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.SsoSalesUser;
import com.sagag.services.tools.domain.SsoSalesUserOutput;
import com.sagag.services.tools.domain.target.AadAccounts;
import com.sagag.services.tools.domain.target.CollectiveDelivery;
import com.sagag.services.tools.domain.target.DeliveryType;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.ExternalUser;
import com.sagag.services.tools.domain.target.InvoiceType;
import com.sagag.services.tools.domain.target.Language;
import com.sagag.services.tools.domain.target.Login;
import com.sagag.services.tools.domain.target.PaymentMethod;
import com.sagag.services.tools.domain.target.Salutation;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.CollectiveDeliveryRepository;
import com.sagag.services.tools.repository.target.DeliveryTypesRepository;
import com.sagag.services.tools.repository.target.InvoiceTypeRepository;
import com.sagag.services.tools.repository.target.LanguageRepository;
import com.sagag.services.tools.repository.target.PaymentMethodRepository;
import com.sagag.services.tools.repository.target.SalutationRepository;
import com.sagag.services.tools.security.crypto.crypt3.Crypt3PasswordEncoder;
import com.sagag.services.tools.support.ExternalApp;
import com.sagag.services.tools.utils.UserSettingConstants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@StepScope
@Component
@OracleProfile
@Transactional
public class SsoSalesUserImportingItemProcessor implements ItemProcessor<SsoSalesUser, SsoSalesUserOutput>, InitializingBean {

  private static final String DEFAULT_GENDER = "Male";

  private static final String PERMITED_GROUP_SALES = "SALES";

  private static final String DEFAULT_PHONE_NUMBER = "123456789";

  private static final double DEFAULT_HOURLY_RATE = 11;

  private static final int DEFAULT_LANGUAGE_ID = 1;

  private static final int DEFAULT_SALUTATION = 1;

  private static final String DEFAULT_PERSONAL_NR = "9999";

  private static final String DEFAULT_LEGAL_ENTITY_ID = "30DA";

  @Autowired
  private CollectiveDeliveryRepository collectiveDeliveryRepo;

  @Autowired
  private DeliveryTypesRepository deliveryTypesRepo;

  @Autowired
  private PaymentMethodRepository paymentRepo;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepo;

  @Autowired
  private LanguageRepository languageRepo;

  @Autowired
  private SalutationRepository salutationRepo;

  private CollectiveDelivery defaultCollectiveDelivery;

  private DeliveryType defaultDeliveryType;

  private PaymentMethod defaultPaymentMethod;

  private InvoiceType defaultInvoiceType;

  private Language defaultLanguage;

  private Salutation defaultSalutation;

  @Override
  public void afterPropertiesSet() throws Exception {
    defaultCollectiveDelivery = collectiveDeliveryRepo.findById(UserSettingConstants.COLLECTIVE_DELIVERY_ID_DEFAULT)
        .orElseThrow(() -> new IllegalArgumentException("Not found collectiveDelivery"));
    defaultDeliveryType =
        deliveryTypesRepo.findById(UserSettingConstants.DELIVERY_ID_DEFAULT).orElseThrow(() -> new IllegalArgumentException("Not found deliveryType"));
    defaultPaymentMethod =
        paymentRepo.findById(UserSettingConstants.PAYMENT_METHOD_ID_DEFAULT).orElseThrow(() -> new IllegalArgumentException("Not found paymentMethod"));
    defaultInvoiceType =
        invoiceTypeRepo.findById(UserSettingConstants.INVOICE_TYPE_ID_DEFAULT).orElseThrow(() -> new IllegalArgumentException("Not found invoiceType"));
    defaultLanguage = languageRepo.findById(DEFAULT_LANGUAGE_ID).orElseThrow(() -> new IllegalArgumentException("Not found language"));
    defaultSalutation = salutationRepo.findById(DEFAULT_SALUTATION).orElseThrow(() -> new IllegalArgumentException("Not found salutation"));
  }

  @Override
  public SsoSalesUserOutput process(SsoSalesUser ssoSalesUser) throws Exception {
    return SsoSalesUserOutput.builder().aadAccounts(createAadAccounts(ssoSalesUser)).externalUser(createExternalUser(ssoSalesUser.getUserName()))
        .eshopUser(createEshopUser(ssoSalesUser)).userSettings(createUserSettings()).login(createLogin()).build();
  }

  private AadAccounts createAadAccounts(SsoSalesUser ssoSalesUser) {
    String personalNr = ssoSalesUser.getPersonalNumber();
    String legalEntityId = ssoSalesUser.getLegalEntityId();
    return AadAccounts.builder().primaryContactEmail(ssoSalesUser.getEmail()).firstName(ssoSalesUser.getFirstName()).lastName(ssoSalesUser.getSurName())
        .permitGroup(PERMITED_GROUP_SALES).createdDate(Calendar.getInstance().getTime()).gender(DEFAULT_GENDER)
        .personalNumber(StringUtils.isNotBlank(personalNr) ? personalNr : DEFAULT_PERSONAL_NR)
        .legalEntityId(StringUtils.isNotBlank(legalEntityId) ? legalEntityId : DEFAULT_LEGAL_ENTITY_ID).build();
  }

  private UserSettings createUserSettings() {
    final UserSettings userSettings = new UserSettings();
    userSettings.setAllocationId(UserSettingConstants.ALLOCATION_ID_DEFAULT);
    userSettings.setCollectiveDelivery(defaultCollectiveDelivery);

    userSettings.setDeliveryType(defaultDeliveryType);
    userSettings.setPaymentMethod(defaultPaymentMethod);
    userSettings.setInvoiceType(defaultInvoiceType);
    userSettings.setClassicCategoryView(UserSettingConstants.CLASSIC_CATEGORY_VIEW_DEFAULT);
    return userSettings;
  }

  private EshopUser createEshopUser(SsoSalesUser ssoSalesUser) {
    final EshopUser eshopUser = new EshopUser();
    eshopUser.setUsername(ssoSalesUser.getUserName());
    eshopUser.setEmail(ssoSalesUser.getEmail());
    eshopUser.setFirstName(ssoSalesUser.getFirstName());
    eshopUser.setLastName(ssoSalesUser.getSurName());
    eshopUser.setPhone(DEFAULT_PHONE_NUMBER);
    eshopUser.setLanguage(defaultLanguage);
    eshopUser.setSalutation(defaultSalutation);
    eshopUser.setHourlyRate(DEFAULT_HOURLY_RATE);
    return eshopUser;
  }

  private Login createLogin() {
    final Login login = new Login();
    login.setUserActive(true);
    return login;
  }

  private ExternalUser createExternalUser(String username) {
    return ExternalUser.builder().externalApp(ExternalApp.AX).username(username).password(new Crypt3PasswordEncoder().encode(username)).active(true)
        .createdDate(Calendar.getInstance().getTime()).build();
  }
}
