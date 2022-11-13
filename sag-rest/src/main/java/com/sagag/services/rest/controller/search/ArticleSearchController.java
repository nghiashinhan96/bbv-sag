package com.sagag.services.rest.controller.search;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.eshop.dto.VehicleUsageDto;
import com.sagag.services.elasticsearch.common.BulbConstants;
import com.sagag.services.elasticsearch.common.OilConstants;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.ivds.api.IvdsArticleInVehicleContextService;
import com.sagag.services.ivds.api.IvdsArticleInWSPContextService;
import com.sagag.services.ivds.api.IvdsArticleService;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.request.CategorySearchRequest;
import com.sagag.services.ivds.request.WSPSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.BatteryArticleSearchRequest;
import com.sagag.services.ivds.request.filter.BulbArticleSearchRequest;
import com.sagag.services.ivds.request.filter.MotorTyreArticleSearchRequest;
import com.sagag.services.ivds.request.filter.OilArticleSearchRequest;
import com.sagag.services.ivds.request.filter.TyreArticleSearchRequest;
import com.sagag.services.ivds.response.ArticleListSearchResponseDto;
import com.sagag.services.ivds.response.CustomArticleResponseDto;
import com.sagag.services.ivds.response.wsp.UniversalPartArticleSearchResponse;
import com.sagag.services.rest.authorization.annotation.IsAccessibleUrlPreAuthorization;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.request.SearchByNumberRequestBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller class to define APIs for article search.
 */
@RestController
@RequestMapping("/search")
@Api(tags = "Articles Search APIs")
@Slf4j
public class ArticleSearchController {

  @Autowired
  private IvdsArticleService ivdsArticleService;

  @Autowired
  private IvdsArticleInVehicleContextService ivdsArticleInVehicleContextService;

  @Autowired
  private IvdsArticleInWSPContextService articleInWSPContextService;

