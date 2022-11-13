package com.sagag.services.ivds.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.oil.OilTypeIdsDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IvdsOilSearchService {

  /**
   * Returns TypeIds By Generic Article Ids.
   *
   * @param vehicleOpt the optional of found vehicle
   * @param oilGenericArticleIds
   * @param categoryIds
   * @return List of {@link OilTypeIdsDto}
   * @throw OlyslagerException throw if got any exceptions
   */
  List<OilTypeIdsDto> getOilTypesByVehicleId(Optional<VehicleDto> vehicleOpt,
      List<String> oilGenericArticleIds, List<String> categoryIds) throws ServiceException;

  /**
   * Returns recommendation By Type Id.
   *
   * @param user who request
   * @param oilIds type ids to search recommendation
   * @param oilGenericArticleIds ids of the oil generic article
   * @param vehicleOpt the optional of found vehicle
   * @param categoryIds
   * @return Page of ArticleDocDto
   * @throw ServiceException throw if got any exceptions
   */
  Page<ArticleDocDto> searchOilRecommendArticles(UserInfo user, List<String> oilIds,
      List<String> oilGenericArticleIds, Optional<VehicleDto> vehicleOpt, List<String> categoryIds)
          throws ServiceException;

  /**
   * Extracts the oil generic article ids from list of given gaids.
   *
   * @param gaIds
   * @return the list of oil gaids
   */
  List<String> extractOilGenericArticleIds(Collection<String> gaIds);

}
