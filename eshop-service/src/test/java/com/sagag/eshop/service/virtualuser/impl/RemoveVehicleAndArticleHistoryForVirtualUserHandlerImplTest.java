package com.sagag.eshop.service.virtualuser.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sagag.eshop.repo.api.UserArticleHistoryRepository;
import com.sagag.eshop.repo.api.UserVehicleHistoryRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.DataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class RemoveVehicleAndArticleHistoryForVirtualUserHandlerImplTest {

  @InjectMocks
  private RemoveVehicleAndArticleHistoryForVirtualUserHandlerImpl handler;

  @Mock
  private UserVehicleHistoryRepository userVehicleHistoryRepo;

  @Mock
  private UserArticleHistoryRepository userArticleHistoryRepo;

  @Test
  public void givenVVirtualUserListShouldRemoveHistories() {
    executeTestCase(DataProvider.buildVirtualUserList(), 1);
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonRemoveHistories() {
    executeTestCase(Collections.emptyList(), 0);
  }

  private void executeTestCase(List<VVirtualUser> users, int numberOfTimes) {
    doNothing().when(userVehicleHistoryRepo).removeVehicleHistoryByUserIds(anyList());
    doNothing().when(userArticleHistoryRepo).removeArticleHistoryByUserIds(anyList());
    handler.accept(users);
    verify(userVehicleHistoryRepo, times(numberOfTimes)).removeVehicleHistoryByUserIds(anyList());
    verify(userArticleHistoryRepo, times(numberOfTimes)).removeArticleHistoryByUserIds(anyList());
  }

}
