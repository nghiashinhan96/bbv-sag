package com.sagag.eshop.service.virtualuser.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.order.OrderHistoryRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.repo.entity.order.OrderHistory;
import com.sagag.eshop.service.DataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class SwapOrderHistoryToOriginalUserForVirtualUserHandlerImplTest {

  @InjectMocks
  private SwapOrderHistoryToOriginalUserForVirtualUserHandlerImpl handler;

  @Mock
  private OrderHistoryRepository orderHistoryRepo;

  @Test
  public void givenVVirtualUserListShouldSwapOrderHistories() {
    executeTestCase(DataProvider.buildVirtualUserList(), 1);
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonSwapOrderHistories() {
    executeTestCase(Collections.emptyList(), 0);
  }

  private void executeTestCase(List<VVirtualUser> users, int numberOfTimes) {
    final List<OrderHistory> orders = Arrays.asList(new OrderHistory());
    when(orderHistoryRepo.findByUserIds(anyList())).thenReturn(orders);
    when(orderHistoryRepo.saveAll(anyList())).thenReturn(orders);

    handler.accept(users);

    verify(orderHistoryRepo, times(numberOfTimes)).findByUserIds(anyList());
    verify(orderHistoryRepo, times(numberOfTimes)).saveAll(anyList());
  }

}
