package com.sagag.services.service.api.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.api.UserArticleHistoryService;
import com.sagag.eshop.service.api.UserVehicleHistoryService;
import com.sagag.eshop.service.dto.OwnSettings;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.enums.UserHistoryFromSource;
import com.sagag.services.domain.eshop.common.EshopAuthority;
import com.sagag.services.domain.eshop.dto.ArticleHistoryDto;
import com.sagag.services.domain.eshop.dto.VehicleHistoryDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import com.sagag.services.domain.sag.external.Customer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class SearchHistoryServiceImplTest {

  @InjectMocks
  private SearchHistoryServiceImpl searchHistoryService;

  @Mock
  private UserVehicleHistoryService userVehHistoryService;

  @Mock
  private UserArticleHistoryService userArtHistoryService;

  @Mock
  private EshopFavoriteService eshopFavoriteService;

  @Test
  public void testGetLatestHistories() {
    UserInfo userInfo = initUserInfo();

    Page<VehicleHistoryDto> userVehHistories =
        new PageImpl<>(Lists.newArrayList(new VehicleHistoryDto()));
    when(userVehHistoryService.searchVehicleHistories(Mockito.any(), Mockito.any()))
        .thenReturn(userVehHistories);
    Page<ArticleHistoryDto> userArtHistories =
        new PageImpl<>(Lists.newArrayList(new ArticleHistoryDto()));
    when(userArtHistoryService.searchArticleHistories(Mockito.any(), Mockito.any()))
        .thenReturn(userArtHistories);
    Page<EshopFavoriteDto> unipartFavorite =
        new PageImpl<>(Lists.newArrayList(new EshopFavoriteDto()));
    when(eshopFavoriteService.getLatestFavoriteItemList(Mockito.any(), Mockito.any()))
        .thenReturn(unipartFavorite);

    searchHistoryService.getLatestHistories(userInfo, UserHistoryFromSource.C4C.name());

    verify(userVehHistoryService, times(1)).searchVehicleHistories(Mockito.any(), Mockito.any());
    verify(userArtHistoryService, times(1)).searchArticleHistories(Mockito.any(), Mockito.any());
    verify(eshopFavoriteService, times(1)).getLatestFavoriteItemList(Mockito.any(),
        Mockito.any());
  }

  private UserInfo initUserInfo() {
    final Long orgCode = 5132364L;
    UserInfo userInfo = new UserInfo();
    userInfo.setId(1l);
    OwnSettings ownSettings = new OwnSettings();
    userInfo.setCustomer(Customer.builder().nr(orgCode).build());
    userInfo.setSettings(ownSettings);
    userInfo.setRoles(Arrays.asList(EshopAuthority.FINAL_USER_ADMIN.name()));
    return userInfo;
  }

}
