package com.sagag.services.ax.request;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.sagag.services.article.api.request.ArticleRequest;
import com.sagag.services.article.api.request.BasketPositionRequest;
import com.sagag.services.common.utils.SagJSONUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AxAvailabilityRequestTest {

  @Test
  public void shouldSumQuantitiesIfDupplicateBasketPositions() {
    final long custNr = 1100005l;
    final Collection<BasketPositionRequest> baskets = createDummyBasketPositions();
    AxAvailabilityRequestBuilder builder =
        new AxAvailabilityRequestBuilder(String.valueOf(custNr), baskets);

    AxAvailabilityRequest request = builder.build();

    log.debug("result = {}", SagJSONUtil.convertObjectToPrettyJson(request));
    Assert.assertThat(request.getCustomerNr(), Matchers.equalTo(String.valueOf(custNr)));
    Assert.assertThat(request.getBasketPositions().size(), Matchers.equalTo(3));
  }

  private static Collection<BasketPositionRequest> createDummyBasketPositions() {
    final String articleId1 = "1001";
    final String articleId2 = "1002";
    final String articleId3 = "1003";
    return Arrays.asList(
        createBasketPostionRequest(articleId1, 10),
        createBasketPostionRequest(articleId1, 5),
        createBasketPostionRequest(articleId1, 4),
        createBasketPostionRequest(articleId2, 1),
        createBasketPostionRequest(articleId3, 9),
        createBasketPostionRequest(articleId2, 11),
        createBasketPostionRequest(articleId2, 12),
        createBasketPostionRequest(articleId2, 13),
        createBasketPostionRequest(articleId2, 14));
  }

  private static BasketPositionRequest createBasketPostionRequest(String articleId, int quantity) {
    ArticleRequest article = new ArticleRequest(articleId, quantity);
    return new BasketPositionRequest(Arrays.asList(article), Optional.empty());
  }
}
