package com.sagag.services.service.user.setting;

import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.PriceDisplaySelection;
import com.sagag.services.common.enums.PriceDisplayStrategy;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.api.EshopGlobalSettingCacheService;
import com.sagag.services.hazelcast.domain.EshopGlobalSettingDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EshopUserSyncAxSetting implements AxSettingsSynchronizer {

  private static final String PRICE_DISPLAY_SELECTION_KEY = "price_display_selection";

  @Autowired
  @Qualifier("eshopGlobalSettingCacheServiceImpl")
  private EshopGlobalSettingCacheService eshopGlobalSettingCacheService;

  @Override
  public void syncAxSetting(UserInfo user) {
    OwnSettings ownSettings = user.getSettings();
    ownSettings.setPriceDisplayChanged(isPriceDisplayChanged(user.getCustomer()));
    ownSettings.setShowDiscount(isShowDiscount(user.getCustomer())
        && user.getSettings().getUserSettings().isShowDiscount());
    ownSettings.setShowGross(isShowGross(user.getCustomer()));
    ownSettings.setPriceTypeDisplayEnum(getPriceTypeDisplayEnum(user));
  }

  @Override
  public boolean take(UserInfo user) {
    return !user.isFinalUserRole();
  }

  private boolean isPriceDisplayChanged(Customer customer) {
    if (Objects.isNull(customer)) {
      return false;
    }

    PriceDisplaySelection priceDisplaySelection = customer.getPriceDisplaySelection();
    // #1560
    if (priceDisplaySelection == PriceDisplaySelection.INHERIT_FROM_GLOBAL) {
      return eshopGlobalSettingCacheService.getSettingByCode(PRICE_DISPLAY_SELECTION_KEY)
          .map(EshopGlobalSettingDto::isEnabled).orElse(false);
    }

    return priceDisplaySelection == PriceDisplaySelection.TRUE;
  }

  private boolean isShowDiscount(Customer customer) {
    if (Objects.isNull(customer)) {
      return false;
    }

    return customer.getPriceDisplayStrategy() == PriceDisplayStrategy.NET_GROSS_DISCOUNT;
  }

  private boolean isShowGross(Customer customer) {
    if (Objects.isNull(customer)) {
      return false;
    }
    final PriceDisplayStrategy priceDisplay = customer.getPriceDisplayStrategy();
    return priceDisplay == PriceDisplayStrategy.NET_GROSS_DISCOUNT
        || priceDisplay == PriceDisplayStrategy.NET_GROSS;
  }

  private PriceDisplayTypeEnum getPriceTypeDisplayEnum(UserInfo userInfo) {

    // #1553
    if (userInfo.hasCust()
        && userInfo.getCustomer().getPriceDisplayStrategy() == PriceDisplayStrategy.NET) {
      if (isSagCzAffiliate(userInfo)) {
        return PriceDisplayTypeEnum.DPC;
      }
      return PriceDisplayTypeEnum.UVPE_OEP;
    }
    if (isSagCzAffiliate(userInfo)) {
      return PriceDisplayTypeEnum.DPC_GROSS;
    }
    return PriceDisplayTypeEnum.UVPE_OEP_GROSS;
  }

  private boolean isSagCzAffiliate(UserInfo userInfo) {
    return Objects.nonNull(userInfo.getSupportedAffiliate())
        && userInfo.getSupportedAffiliate().isSagCzAffiliate();
  }

}
