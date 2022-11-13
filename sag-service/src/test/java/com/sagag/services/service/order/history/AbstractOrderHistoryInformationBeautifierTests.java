package com.sagag.services.service.order.history;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.hazelcast.domain.order.OrderInfoDto;
import com.sagag.services.hazelcast.domain.order.OrderItemDetailDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
public class AbstractOrderHistoryInformationBeautifierTests {

  private static final String EXPECTED_ORDER_INFO_JSON_PATH =
      "/json/orderhistory/oh_info.json";

  private OrderInfoDto expectedOrderInfo;

  @Before
  public void setup() throws IOException {
    log.debug("Initializing setup data for Base Test of Order History Info Beautifier");
    final String ohInfoJson = IOUtils.toString(getClass()
        .getResourceAsStream(EXPECTED_ORDER_INFO_JSON_PATH), StandardCharsets.UTF_8);
    this.expectedOrderInfo = OrderInfoDto.createOrderInfoDtoFromJson(ohInfoJson);

  }

  protected void assertOrderHistory(OrderInfoDto orderInfo) {
    log.debug("{}", SagJSONUtil.convertObjectToPrettyJson(orderInfo));
    Assert.assertThat(orderInfo, Matchers.notNullValue());

    final Optional<OrderItemDetailDto> orderItemOpt = orderInfo.getItems().stream().findFirst();
    final Optional<OrderItemDetailDto> expectedItemOpt =
        this.expectedOrderInfo.getItems().stream().findFirst();

    Assert.assertThat(orderItemOpt.isPresent(), Matchers.is(true));

    final Optional<VehicleDto> actualVehicleOpt = orderItemOpt.map(OrderItemDetailDto::getVehicle);
    Assert.assertThat(actualVehicleOpt.isPresent(), Matchers.is(true));

    final String expectedVehId = expectedItemOpt.map(OrderItemDetailDto::getVehicle)
        .map(VehicleDto::getVehId).orElse(StringUtils.EMPTY);
    Assert.assertThat(actualVehicleOpt.get().getVehId(), Matchers.is(expectedVehId));

    final Optional<ArticleDocDto> actualArticleOpt = orderItemOpt
        .map(OrderItemDetailDto::getArticle);
    final Optional<ArticleDocDto> expectedArticleOpt = expectedItemOpt
        .map(OrderItemDetailDto::getArticle);
    Assert.assertThat(actualArticleOpt.isPresent(), Matchers.is(expectedArticleOpt.isPresent()));

    final ArticleDocDto actualArticle = actualArticleOpt.get();
    final int actualSalesQuantityByVeh = actualArticle.getSalesQuantity();
    final int expectedSalesQuantityByVeh = expectedArticleOpt.get().getSalesQuantity();
    Assert.assertThat(actualSalesQuantityByVeh, Matchers.is(expectedSalesQuantityByVeh));

    final int actualAmountNumber = actualArticle.getAmountNumber();
    final int expectedAmountNumber = expectedSalesQuantityByVeh;
    Assert.assertThat(actualAmountNumber, Matchers.is(expectedAmountNumber));
  }
}