  @Autowired
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  /**
   * Returns the article list by number. The response is potentially a list of article.
   *
   * @param articleNr the article number should not be blank.
   * @param authed the authenticated user
   * @param body the request body
   * @return a list of {@link ArticleDocDto}
   */
  @ApiOperation(
      value = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_NUMBER_API_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_NUMBER_API_NOTE
      )
  @PostMapping(
      value = { "/articles", "/articles/{number:.+}" },
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<ArticleDocDto> searchArticlesByNumber(final OAuth2Authentication authed,
      @PathVariable(name = "number", required = false) final String articleNr,
      @RequestParam(name = "artNr", required = false) final String artNr,
      @RequestBody SearchByNumberRequestBody body) throws ResultNotFoundException {
    String searchText = StringUtils.defaultString(artNr, articleNr);
    Assert.hasText(searchText, "the article number should not be null or blank");
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Pageable pageable = PageUtils.defaultPageable(body.getOffset(), body.getSize());
    final Page<ArticleDocDto> articles = ivdsArticleService.searchArticlesByNumber(user, searchText,
        body.getAmountNumber(), pageable, body.isDeepLink());

    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
  }

  /**
   * Searches the articles by category ids and vehicle ids.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return {@link ArticleListSearchResponseDto}
   * @throws ServiceException
   */
  // @formatter:off
  @ApiOperation(
      value = ApiDesc.ArticleSearch.SEARCH_ARTICLES_BY_CATEGORIES_AND_VEHICLES_API_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_ARTICLES_BY_CATEGORIES_AND_VEHICLES_API_NOTE
      )
  @PostMapping(
      value = "/parts",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  public ArticleListSearchResponseDto searchArticlesByCateIdsAndVehIds(
      final OAuth2Authentication authed, @RequestBody CategorySearchRequest body)
          throws ServiceException {
    // @formatter:on
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ivdsArticleInVehicleContextService.searchArticlesInVehicleContext(user, body);
  }

  /**
   * Searches the articles by category ids and vehicle ids without calling to ERP.
   *
   * @param authed the authenticated user
   * @param body the request body
   * @return {@link ArticleListSearchResponseDto}
   * @throws ServiceException
   */
  // @formatter:off
  @ApiOperation(
      value = ApiDesc.ArticleSearch.SEARCH_ARTICLES_BY_CATEGORIES_AND_VEHICLES_API_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_ARTICLES_BY_CATEGORIES_AND_VEHICLES_API_NOTE
      )
  @PostMapping(
      value = "/v2/parts",
      produces = MediaType.APPLICATION_JSON_VALUE
      )
  public ArticleListSearchResponseDto searchArticlesByCateIdsAndVehIdsV2(
      final OAuth2Authentication authed, @RequestBody CategorySearchRequest body)
          throws ServiceException {
    // @formatter:on
    final UserInfo user = (UserInfo) authed.getPrincipal();
    body.setUsingVersion2(true);
    return ivdsArticleInVehicleContextService.searchArticlesInVehicleContext(user, body);
  }

  /**
   * Returns the article list by number. The response is potentially a list of article.
   *
   * @param artId the article number should not be blank.
   * @return a list of {@link ArticleDoc}
   */
  @ApiOperation(
      value = ApiDesc.ArticleSearch.SEARCH_VEHICLE_USAGE_BY_ARTID_API_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_VEHICLE_USAGE_BY_ARTID_API_NOTE
      )
  @GetMapping(value = { "/articles/usages", "/articles/{id:.+}/usages" },
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<VehicleUsageDto> searchArticleVehicleUsage(
      @PathVariable(name = "id", required = false) final String id,
      @RequestParam(name = "size", defaultValue = "10") final int size,
      @RequestParam(name = "artId", required = false) final String artId) {

    return ivdsArticleTaskExecutors
        .executeTaskWithVehicleUsages(StringUtils.defaultString(artId, id));
  }

  /**
   * Returns the tyre articles and its aggregation dropdown list from tyres search.
   *
   * @param searchRequest the tyre article search request
   * @param request the http request
   * @return the {@link CustomArticleResponseDto}
   */
  @ApiOperation(value = "Search tyre articles and aggregations",
      notes = "The service will search tyre articles and aggregations")
  @PostMapping(value = "/tyre", produces = MediaType.APPLICATION_JSON_VALUE)
  @IsAccessibleUrlPreAuthorization
  public CustomArticleResponseDto searchTyreArticlesBySearchRequest(
      final OAuth2Authentication authed, @RequestBody TyreArticleSearchRequest searchRequest,
      HttpServletRequest request) {
    log.debug("Search tyre articles from search request {}", searchRequest);
    // will get maximum limit result number if the size was not specified
    final int size =
        searchRequest.getSize() <= 0 ? SagConstants.MAX_PAGE_SIZE : searchRequest.getSize();
    final PageRequest pageRequest = PageUtils.defaultPageable(searchRequest.getOffset(), size);
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ivdsArticleService.searchTyreArticlesByRequest(user, searchRequest, pageRequest);
  }

  /**
   * Filters the articles.
   *
   * @param authed the authenticated user
   * @param request the request body
   * @return {@link ArticleFilteringResponseDto}
   */
  // @formatter:off
  @PostMapping(value = "/articles/filter", produces = MediaType.APPLICATION_JSON_VALUE)
  public ArticleFilteringResponseDto searchArticlesByFilter(final OAuth2Authentication authed,
      @RequestBody final ArticleFilterRequest request) {
    //@formatter:on
    Assert.hasText(request.getFilterMode(), "The given filter mode must not be empty");
    // will get maximum limit result number if the size was not specified
    final int size = request.getSize() <= 0 ? SagConstants.MAX_PAGE_SIZE : request.getSize();
    final PageRequest pageRequest = PageRequest.of(request.getOffset(), size);
    final UserInfo user = (UserInfo) authed.getPrincipal();
    request.setNeedSubAggregated(!Objects.equals(FilterMode.CROSS_REFERENCE.name(), request.getFilterMode()));
    return ivdsArticleService.searchArticlesByFilteringRequest(user, request, pageRequest);
  }

  /**
   * Returns the motorbike tyre articles and its aggregation dropdown list from tyres search.
   *
   * @param authed the user who requests
   * @param searchRequest the motorbike tyre article search request
   * @param request the http request
   * @return the {@link CustomArticleResponseDto}
   */
  @ApiOperation(value = "Search motorbike tyre articles and aggregations",
      notes = "The service will search motorbike tyre articles and aggregations")
  @PostMapping(value = "/tyre/motor", produces = MediaType.APPLICATION_JSON_VALUE)
  @IsAccessibleUrlPreAuthorization
  public CustomArticleResponseDto searchMotorTyreArticlesBySearchRequest(
      final OAuth2Authentication authed,
      @RequestBody final MotorTyreArticleSearchRequest searchRequest, HttpServletRequest request) {
    log.debug("Search motorbike tyre articles from search request {}", searchRequest);
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final int size =
        searchRequest.getSize() <= 0 ? SagConstants.MAX_PAGE_SIZE : searchRequest.getSize();
    final PageRequest pageRequest = PageRequest.of(searchRequest.getOffset(), size);
    return ivdsArticleService.searchMotorTyreArticlesByRequest(user, searchRequest, pageRequest);
  }

  /**
   * Returns the article list by bar code
   *
   * @param code the code should not be blank.
   * @param authed the authenticated user
   * @return the list of {@link ArticleDocDto} with pageable
   * @throws ResultNotFoundException
   */
  @ApiOperation(value = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_BAR_CODE_API_DESC,
      notes = ApiDesc.ArticleSearch.SEARCH_ARTICLE_BY_BAR_CODE_API_NOTE)
  @PostMapping(value = "/barcode", produces = MediaType.APPLICATION_JSON_VALUE)
  public Page<ArticleDocDto> searchArticlesByBarCode(final OAuth2Authentication authed,
      @RequestParam(name = "barCode", defaultValue = "") final String barCode)
      throws ResultNotFoundException {
    Assert.hasText(barCode, "The given bar code should not be null or blank");
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final Page<ArticleDocDto> articles = ivdsArticleService.searchArticlesByBarCode(user, barCode);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(articles);
  }

  /**
   * Returns the bulb articles and its aggregation dropdown list from articles search.
   *
   * @param request the bulb http request
   * @param authed the user who requests
   * @param bulbSearchRequest the bulb article search request
   * @return the {@link CustomArticleResponseDto}
   */
  @ApiOperation(value = "Search bulb articles and aggregations",
      notes = "The service will search bulb articles and aggregations")
  @PostMapping(value = "/bulbs", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @IsAccessibleUrlPreAuthorization
  public CustomArticleResponseDto searchBulbArticlesBySearchRequest(HttpServletRequest request,
      final OAuth2Authentication authed, @RequestBody BulbArticleSearchRequest bulbSearchRequest) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    return ivdsArticleService.searchBulbArticlesByRequest(user, bulbSearchRequest,
        PageUtils.defaultPageable(BulbConstants.ARTICLES_MAX_PAGE_SIZE));
  }

  /**
   * Returns the battery articles and its aggregation dropdown list from battery search.
   *
   * @param request the battery http request
   * @param authed the user who requests
   * @param batterySearchRequest the battery article search request
   * @return the {@link CustomArticleResponseDto}
   */
  @ApiOperation(value = "Search battery articles and aggregations",
      notes = "The service will search battery articles and aggregations")
  @PostMapping(value = "/battery", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @IsAccessibleUrlPreAuthorization
  public CustomArticleResponseDto searchBatteryArticlesBySearchRequest(
      final HttpServletRequest request, final OAuth2Authentication authed,
      @RequestBody BatteryArticleSearchRequest batterySearchRequest) {
    log.debug("Search battery articles from search request {}", batterySearchRequest);
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final PageRequest pageRequest = PageUtils.defaultPageable(SagConstants.MAX_PAGE_SIZE);
    return ivdsArticleService.searchBatteryArticlesByRequest(user, batterySearchRequest,
        pageRequest);
  }

  /**
   * Returns the oil articles and its aggregation dropdown list from articles search.
   *
   * @param request the oil http request
   * @param auth the user who requests
   * @param oilSearchRequest the oil article search request
   * @return the {@link CustomArticleResponseDto}
   */
  @ApiOperation(value = "Search oil articles and aggregations",
      notes = "The service will search oil articles and aggregations")
  @PostMapping(value = "/oil", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @IsAccessibleUrlPreAuthorization
  public CustomArticleResponseDto searchOilArticlesBySearchRequest(final HttpServletRequest request,
      final OAuth2Authentication auth,
      @RequestBody final OilArticleSearchRequest oilSearchRequest) {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    return ivdsArticleService.searchOilArticlesByRequest(user, oilSearchRequest,
        PageUtils.defaultPageable(OilConstants.ARTICLES_MAX_PAGE_SIZE));
  }

  @GetMapping(value = "/article/{id:.+}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ArticleDocDto searchArticleByArticleId(OAuth2Authentication auth,
      @PathVariable("id") String articleId) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) auth.getPrincipal();
    return RestExceptionUtils.doSafelyReturnOptionalRecord(
        ivdsArticleService.searchArticleByArticleId(user, articleId, Optional.empty()));
  }

  @ApiOperation(value = ApiDesc.ArticleSearchV2.SEARCH_ARTICLES_BY_UNIVERSAL_PART_LEAF_API_DESC,
      notes = ApiDesc.ArticleSearchV2.SEARCH_ARTICLES_BY_UNIVERSAL_PART_LEAF_API_NOTE)
  @PostMapping(value = "/v2/universal-parts/gen-arts", produces = MediaType.APPLICATION_JSON_VALUE)
  public UniversalPartArticleSearchResponse searchArticlesByUniversalPartTest(
      final OAuth2Authentication authed, @RequestBody WSPSearchRequest body) {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    final int size = body.getSize() <= 0 ? SagConstants.MAX_PAGE_SIZE : body.getSize();
    final PageRequest pageRequest = PageUtils.defaultPageable(body.getOffset(), size);
    return articleInWSPContextService.searchUniversalPartArticlesByRequest(user, body, pageRequest);
  }

}
