package com.sagag.services.service.order.history;

import com.sagag.eshop.service.api.UserService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.OrderExternalService;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.hazelcast.domain.order.OrderHistoryFilters;
import com.sagag.services.service.DataProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
public class DefaultOrderHistoryHandlerTest {

  @InjectMocks
  private DefaultOrderHistoryHandler handler;

  @Mock
  private UserService userService;

  @Mock
  private OrderExternalService orderExternalService;

  @Test
  public void testGetOrderHistoryFilterForNormalUser() {
    UserInfo userInfo = new UserInfo();
    userInfo.setRoles(Arrays.asList(EshopAuthority.NORMAL_USER.name()));
    OrderHistoryFilters response = handler.getFilters(userInfo);
    Assert.assertNotNull(response);
  }

  @Test
  public void testGetOrderHistoryFilterWithInvalidOrg() {
    OrderHistoryFilters response = handler.getFilters(DataProvider.createUserInfo());
    Assert.assertNotNull(response);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOrderHistoryFilterForSales() {
    UserInfo userInfo = new UserInfo();
    userInfo.setRoles(Arrays.asList(EshopAuthority.SALES_ASSISTANT.name()));
    handler.getFilters(userInfo);
  }
}
