package com.sagag.services.rest.controller.uniparts;

import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteRequestDto;
import com.sagag.services.rest.swagger.docs.ApiDesc;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Eshop Favorite controller class.
 */
@RestController
@RequestMapping("/favorite")
@Api(tags = "Favorite APIs")
public class FavoriteController {

  @Autowired
  private EshopFavoriteService favoriteBusinessService;

  @ApiOperation(value = ApiDesc.EshopFavorite.PROCESS_FAVORITE_ITEM_API_DESC,
      notes = ApiDesc.EshopFavorite.PROCESS_FAVORITE_ITEM_API_NOTE)
  @PostMapping(value = "/process-item", produces = MediaType.APPLICATION_JSON_VALUE)
  public void processFavoriteItem(final OAuth2Authentication authed,
      @RequestBody EshopFavoriteDto favoriteDto) throws ValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    favoriteBusinessService.processFavoriteItem(user, favoriteDto, favoriteDto.isAddItem());
  }

  @ApiOperation(value = ApiDesc.EshopFavorite.GET_LASTEST_FAVORITE_COMMENTS_API_DESC,
      notes = ApiDesc.EshopFavorite.GET_LASTEST_FAVORITE_COMMENTS_API_DESC)
  @GetMapping(value = "/latest-comment", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<String> getLatestFavoriteComments(final OAuth2Authentication authed) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return favoriteBusinessService.searchComments(StringUtils.EMPTY, user);
  }

  @ApiOperation(value = ApiDesc.EshopFavorite.GET_LASTEST_FAVORITE_COMMENTS_API_DESC,
      notes = ApiDesc.EshopFavorite.GET_LASTEST_FAVORITE_COMMENTS_API_DESC)
  @GetMapping(value = "/search/comment", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<String> getLatestFavoriteComments(final OAuth2Authentication authed,
      @RequestParam(value = "text", required = false) String text) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return favoriteBusinessService.searchComments(text, user);
  }

  @ApiOperation(value = ApiDesc.EshopFavorite.GET_FAVORITE_ITEM_INFO_BY_LIST_API_DESC,
      notes = ApiDesc.EshopFavorite.GET_FAVORITE_ITEM_INFO_BY_LIST_API_NOTE)
  @PostMapping(value = "/info-items", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<EshopFavoriteDto> getFavoriteItemsInfo(final OAuth2Authentication authed,
      @RequestBody List<EshopFavoriteDto> items) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return favoriteBusinessService.getInfoItemsByList(user, items);
  }

  @ApiOperation(value = ApiDesc.EshopFavorite.SEARCH_FAVORITE_ITEM_API_DESC,
      notes = ApiDesc.EshopFavorite.SEARCH_FAVORITE_ITEM_API_NOTE)
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<EshopFavoriteDto> getFavoriteItemList(final OAuth2Authentication authed,
      @RequestBody EshopFavoriteRequestDto requestDto) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return favoriteBusinessService.searchFavoriteItem(user, requestDto);
  }


  @ApiOperation(value = ApiDesc.EshopFavorite.UPDATE_FAVORITE_ITEM_API_DESC,
      notes = ApiDesc.EshopFavorite.UPDATE_FAVORITE_ITEM_API_NOTE)
  @PutMapping(value = "/item", produces = MediaType.APPLICATION_JSON_VALUE)
  public EshopFavoriteDto editFavoriteItemInfo(final OAuth2Authentication authed,
      @RequestBody final EshopFavoriteDto favoriteDto) throws ValidationException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return favoriteBusinessService.updateFavoriteItem(user, favoriteDto);
  }
}
