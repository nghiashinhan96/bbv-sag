package com.sagag.services.ivds.api;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.article.oil.OilProduct;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.ivds.request.FreetextSearchRequest;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.BatteryArticleSearchRequest;
import com.sagag.services.ivds.request.filter.BulbArticleSearchRequest;
import com.sagag.services.ivds.request.filter.MotorTyreArticleSearchRequest;
import com.sagag.services.ivds.request.filter.OilArticleSearchRequest;
import com.sagag.services.ivds.request.filter.TyreArticleSearchRequest;
import com.sagag.services.ivds.response.CustomArticleResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ValidationException;

/**
 * Interface class for IVDS article service.
 */
public interface IvdsArticleService {

  /**
   * Returns list of articles searched by article number.
   *
   * @param user
   * @param articleNr
   * @param amountNumber the param to pass in the result outbound link to set default amount number
   *        in the article list result
   * @param size
   * @return the page of {@link ArticleDocDto}
   */
  Page<ArticleDocDto> searchArticlesByNumber(UserInfo user, String articleNr,
      int amountNumber, Pageable pageable, boolean isDeepLink);

  /**
   * Returns the articles from free text search.
   *
   * @param request the article free text search request
   * @return a list of {@link Page<ArticleDocDto>}
   */
  ArticleFilteringResponseDto searchFreetext(FreetextSearchRequest request);

  /**
   * Returns a list of articles by its pimIds.
   *
   * @param user    the user who requests
   * @param pimIds  the list umarids to search articles
   * @param vehicle the selected vehicle optional
   * @return the list of {@link ArticleDocDto}
   */
  Page<ArticleDocDto> searchArticlesByArticleIds(UserInfo user, Set<String> pimIds,
      Optional<VehicleDto> vehicle);

  /**
   * Returns a list of articles by its pimIds and fill oil product data.
   *
   * @param user    the user who requests
   * @param pimIds  the list umarids to search articles
   * @param vehicle the selected vehicle optional
   * @return the list of {@link ArticleDocDto}
   */
  Page<ArticleDocDto> searchAndFillOilProductArticlesByArticleIds(UserInfo user, Set<String> pimIds,
      Optional<VehicleDto> vehicle, List<OilProduct> oilProducts);

  /**
   * Returns a optional of articles by its pimIds.
   *
   * @param user    the user who requests
   * @param pimIds  the list umarids to search articles
   * @param vehicle the selected vehicle optional
   * @return the optional of {@link ArticleDocDto}
   */
  Optional<ArticleDocDto> searchArticleByArticleId(UserInfo user, String artId,
      Optional<VehicleDto> vehicle);

  /**
   * Returns the article from ERP which contains price for VIN license.
   *
   * @param user    the user who requests
   * @param licenseArticleId the vin article id
   * @param priceDisplayTypeEnum
   * @return the {@link ArticleDocDto}
   */
  ArticleDocDto searchVinArticle(UserInfo user, String licenseArticleId);

  /**
   * Returns the NET price from ERP which contains price for Coupon article ID.
   * @param user
   * @param couponArticleId the coupon article id
   * @return the optional of NET price value
   */
  Optional<Double> getCounponPrice(UserInfo user, String couponArticleId);

  /**
   * Returns the article list by scanned code
   *
   * @param code the code to search
   * @param user the user who requests
   * @return a list of {@link ArticleDocDto}
   * @throws ValidationException thrown when the code is invalid.
   */
  Page<ArticleDocDto> searchArticlesByBarCode(UserInfo user, String code);

  /**
   * Returns the tyre articles and its aggregation dropdown list from tyres search.
   *
   * @param user the current user who requests
   * @param request the tyre article search request
   * @param pageable the paging object
   * @return the {@link CustomArticleResponseDto}
   */
  CustomArticleResponseDto searchTyreArticlesByRequest(UserInfo user,
      TyreArticleSearchRequest request, Pageable pageable);

  /**
   * Returns the motor tyre articles and its aggregation dropdown list from motor tyres search.
   *
   * @param user the user who requests
   * @param request the motor tyre article search request
   * @param pageRequest the paging object
   * @return the {@link CustomArticleResponseDto}
   */
  CustomArticleResponseDto searchMotorTyreArticlesByRequest(UserInfo user,
      MotorTyreArticleSearchRequest request, PageRequest pageRequest);

  /**
   * Returns the bulb articles and its aggregation dropdown list from bulb search.
   *
   * @param user the user who requests
   * @param request the bulb article search request
   * @param pageable the paging object
   * @return the {@link CustomArticleResponseDto}
   */
  CustomArticleResponseDto searchBulbArticlesByRequest(UserInfo user,
      BulbArticleSearchRequest request, Pageable pageable);

  /**
   * Returns the battery articles and its aggregation dropdown list from battery search.
   *
   * @param user the user who requests
   * @param request the battery article search request
   * @param pageable the paging object
   * @return the {@link CustomArticleResponseDto}
   */
  CustomArticleResponseDto searchBatteryArticlesByRequest(UserInfo user,
      BatteryArticleSearchRequest request, Pageable pageable);

  /**
   * Returns the oil articles and its aggregation dropdown list from oil search.
   *
   * @param user the user who requests
   * @param request the oil article search request
   * @param pageable the paging object
   * @return the {@link CustomArticleResponseDto}
   */
  CustomArticleResponseDto searchOilArticlesByRequest(UserInfo user,
      OilArticleSearchRequest request, Pageable pageable);

  /**
   * Filters the articles.
   *
   * @param user who request
   * @param request request data
   * @param pageable paging information
   * @return Article filtering response
   */
  ArticleFilteringResponseDto searchArticlesByFilteringRequest(UserInfo user,
      ArticleFilterRequest request, Pageable pageable);

  /**
   * Returns articles by article ids.
   *
   * @param user the user who requests
   * @param artIds the article ids to search
   * @return a list of {@link ArticleDocDto}
   */
  Page<ArticleDocDto> searchArticleByArticleIds(UserInfo user, List<String> artIds);
}
