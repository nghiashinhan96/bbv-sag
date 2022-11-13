package com.sagag.services.rest.controller.search;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.common.utils.RestExceptionUtils;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.rest.swagger.docs.ApiDesc;
import com.sagag.services.service.api.CategoryBusinessService;
import com.sagag.services.service.enums.CategoryType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Category controller class.
 */
@RestController
@RequestMapping("/categories")
@Api(tags = "Categories APIs")
public class CategoryController {

  private static final String NOT_FOUND_CATEGORIES_MSG =
      "Not found any categories for vehicle id is %s";

  @Autowired
  private CategoryBusinessService categoryBusService;

  @ApiOperation(value = ApiDesc.Category.ALL_CATEGORIES_VEHICLE_API_DESC,
      notes = ApiDesc.Category.ALL_CATEGORIES_VEHICLE_API_NOTE)
  @GetMapping(value = "/{vehicleId}/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<CategoryType, List<CategoryItem>> getAllCategoriesByVehicle(
      final OAuth2Authentication authed, @PathVariable("vehicleId") final String vehId,
      @RequestParam(value = "addCupiCode", defaultValue = "false") boolean addCupiCode)
      throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();

    final Map<CategoryType, List<CategoryItem>> categories = categoryBusService
        .getCategoriesByVehicleIdAndVehicleType(user, vehId, addCupiCode);

    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(categories,
        String.format(NOT_FOUND_CATEGORIES_MSG, vehId));
  }

  @ApiOperation(value = ApiDesc.Category.SEARCH_CATEGORIES_VEHICLE_API_DESC,
      notes = ApiDesc.Category.SEARCH_CATEGORIES_VEHICLE_API_NOTE)
  @GetMapping(value = "/{vehicleId}/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, String> searchCategoriesByText(final OAuth2Authentication authed,
      @PathVariable("vehicleId") final String vehId,
      @RequestParam(value = "text") final String text,
      @RequestParam(value = "addCupiCode", defaultValue = "false") boolean addCupiCode)
          throws ResultNotFoundException {

    final UserInfo user = (UserInfo) authed.getPrincipal();

    final Map<String, String> categories =
        categoryBusService.searchCategoriesByFreeText(user, vehId, addCupiCode, text);

    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(categories,
        String.format(NOT_FOUND_CATEGORIES_MSG, vehId));
  }

  @ApiOperation(value = ApiDesc.Category.QUICK_CLICK_CATEGORIES_VEHICLE_API_DESC,
      notes = ApiDesc.Category.QUICK_CLICK_CATEGORIES_VEHICLE_API_NOTE)
  @GetMapping(value = "/quick/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<List<CategoryItem>> getQuickClickCategories(final OAuth2Authentication authed,
      @PathVariable String vehicleId,
      @RequestParam(value = "addCupiCode", defaultValue = "false") boolean addCupiCode)
          throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();

    final List<List<CategoryItem>> categories =
        categoryBusService.getQuickClickCategoriesByVehicleId(user, vehicleId, addCupiCode);

    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(categories,
        String.format(NOT_FOUND_CATEGORIES_MSG, vehicleId));
  }

  @PostMapping(value = "/filter")
  public Map<String, List<ArticleFilterItem>> searchArticlesByFilter(
      final OAuth2Authentication authed, @RequestParam("gaId") final String gaId,
      @RequestBody final ArticleFilterRequest request) throws ResultNotFoundException {
    final UserInfo user = (UserInfo) authed.getPrincipal();
    request.setGaIds(Arrays.asList(gaId));
    request.setNeedSubAggregated(true);
    return RestExceptionUtils.doSafelyReturnNotEmptyRecords(
        categoryBusService.searchArticlesByFilter(user, request,
            PageUtils.defaultPageable(request.getOffset(), request.getSize())));
  }

}
