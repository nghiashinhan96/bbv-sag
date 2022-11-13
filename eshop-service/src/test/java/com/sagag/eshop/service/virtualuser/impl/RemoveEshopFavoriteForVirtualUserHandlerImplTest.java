package com.sagag.eshop.service.virtualuser.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sagag.eshop.repo.api.EshopFavoriteRepository;
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
public class RemoveEshopFavoriteForVirtualUserHandlerImplTest {

  @InjectMocks
  private RemoveEshopFavoriteForVirtualUserHandlerImpl handler;

  @Mock
  private EshopFavoriteRepository eshopFavoriteRepo;

  @Test
  public void givenVVirtualUserListShouldRemoveEshopFavoriteItems() {
    executeTestCase(DataProvider.buildVirtualUserList(), 1);
  }

  @Test
  public void givenEmptyVVirtualUserListShouldNonRemoveEshopFavoriteItems() {
    executeTestCase(Collections.emptyList(), 0);
  }

  private void executeTestCase(List<VVirtualUser> users, int numberOfTimes) {
    doNothing().when(eshopFavoriteRepo).removeFavoriteItemsByUserIds(anyList());
    handler.accept(users);
    verify(eshopFavoriteRepo, times(numberOfTimes)).removeFavoriteItemsByUserIds(anyList());
  }

}
