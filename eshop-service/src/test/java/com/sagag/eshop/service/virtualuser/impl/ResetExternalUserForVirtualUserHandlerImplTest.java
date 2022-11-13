package com.sagag.eshop.service.virtualuser.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.DataProvider;
import com.sagag.eshop.service.api.ExternalUserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
public class ResetExternalUserForVirtualUserHandlerImplTest {

  @InjectMocks
  private ResetExternalUserForVirtualUserHandlerImpl handler;

  @Mock
  private ExternalUserService externalUserService;

  @Test
  public void givenVVirtualUserListShouldResetExternalAccounts() {
    executeTestCase(DataProvider.buildVirtualUserList(), 1);
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonResetExternalAccounts() {
    executeTestCase(Collections.emptyList(), 0);
  }

  private void executeTestCase(List<VVirtualUser> users, int numberOfTimes) {
    doNothing().when(externalUserService).releaseVirtualUsers(anyList());

    handler.accept(users);

    verify(externalUserService, times(numberOfTimes)).releaseVirtualUsers(anyList());
  }

}
