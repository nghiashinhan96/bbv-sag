package com.sagag.services.service.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ResultNotFoundException;
import com.sagag.services.domain.article.ArticleFilterItem;
import com.sagag.services.domain.eshop.category.CategoryItem;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.service.enums.CategoryType;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CategoryBusinessService {

  /**
   * Returns the all categories on the selected vehicle id.
   *
   * @param user
   * @param vehicleId the vehicle id.
   * @param vehicleType the vehicle type(car or motorbike).
   * @param addCupiCode
   * @return a map of categories.
   */
  Map<CategoryType, List<CategoryItem>> getCategoriesByVehicleIdAndVehicleType(UserInfo user,
      String vehicleId, boolean addCupiCode);

  /**
   * Returns vehicle quick click categories in the Elastic-search and cached them.
   *
   * @return a list of categories.
   */
  List<List<CategoryItem>> getQuickClickCategoriesByVehicleId(UserInfo user, String vehicleId,
      boolean addCupiCode);

  /**
   * Returns the searched category list on the selected vehicle id.
   *
   * @param vehId the vehicle id.
   * @param freetext the searched text.
   * @param authed the authenticated user
   * @return a list of category.
   * @throws ResultNotFoundException exception when no result found.
   */
  Map<String, String> searchCategoriesByFreeText(UserInfo user, String vehicleId,
      boolean addCupiCode, String freetext);

  /**
   * Returns the sub aggregation of selected gaid.
   *
   * <pre>
   * We call this API with selected gaid from new filtering of freetext mode.
   * #4518: [AT-AX] Refinement of the new filtering for article lists without vehicle context:
   * Merkmale and values: Backend
   * From Simon's proposal
   * </pre>
   */
  Map<String, List<ArticleFilterItem>> searchArticlesByFilter(UserInfo user,
      ArticleFilterRequest request, Pageable pageable);
}
