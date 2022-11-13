package com.sagag.services.thule.extractor;

import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.thule.DataProvider;
import com.sagag.services.thule.domain.BuyersGuideData;
import com.sagag.services.thule.domain.BuyersGuideOrder;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
public class BuyersGuideDataExtractorTest {

  @InjectMocks
  private BuyersGuideDataExtractor extractor;

  @Test
  public void givenEmptyMapShouldReturnEmptyOptional() {
    testAndAssert(ArrayUtils.EMPTY_STRING_ARRAY, Matchers.is(false));
  }

  @Test
  public void givenEmptyDealerIdShouldReturnBuyersGuideData() {
    final String orderListData = "20201301_1|20201401_2";
    testAndAssert(new String[] { StringUtils.EMPTY, orderListData }, Matchers.is(true));
  }

  @Test
  public void givenEmptyOrderListShouldReturnBuyersGuideData() {
    final String orderListData = StringUtils.EMPTY;
    testAndAssert(new String[] { StringUtils.EMPTY, orderListData }, Matchers.is(true));
  }

  @Test
  public void givenGoodFormDataShouldReturnBuyersGuideData() {
    final Optional<BuyersGuideData> result = extractor.apply(DataProvider.buildSampleMap());
    assertWithGoodFormData(result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenFormDataContainsMissingValueShouldThrowException() {
    extractor.apply(DataProvider.buildSampleMapMissingQuantityAndMovexId());
  }

  private void assertWithGoodFormData(final Optional<BuyersGuideData> result) {
    Assert.assertThat(result.isPresent(), Matchers.is(true));

    BuyersGuideData buyersGuideData = result.get();
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(buyersGuideData));
    Assert.assertThat(buyersGuideData.getDealerId(), Matchers.is(DataProvider.EDITED_DEALER_ID));
    Assert.assertThat(buyersGuideData.getOrders(), Matchers.not(Matchers.empty()));
    for (BuyersGuideOrder orderItem : buyersGuideData.getOrders()) {
      Assert.assertThat(orderItem.getMovexId(), Matchers.not(Matchers.isEmptyOrNullString()));
      Assert.assertThat(orderItem.getQuantity(), Matchers.greaterThanOrEqualTo(0l));
    }
  }

  private void testAndAssert(String[] data, Matcher<Boolean> requiredMatcher) {
    final Optional<BuyersGuideData> result = extractor.apply(DataProvider.buildMap(data));
    Assert.assertThat(result.isPresent(), requiredMatcher);
    if (!result.isPresent()) {
      return;
    }
    BuyersGuideData buyersGuideData = result.get();
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(buyersGuideData));
    Assert.assertThat(buyersGuideData.getDealerId(), Matchers.is(data[0]));
    Assert.assertThat(buyersGuideData.getOrders(), Matchers.not(Matchers.empty()));
  }

}
