package com.sagag.services.article.api;

import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;

import java.util.List;
import java.util.Optional;

public interface SoapArticleErpExternalService {

  /**
   * Searches prices and availabilities for articles.
   *
   * @param username
   * @param customerId
   * @param securityToken
   * @param language
   * @param articles
   * @param vatRate
   * @param additional
   * @return the list of updated <code>ArticleDocDto</code>
   */
  List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username, String customerId,
      String securityToken, String language, List<ArticleDocDto> articles, double vatRate,
      AdditionalSearchCriteria additional);

  /**
   * Searches prices and availabilities for articles and vehicle.
   *
   * @param username
   * @param customerId
   * @param securityToken
   * @param language
   * @param articles
   * @param vatRate
   * @param vehicleOpt
   * @return the list of updated <code>ArticleDocDto</code>
   */
  List<ArticleDocDto> searchArticlePricesAndAvailabilities(String username, String customerId,
      String securityToken, String language, List<ArticleDocDto> articles, double vatRate,
      Optional<VehicleDto> vehicleOpt);

  /**
   * Searches availabilities details for articles and vehicle.
   *
   * @param username
   * @param customerId
   * @param securityToken
   * @param language
   * @param articles
   * @param vatRate
   * @param vehicleOpt
   * @return the list of updated <code>ArticleDocDto</code>
   */
  List<ArticleDocDto> searchArticleAvailabilitiesDetails(String username, String customerId,
      String securityToken, String language, List<ArticleDocDto> articles, double vatRate,
      Optional<VehicleDto> vehicleOpt);
}
