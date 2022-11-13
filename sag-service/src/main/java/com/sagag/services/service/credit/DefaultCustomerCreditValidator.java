package com.sagag.services.service.credit;

import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.api.OrganisationCollectionService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.domain.eshop.dto.CustomerCreditCheckResultDto;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.domain.cart.ShoppingCart;
import com.sagag.services.hazelcast.domain.user.EshopBasketContext;
import com.sagag.services.service.api.CartBusinessService;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

public class DefaultCustomerCreditValidator implements CustomerCreditValidator {

  private static final String PAYMENT_ADDITIONAL_CREDIT_KEY_PREFIX = "payment_additional_credit_";

  @Autowired
  private CartBusinessService cartBusService;

  @Autowired
  private ContextService contextService;

  @Autowired
  private UserSettingsRepository userSettingsRepo;

  @Autowired
  private OrganisationCollectionService orgCollectionService;

  @Override
  public CustomerCreditCheckResultDto validate(UserInfo user, double customerCredit,
      double customerCashCreditPayment, ShopType shopType) {
    // https://app.assembla.com/spaces/sag-eshop/tickets/1689
    Assert.notNull(user, "User must not be null");
    Assert.isTrue(user.isValidCustNr(), "Invalid ERP customer number");
    Assert.notNull(user.getCollectionId(), "Invalid Collection id");
    Assert.hasText(user.getAffiliateShortName(), "Affiliate name is required");
    Assert.hasText(user.getUsername(), "Username is required");

    final PaymentMethodType paymentMethod = getPaymentMethod(user);

    if (isSaleCz(user) || CustomerCreditCheckResultDto.isAlwaysValid(paymentMethod)) {
      return CustomerCreditCheckResultDto.of(paymentMethod, customerCredit);
    }

    final ShoppingCart shoppingCart = cartBusService.checkoutShopCart(user, shopType, true, false);

    return CustomerCreditCheckResultDto.process(paymentMethod, customerCredit,
        shoppingCart.getNetTotalExclVat(),
        getAdditionCreditFromSettings(user.getCollectionShortname(), paymentMethod));
  }

  private boolean isSaleCz(UserInfo user) {
    return user.isSaleOnBehalf() && user.getSupportedAffiliate().isCzAffiliate();
  }

  private PaymentMethodType getPaymentMethod(UserInfo user) {
    String paymentMethod = getPaymentMethodFromContext(user.key());
    if (StringUtils.isBlank(paymentMethod)) {
      paymentMethod = getPaymentMethodFromUserSetting(user.getId());
    }
    return PaymentMethodType.valueOfIgnoreCase(paymentMethod);
  }

  private String getPaymentMethodFromUserSetting(Long userId) {
    return userSettingsRepo.findByUserId(userId)
        .map(setting -> setting.getPaymentMethod().getDescCode()).orElse(StringUtils.EMPTY);
  }

  private String getPaymentMethodFromContext(final String userKey) {
    final EshopBasketContext basketContext = contextService.getBasketContext(userKey);
    if (basketContext == null || basketContext.getPaymentMethod() == null) {
      return StringUtils.EMPTY;
    }
    return basketContext.getPaymentMethod().getDescCode();
  }

  private double getAdditionCreditFromSettings(final String collectionShortname,
      final PaymentMethodType paymentMethod) {
    final String settingKey = StringUtils.join(PAYMENT_ADDITIONAL_CREDIT_KEY_PREFIX,
        StringUtils.lowerCase(paymentMethod.name()));
    return orgCollectionService
        .findSettingValueByCollectionShortnameAndKey(collectionShortname, settingKey)
        .map(NumberUtils::toDouble).orElse(NumberUtils.DOUBLE_ZERO);
  }
}
