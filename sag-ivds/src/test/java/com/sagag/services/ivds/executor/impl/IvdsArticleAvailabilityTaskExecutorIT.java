package com.sagag.services.ivds.executor.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.ivds.DataProvider;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.request.ArticleInformationRequestItem;
import com.sagag.services.ivds.request.availability.GetArticleInformation;
import com.sagag.services.ivds.response.availability.GetArticleAvailabilityResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class IvdsArticleAvailabilityTaskExecutorIT {

  @Autowired
  private IvdsArticleAvailabilityTaskExecutorImpl executor;

  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }

  @Test
  public void shouldGetArticleAvailabilties() {
    final UserInfo user = DataProvider.buildUserInfo();
    final List<ArticleInformationRequestItem> items = buildAvailabilityRequestItems();

    final GetArticleAvailabilityResponse availabilities =
        executor.executeAvailabilities(user, ofRequest(items));

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(availabilities));

    Assert.assertThat(availabilities.getItems().isEmpty(), Matchers.is(false));
    Assert.assertThat(availabilities.getItems().size(), Matchers.equalTo(items.size()));
  }

  @Test
  public void givenInvalidItem_shouldReturnSafelyWithoutInvalidItem() {
    final UserInfo user = DataProvider.buildUserInfo();
    final List<ArticleInformationRequestItem> items = buildAvailabilityRequestItems();

    // Add one more invalid item
    items.set(0, ArticleInformationRequestItem.builder().idPim("AAAA").quantity(10).build());

    final GetArticleAvailabilityResponse availabilities =
        executor.executeAvailabilities(user, ofRequest(items));

    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(availabilities));

    Assert.assertThat(availabilities.getItems().isEmpty(), Matchers.is(false));
    Assert.assertThat(availabilities.getItems().size(), Matchers.equalTo(items.size() - 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenSixItems_shouldThrowIllegalException() {
    final UserInfo user = DataProvider.buildUserInfo();
    final List<ArticleInformationRequestItem> items = buildAvailabilityRequestItems();

    // Add one more item
    items.add(ArticleInformationRequestItem.builder().idPim("1000995049").quantity(10).build());

    executor.executeAvailabilities(user, ofRequest(items));
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenEmptyItems_shouldThrowIllegalException() {
    final UserInfo user = DataProvider.buildUserInfo();
    executor.executeAvailabilities(user, ofRequest(Collections.emptyList()));
  }

  private static GetArticleInformation ofRequest(List<ArticleInformationRequestItem> items) {
    GetArticleInformation request = new GetArticleInformation();
    request.setAvailabilityRequestItemList(items);
    return request;
  }

  private static List<ArticleInformationRequestItem> buildAvailabilityRequestItems() {
    final List<ArticleInformationRequestItem> items = new ArrayList<>();

    items.add(ArticleInformationRequestItem.builder().idPim("1000995049").quantity(100).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1001055177").quantity(10).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1001112351").quantity(1).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1001070451").quantity(2).build());
    items.add(ArticleInformationRequestItem.builder().idPim("1000326408").quantity(3).build());
    return items;
  }
}
