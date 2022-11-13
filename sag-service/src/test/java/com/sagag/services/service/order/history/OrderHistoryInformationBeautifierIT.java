package com.sagag.services.service.order.history;

import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.api.OrderHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.hazelcast.api.impl.GenArtCacheServiceImpl;
import com.sagag.services.hazelcast.domain.order.OrderInfoDto;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.SagServiceApplication;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
@Slf4j
public class OrderHistoryInformationBeautifierIT
  extends AbstractOrderHistoryInformationBeautifierTests {

  private static final long ORDER_ID = 10455l;

  @Autowired
  private OrderHistoryInformationBeautifier beautifier;

  @Autowired
  private OrderHistoryService orderHistoryService;

  @Autowired
  private GenArtCacheServiceImpl genArtCacheService;

  private UserInfo user;

  @Override
  @Before
  public void setup() throws IOException {
    super.setup();

    log.debug("Initializing setup data for IT");
    LocaleContextHolder.setLocale(Locale.GERMAN);
    genArtCacheService.refreshCacheAll();
    this.user = DataProvider.createUserInfo();
  }

  @Test
  public void givenOrderIdShouldBeautifySalesQuantityByFitment() {
    final long orderId = ORDER_ID;
    final OrderInfoDto orderInfo = getOrderInfoDetail(orderId, false);
    assertOrderHistory(orderInfo);
  }

  @Test
  public void givenOrderIdShouldBeautifyErpArticle() {
    final long orderId = ORDER_ID;
    final OrderInfoDto orderInfo = getOrderInfoDetail(orderId, true);
    assertOrderHistory(orderInfo);
  }

  private OrderInfoDto getOrderInfoDetail(long orderId, boolean updateErp) {
    Optional<OrderHistory> orderHistory = orderHistoryService.getOrderDetail(orderId);

    if (!orderHistory.isPresent()) {
      return null;
    }
    final OrderInfoDto orderInfo =
        OrderInfoDto.createOrderInfoDtoFromJson(orderHistory.get().getOrderInfoJson());
    beautifier.beautify(user, orderInfo, updateErp);
    return orderInfo;
  }

}
