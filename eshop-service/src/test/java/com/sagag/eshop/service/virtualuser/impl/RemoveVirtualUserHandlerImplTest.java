package com.sagag.eshop.service.virtualuser.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.GroupUserRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
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
public class RemoveVirtualUserHandlerImplTest {

  @InjectMocks
  private RemoveVirtualUserHandlerImpl handler;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private UserSettingsRepository userSettingsRepo;

  @Mock
  private GroupUserRepository groupUserRepo;

  @Mock
  private LoginRepository loginRepo;

  @Test
  public void givenVVirtualUserListShouldRemoveVirtualUsers() {
    executeTestCase(DataProvider.buildVirtualUserList(), 1);
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonRemoveVirtualUsers() {
    executeTestCase(Collections.emptyList(), 0);
  }

  private void executeTestCase(List<VVirtualUser> users, int numberOfTimes) {
    doNothing().when(loginRepo).removeLoginByUserIds(anyList());
    doNothing().when(groupUserRepo).removeGroupUserByUserIds(anyList());
    doNothing().when(eshopUserRepo).removeEshopUsersByIds(anyList());
    doNothing().when(userSettingsRepo).removeSettingsByIds(anyList());

    handler.accept(users);

    verify(loginRepo, times(numberOfTimes)).removeLoginByUserIds(anyList());
    verify(groupUserRepo, times(numberOfTimes)).removeGroupUserByUserIds(anyList());
    verify(eshopUserRepo, times(numberOfTimes)).removeEshopUsersByIds(anyList());
    verify(userSettingsRepo, times(numberOfTimes)).removeSettingsByIds(anyList());
  }

}
