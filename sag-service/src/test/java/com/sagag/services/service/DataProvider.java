package com.sagag.services.service;

import com.google.common.collect.Maps;
import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.SettingUpdateDto;
import com.sagag.services.domain.eshop.dto.price.DisplayedPriceDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.domain.sag.erp.PriceWithArticle;
import com.sagag.services.domain.sag.erp.PriceWithArticlePrice;
import com.sagag.services.domain.sag.external.Customer;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import lombok.experimental.UtilityClass;

import org.assertj.core.util.Lists;

import java.util.Collections;
import java.util.Date;

/**
 * Utilities for data provider in test cases.
 */
@UtilityClass
public class DataProvider {

  public static final Long CUSTOMER_NR = 1111101L;
  public static final String CUSTOMER_NAME = "GARAGE D-Store AT";
  public static final Long USER_ID =  32L;
  public static final double VAT_RATE = 20;

  public static UserSettings createUserSetting() {
    return UserSettings.builder().id(38).netPriceView(true).netPriceConfirm(false)
        .classicCategoryView(false).singleSelectMode(false).build();
  }

  public static UserInfo createUserInfo() {
    final UserInfo userInfo = new UserInfo();
    userInfo.setId(USER_ID);
    userInfo.setOrganisationId(1);
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(createAxCustomer());
    userInfo.setSettings(ownSettings);
    userInfo.setCompanyName(SupportedAffiliate.DERENDINGER_AT.getCompanyName());
    userInfo.setAffiliateShortName(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    userInfo.setRoles(Collections.emptyList());
    return userInfo;
  }

  public static SettingUpdateDto createUserUpdateDto() {
    return SettingUpdateDto.builder().netPriceView(true).netPriceConfirm(true)
        .classicCategoryView(true).singleSelectMode(true).build();
  }

  public static Customer createAxCustomer() {
    return Customer.builder().name(CUSTOMER_NAME).nr(CUSTOMER_NR).links(Maps.newHashMap()).build();
  }

  public UserInfo initCustomerUser() {
    UserInfo userInfo = new UserInfo();
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(Customer.builder().build());
    userInfo.setSettings(ownSettings);

    return userInfo;
  }

  public UserInfo initSaleOnbehalfUser() {
    UserInfo user = new UserInfo();
    user.setSalesId(5L);
    OwnSettings ownSettings = new OwnSettings();
    user.setCustomer(Customer.builder().build());
    user.setSettings(ownSettings);
    return user;
  }

  public ShoppingCartItem buildShoppingCartItem(Date arrivalTime) {
    final ShoppingCartItem item = new ShoppingCartItem();

    final Availability availability = new Availability();
    availability.setArrivalTime(
        DateUtils.getUTCDateString(arrivalTime, DateUtils.UTC_DATE_PATTERN));

    final DisplayedPriceDto displayedPrice = new DisplayedPriceDto();
    displayedPrice.setType("Typ");
    displayedPrice.setBrand("Brand");

    final PriceWithArticlePrice priceWithArt = new PriceWithArticlePrice();
    priceWithArt.setType("Type");

    final PriceWithArticle price = new PriceWithArticle();
    price.setPrice(priceWithArt);

    final ArticleDocDto articleItem = new ArticleDocDto();
    articleItem.setAvailabilities(Lists.newArrayList(availability));
    articleItem.setPrice(price);
    articleItem.setDisplayedPrice(displayedPrice);

    item.setArticleItem(articleItem);
    return item;
  }
}
