package com.sagag.services.ivds.api.impl;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.ivds.app.IvdsApplication;
import com.sagag.services.ivds.response.ThuleArticleListSearchResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { IvdsApplication.class })
@EshopIntegrationTest
@Slf4j
public class IvdsThuleServiceImplIT extends BaseSearchServiceImplIT {

  private static final String[] KEYS_OF_MAP_BY_ORDER = {"dealer", "order_list"};

  private static final String DEALER_ID = "{test-ddat-0C9094D8-09DB-40C5-83A8-48D22750A7DB}";

  private static final String[] SAMPLE_BUYERS_GUIDE_DATA = new String[] { DEALER_ID,
      "721400_1|721500_2|6357B_2|6355B_1|6352B_33|OC90_1" };

  @Autowired
  private IvdsThuleServiceImpl service;

  @Test
  public void testSearchArticlesByBuyersGuide() {
    ThuleArticleListSearchResponse response =
        service.searchArticlesByBuyersGuide(user, buildSampleMap());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));
    Assert.assertThat(response.getThuleArticles().isEmpty(), Matchers.is(false));
    Assert.assertThat(response.getNotFoundPartNumbers().isEmpty(), Matchers.is(false));
  }

  @Test
  public void testSearchArticlesByBuyersGuideWithEmptyMap() {
    ThuleArticleListSearchResponse response =
        service.searchArticlesByBuyersGuide(user, Collections.emptyMap());
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));
    Assert.assertThat(response.getThuleArticles().isEmpty(), Matchers.is(true));
    Assert.assertThat(response.getNotFoundPartNumbers().isEmpty(), Matchers.is(true));
  }

  @Test
  public void testSearchArticlesByBuyersGuideWithContainNotFoundPartNumbersMap() {
    final String[] sample = new String[] { DEALER_ID,
        "721400_1|721500_2|111721400_1|1111721500_2" };
    ThuleArticleListSearchResponse response =
        service.searchArticlesByBuyersGuide(user, buildMap(sample));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));
    Assert.assertThat(response.getThuleArticles().isEmpty(), Matchers.is(false));
    Assert.assertThat(response.getNotFoundPartNumbers().isEmpty(), Matchers.is(false));
  }

  @Test
  public void testSearchArticlesByBuyersGuideWithNotFoundPartNumbersMap() {
    final String[] sample = new String[] { DEALER_ID, "111721400_1|1111721500_2" };
    ThuleArticleListSearchResponse response =
        service.searchArticlesByBuyersGuide(user, buildMap(sample));
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(response));
    Assert.assertThat(response.getThuleArticles().isEmpty(), Matchers.is(true));
    Assert.assertThat(response.getNotFoundPartNumbers().isEmpty(), Matchers.is(false));
  }

  private static Map<String, String> buildMap(String... strs) {
    if (ArrayUtils.isEmpty(strs) || KEYS_OF_MAP_BY_ORDER.length != strs.length) {
      return Collections.emptyMap();
    }
    Map<String, String> map = new HashMap<>();
    IntStream.range(0, strs.length)
    .forEach(index -> map.put(KEYS_OF_MAP_BY_ORDER[index], strs[index]));
    return map;
  }

  private static Map<String, String> buildSampleMap() {
    return buildMap(SAMPLE_BUYERS_GUIDE_DATA);
  }
}
