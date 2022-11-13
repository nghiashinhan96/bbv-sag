package com.sagag.eshop.service.virtualuser.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.VinLoggingRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.repo.entity.VinLogging;
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
public class SwapVinLoggingToOriginalUserForVirtualUserHandlerImplTest {

  @InjectMocks
  private SwapVinLoggingToOriginalUserForVirtualUserHandlerImpl handler;

  @Mock
  private VinLoggingRepository vinLoggingRepo;

  @Test
  public void givenVVirtualUserListShouldSwapVinLogging() {
    executeTestCase(DataProvider.buildVirtualUserList(), 1);
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonSwapVinLogging() {
    executeTestCase(Collections.emptyList(), 0);
  }

  private void executeTestCase(List<VVirtualUser> users, int numberOfTimes) {
    final List<VinLogging> loggingList = Arrays.asList(new VinLogging());
    when(vinLoggingRepo.findByUserIds(anyList())).thenReturn(loggingList);
    when(vinLoggingRepo.saveAll(anyList())).thenReturn(loggingList);

    handler.accept(users);

    verify(vinLoggingRepo, times(numberOfTimes)).findByUserIds(anyList());
    verify(vinLoggingRepo, times(numberOfTimes)).saveAll(anyList());
  }

}
