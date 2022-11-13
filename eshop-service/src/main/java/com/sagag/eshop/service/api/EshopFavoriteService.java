package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EshopFavoriteService {

  /**
   * Gets a favorite items
   *
   * @param userInfo the user info
   */
  List<EshopFavoriteDto> getFavoriteItemList(UserInfo userInfo);

  /**
   * Processes a favorite item
   *
   * @param userInfo the user info
   * @param favoriteItem the favorite item to create
   */
  void processFavoriteItem(UserInfo userInfo, EshopFavoriteDto favoriteItem, Boolean isAdd)
      throws ValidationException;

  /**
   * Update a favorite item
   *
   * @param userInfo the user info
   * @param favoriteItem the favorite item to update
   */
  EshopFavoriteDto updateFavoriteItem(UserInfo userInfo, EshopFavoriteDto favoriteItem)
      throws ValidationException;

  /**
   * Get latest user's unipart favorite item by page
   *
   * @param userInfo current login user
   * @param pageable page setting
   * @return Page of {@link EshopFavoriteDto}
   */
  Page<EshopFavoriteDto> getLatestFavoriteItemList(UserInfo userInfo, Pageable pageable);

  /**
   * Gets Favorite Items info by list
   *
   * @param userInfo current login user
   * @param items need to get info
   * @return Page of {@link EshopFavoriteDto}
   */
  List<EshopFavoriteDto> getInfoItemsByList(UserInfo userInfo, List<EshopFavoriteDto> items);

  /**
   * Finds favorite items
   *
   * @param userInfo current login user
   * @param requestDto page setting
   * @return Page of {@link EshopFavoriteDto}
   */
  Page<EshopFavoriteDto> searchFavoriteItem(UserInfo userInfo, EshopFavoriteRequestDto requestDto);

  /**
   * Updates favorite flag for article list
   *
   * @param userInfo the user info
   * @param articles the article list
   */
  void updateFavoriteFlagArticles(UserInfo userInfo, List<ArticleDocDto> articles);

  /**
   * Updates favorite flag for vehicle list
   *
   * @param userInfo the user info
   * @param vehicles the vehicle list
   */
  void updateFavoriteFlagVehicle(UserInfo userInfo, List<VehicleDto> vehicles);

  /**
   * Finds favorite comment
   *
   * @param userInfo current login user
   * @param requestDto page setting
   * @return List of {@link EshopFavoriteDto}
   */
  List<String> searchComments(String text, UserInfo userInfo);

}

