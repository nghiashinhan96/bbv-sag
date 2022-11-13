package com.sagag.services.rest.controller.offer;

import com.sagag.eshop.repo.criteria.offer.ShopArticleSearchCriteria;
import com.sagag.eshop.service.api.ShopArticleService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.ShopArticleDto;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.rest.resource.offer.ShopArticlePageResource;
import com.sagag.services.rest.resource.offer.ShopArticleResource;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.request.offer.ShopArticleRequestBody;
import com.sagag.services.service.request.offer.ShopArticleSearchRequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller class for Shop articles APIs.
 */
@RestController
@RequestMapping("/offer/shop")
@Api(tags = "Offer Shop Article APIs")
public class ShopArticleController {

  @Autowired
  private ShopArticleService shopArticleService;

  /**
   * Returns the page of shop articles.
   *
   * @param auth
   * @param body
   * @return the resource object of {@link ShopArticlePageResource}
   */
  @ApiOperation(value = "Searchs offer shop articles", response = ShopArticlePageResource.class)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST),
      @ApiResponse(code = ApiDesc.Code.NOT_FOUND, message = ApiDesc.Message.NOT_FOUND)
  })
  @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShopArticlePageResource searchShopArticles(final OAuth2Authentication auth,
      HttpServletRequest request, @RequestBody final ShopArticleSearchRequestBody body,
      @PageableDefault final Pageable pageable) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final ShopArticleSearchCriteria criteria =
        SagBeanUtils.map(body, ShopArticleSearchCriteria.class);
    criteria.setOrganisationId(user.getOrganisationId());

    final Page<ShopArticleDto> shopArticles =
        shopArticleService.getShopArticlesByCriteria(criteria, pageable);
    return ShopArticlePageResource.of(RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        shopArticles, "Not found any shop articles"));
  }

  /**
   * Creates new shop articles.
   *
   * @param auth
   * @param body
   * @return the resource object of {@link ShopArticleResource}
   */
  @ApiOperation(value = "Creates new shop articles", response = ShopArticleResource.class)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST)
  })
  @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShopArticleResource createNewShopArticle(final OAuth2Authentication auth,
      HttpServletRequest request, @RequestBody final ShopArticleRequestBody body) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final ShopArticleDto shopArticle = SagBeanUtils.map(body, ShopArticleDto.class);
    shopArticle.setCreatedUserId(user.getId());
    shopArticle.setOrganisationId(user.getOrganisationId());
    return ShopArticleResource.of(shopArticleService.createNewShopArticle(shopArticle));
  }

  /**
   * Edits existing shop articles.
   *
   * @param auth
   * @param shopArticleId
   * @return the resource object of {@link ShopArticleResource}
   */
  @ApiOperation(value = "Edits existing shop articles", response = ShopArticleResource.class)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST)
  })
  @PutMapping(value = "/edit/{shopArticleId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShopArticleResource editShopArticle(final OAuth2Authentication auth,
      HttpServletRequest request, @PathVariable final Long shopArticleId,
      @RequestBody final ShopArticleRequestBody body) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    final ShopArticleDto shopArticle = SagBeanUtils.map(body, ShopArticleDto.class);
    shopArticle.setId(shopArticleId);
    shopArticle.setModifiedUserId(user.getId());
    return ShopArticleResource.of(shopArticleService.editShopArticle(shopArticle));
  }

  /**
   * Removes existing shop articles.
   *
   * @param auth
   * @param shopArticleId
   * @return the resource object of {@link ShopArticleResource}
   */
  @ApiOperation(value = "Removes existing shop articles", response = ShopArticleResource.class)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST)
  })
  @DeleteMapping(value = "/remove/{shopArticleId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShopArticleResource removeShopArticle(final OAuth2Authentication auth,
      HttpServletRequest request, @PathVariable final Long shopArticleId) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    return ShopArticleResource.of(shopArticleService.removeShopArticle(user.getId(), shopArticleId));
  }

  /**
   * Returns the shop article details by id.
   *
   * @param auth
   * @param shopArticleId
   * @return the resource object of {@link ShopArticleResource}
   */
  @ApiOperation(value = "Get shop articles details", response = ShopArticleResource.class)
  @ApiResponses(value = {
      @ApiResponse(code = ApiDesc.Code.OK, message = ApiDesc.Message.SUCCESSFUL),
      @ApiResponse(code = ApiDesc.Code.BAD_REQUEST, message = ApiDesc.Message.BAD_REQUEST),
      @ApiResponse(code = ApiDesc.Code.NOT_FOUND, message = ApiDesc.Message.NOT_FOUND)
  })
  @GetMapping(value = "/{shopArticleId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ShopArticleResource getShopArticleDetails(final OAuth2Authentication auth,
      HttpServletRequest request, @PathVariable final Long shopArticleId)
          throws ResultNotFoundException {

    final Optional<ShopArticleDto> shopArticleOpt =
        shopArticleService.getShopArticleDetails(shopArticleId);
    return ShopArticleResource.of(RestExceptionUtils.doSafelyReturnOptionalRecord(
        shopArticleOpt, String.format("Not found any shop article with id = %s", shopArticleId)));
  }

}
