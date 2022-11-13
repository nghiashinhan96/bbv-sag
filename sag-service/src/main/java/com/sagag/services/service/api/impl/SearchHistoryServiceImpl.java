package com.sagag.services.service.api.impl;

import com.sagag.eshop.repo.criteria.user_history.UserArticleHistorySearchCriteria;
import com.sagag.eshop.repo.criteria.user_history.UserVehicleHistorySearchCriteria;
import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.api.UserArticleHistoryService;
import com.sagag.eshop.service.api.UserVehicleHistoryService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.eshop.dto.ArticleHistoryDto;
import com.sagag.services.domain.eshop.dto.UserSearchHistoryDto;
import com.sagag.services.domain.eshop.dto.VehicleHistoryDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import com.sagag.services.service.api.SearchHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

  private static final int DEFAULT_ART_HISTORIES_SIZE = 8;

  private static final int DEFAULT_VEH_HISTORIES_SIZE = 5;

  private static final int DEFAULT_UNIPART_FAVORITE_SIZE = 5;

  @Autowired
  private UserVehicleHistoryService userVehHistoryService;

  @Autowired
  private UserArticleHistoryService userArtHistoryService;

  @Autowired
  private EshopFavoriteService eshopFavoriteService;

  @Override
  public UserSearchHistoryDto getLatestHistories(UserInfo userInfo, String fromSource) {
    Page<VehicleHistoryDto> userVehHistories =
        getDefaultUserVehSearchHistories(userInfo, fromSource);

    Page<ArticleHistoryDto> userArtHistories =
        getDefaultUserArtSearchHistories(userInfo, fromSource);

    Page<EshopFavoriteDto> userUnipartFavorite = eshopFavoriteService
        .getLatestFavoriteItemList(userInfo,
            PageUtils.defaultPageable(DEFAULT_UNIPART_FAVORITE_SIZE));

    return UserSearchHistoryDto.builder().vehHistories(userVehHistories.getContent())
        .artHistories(userArtHistories.getContent())
        .unipartFavotite(userUnipartFavorite.getContent()).build();
  }

  private Page<ArticleHistoryDto> getDefaultUserArtSearchHistories(UserInfo user,
      String fromSource) {
    UserArticleHistorySearchCriteria defaultUserArtHistoryCriteria =
        UserArticleHistorySearchCriteria.builder().userId(user.getOriginalUserId())
        .orgId(user.getUserOrganisationId()).filterMode(fromSource).orderDescBySelectDate(true)
        .build();

    return userArtHistoryService.searchArticleHistories(defaultUserArtHistoryCriteria,
        PageUtils.defaultPageable(DEFAULT_ART_HISTORIES_SIZE));
  }

  private Page<VehicleHistoryDto> getDefaultUserVehSearchHistories(UserInfo user,
      String fromSource) {
    UserVehicleHistorySearchCriteria defaultUserVehHistoryCriteria =
        UserVehicleHistorySearchCriteria.builder().userId(user.getOriginalUserId())
        .orgId(user.getUserOrganisationId()).filterMode(fromSource).orderDescBySelectDate(true)
        .build();

    return userVehHistoryService.searchVehicleHistories(defaultUserVehHistoryCriteria,
        PageUtils.defaultPageable(DEFAULT_VEH_HISTORIES_SIZE));
  }

}
