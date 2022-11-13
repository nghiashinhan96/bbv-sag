package com.sagag.services.dvse.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sagag.eshop.repo.entity.UserSettings;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.ax.enums.AustriaArticleAvailabilityState;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.dvse.DvseApplication;
import com.sagag.services.dvse.DvseDataProvider;
import com.sagag.services.dvse.api.CatalogService;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformation;
import com.sagag.services.dvse.wsdl.dvse.GetArticleInformationResponse;
import com.sagag.services.dvse.wsdl.dvse.Item;
import com.sagag.services.dvse.wsdl.dvse.SendOrder;
import com.sagag.services.dvse.wsdl.dvse.SendOrderResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { DvseApplication.class })
@EshopIntegrationTest
public class DvseCatalogServiceImplIT extends AbstractDvseCatalogServiceImplIT {

  @Autowired
  private CatalogService catalogService;

  @Before
  @Override
  public void init() {
    UserInfo userInfo = new UserInfo();
    userInfo.setId(26L);
    userInfo.setUsername("test.admin");
    userInfo.setAffiliateShortName(SupportedAffiliate.DERENDINGER_AT.getAffiliate());
    userInfo.setRoles(Arrays.asList(EshopAuthority.USER_ADMIN.name()));
    userInfo.setCompanyName(SupportedAffiliate.DERENDINGER_AT.getCompanyName());
    custExtService.findCustomerByNumber(userInfo.getCompanyName(), "1110914")
        .ifPresent(userInfo::setCustomer);
    OwnSettings settings = new OwnSettings();
    UserSettings uSettings = new UserSettings();
    uSettings.setId(1);
    settings.setUserSettings(uSettings);
    userInfo.setSettings(settings);
    // save the user info to cache when user login from econnect shop
    userCacheService.put(userInfo);
    initializeUserAppContext(userInfo.key()); // initialize user application context for testing
  }

  @Test
  @Ignore
  public void testGetArticleInformationSuccessfully() {

    GetArticleInformation request = DvseDataProvider.createGetArticleInfomation();
    GetArticleInformationResponse response = catalogService.getArticleInfos(request);

    Assert.assertNotNull(response);
  }

  @Test
  public void testGetArticleInformationSuccessfullyWithLockedStatus() {

    GetArticleInformation request =
        DvseDataProvider.createGetArticleInfomationWithLockedStatusOnES();
    GetArticleInformationResponse response = catalogService.getArticleInfos(request);

    Assert.assertNotNull(response);

    Assert.assertThat(
        assertArticlesWithLockedStatus(response, DvseDataProvider.pimIdIsLockedWithDat()),
        Matchers.equalTo(true));
  }

  private static boolean assertArticlesWithLockedStatus(GetArticleInformationResponse response,
      String expectedLockedArticleId) {

    List<Item> items = response.getGetArticleInformationResult().getItems().getItem();

    return items.stream()
        .filter(
            item -> StringUtils.equals(expectedLockedArticleId, item.getWholesalerArticleNumber()))
        .anyMatch(matchLockedArticle());
  }

  private static Predicate<Item> matchLockedArticle() {
    return item -> AustriaArticleAvailabilityState.YELLOW.getCode() == item.getAvailState()
        .getAvailState();
  }

  @Ignore("test on local only due to cache required")
  @Test
  public void testAddItemsToCart() {

    SendOrder request = DvseDataProvider.createAddItemsToCart();
    SendOrderResponse response = catalogService.addItemsToCart(request);

    Assert.assertNotNull(response);
    Assert.assertThat(response.getSendOrderResult().getItem().getOrderId(),
        Matchers.equalTo("ORDER_ID_1234567"));
  }

  @Ignore("test on local only due to cache required")
  @Test
  public void testAddItemsToCartWithDepotAndRecycle() {

    SendOrder request = DvseDataProvider.createAddItemsToCartWithDepotAndRecycleCase();
    SendOrderResponse response = catalogService.addItemsToCart(request);

    Assert.assertNotNull(response);
    Assert.assertThat(response.getSendOrderResult().getItem().getOrderId(),
        Matchers.equalTo("ORDER_ID_1234567"));
  }

}
