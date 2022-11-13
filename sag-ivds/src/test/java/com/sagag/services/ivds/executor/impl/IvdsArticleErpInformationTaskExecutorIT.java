package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.executor.IvdsArticleErpInformationTaskExecutor;
import com.sagag.services.ivds.request.ArticleInformationRequestItem;
import com.sagag.services.ivds.request.ErpInfoRequest;
import com.sagag.services.ivds.request.GetArticleSyncInformation;
import com.sagag.services.ivds.response.GetArticleInformationResponse;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class IvdsArticleErpInformationTaskExecutorIT {

  @Autowired
  private IvdsArticleErpInformationTaskExecutor executor;

  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void shouldGetArticleWithOnlyAvailabilties() {
    final UserInfo user = DataProvider.buildUserInfo();
    final List<ArticleInformationRequestItem> items = buildAvailabilityRequestItems();
    ErpInfoRequest erpInfoRequest = new ErpInfoRequest();
    erpInfoRequest.setAvailabilityRequested(true);
    final GetArticleInformationResponse articlesInfoResponse =
        executor.executeErpInformation(user, ofRequest(items, erpInfoRequest));

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articlesInfoResponse));

    Assert.assertThat(articlesInfoResponse.getItems().isEmpty(), Matchers.is(false));
    Assert.assertThat(articlesInfoResponse.getItems().size(), Matchers.equalTo(items.size()));
    articlesInfoResponse.getItems().entrySet().forEach(artInfo -> {
      Assert.assertThat(artInfo.getValue().getAvailabilities().size(), Matchers.greaterThan(0));
      Assert.assertThat(artInfo.getValue().getPrice(), Matchers.nullValue());
      Assert.assertThat(artInfo.getValue().getStock(), Matchers.nullValue());
    });
  }

  @Test
  public void shouldGetArticleWithOnlyPrice() {
    final UserInfo user = DataProvider.buildUserInfo();
    final List<ArticleInformationRequestItem> items = buildAvailabilityRequestItems();
    ErpInfoRequest erpInfoRequest = new ErpInfoRequest();
    erpInfoRequest.setPriceRequested(true);
    final GetArticleInformationResponse articlesInfoResponse =
        executor.executeErpInformation(user, ofRequest(items, erpInfoRequest));

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articlesInfoResponse));

    Assert.assertThat(articlesInfoResponse.getItems().isEmpty(), Matchers.is(false));
    Assert.assertThat(articlesInfoResponse.getItems().size(), Matchers.equalTo(items.size()));
    articlesInfoResponse.getItems().entrySet().forEach(artInfo -> {
      Assert.assertThat(artInfo.getValue().getAvailabilities(),
          Matchers.anyOf(Matchers.notNullValue(), Matchers.nullValue()));
      Assert.assertThat(artInfo.getValue().getPrice(),
          Matchers.anyOf(Matchers.notNullValue(), Matchers.nullValue()));
      Assert.assertThat(artInfo.getValue().getStock(),
          Matchers.anyOf(Matchers.notNullValue(), Matchers.nullValue()));
    });
  }

  @Test
  public void shouldGetArticleWithPriceAndAvail() {
    final UserInfo user = DataProvider.buildUserInfo();
    final List<ArticleInformationRequestItem> items = buildAvailabilityRequestItems();
    ErpInfoRequest erpInfoRequest = new ErpInfoRequest();
    erpInfoRequest.setPriceRequested(true);
    erpInfoRequest.setAvailabilityRequested(true);
    final GetArticleInformationResponse articlesInfoResponse =
        executor.executeErpInformation(user, ofRequest(items, erpInfoRequest));

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(articlesInfoResponse));

    Assert.assertThat(articlesInfoResponse.getItems().isEmpty(), Matchers.is(false));
    Assert.assertThat(articlesInfoResponse.getItems().size(), Matchers.equalTo(items.size()));
    articlesInfoResponse.getItems().entrySet().forEach(artInfo -> {
      Assert.assertThat(artInfo.getValue().getAvailabilities(),
          Matchers.anyOf(Matchers.notNullValue(), Matchers.nullValue()));
      Assert.assertThat(artInfo.getValue().getPrice(),
          Matchers.anyOf(Matchers.notNullValue(), Matchers.nullValue()));
      Assert.assertThat(artInfo.getValue().getStock(),
          Matchers.anyOf(Matchers.notNullValue(), Matchers.nullValue()));
    });
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenEmptyItems_shouldThrowIllegalException() {
    final UserInfo user = DataProvider.buildUserInfo();
    executor.executeErpInformation(user, ofRequest(Collections.emptyList(), null));
  }

  private GetArticleSyncInformation ofRequest(List<ArticleInformationRequestItem> items,
      ErpInfoRequest erpInfoRequest) {
    GetArticleSyncInformation request = new GetArticleSyncInformation();
    request.setArticleInformationRequestItems(items);
    request.setErpInfoRequest(erpInfoRequest);
    return request;
  }

  private List<ArticleInformationRequestItem> buildAvailabilityRequestItems() {
    final List<ArticleInformationRequestItem> items = new ArrayList<>();

    items.add(ArticleInformationRequestItem.builder().idPim("1000950041").quantity(100).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1001230465").quantity(10).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1001230464").quantity(1).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1001513754").quantity(2).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1001260960").quantity(3).build());
    return items;
  }
}
