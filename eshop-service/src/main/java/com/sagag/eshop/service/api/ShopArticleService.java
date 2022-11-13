package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.criteria.offer.ShopArticleSearchCriteria;
import com.sagag.eshop.service.dto.offer.ShopArticleDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interface class of shop article.
 */
public interface ShopArticleService {

  /**
   * Returns the shop article details.
   *
   * @param id the shop article id
   * @return the optional of {@link ShopArticleDto}
   */
  Optional<ShopArticleDto> getShopArticleDetails(Long id);

  /**
   * Returns the page of shop articles.
   *
   * @param criteria the shop article search criteria
   * @param pageable
   * @return the page of {@link ShopArticleDto}
   */
  Page<ShopArticleDto> getShopArticlesByCriteria(ShopArticleSearchCriteria criteria,
      Pageable pageable);

  /**
   * Creates the new shop article.
   *
   * @param shopArticle
   * @return the result of {@link ShopArticleDto}
   */
  ShopArticleDto createNewShopArticle(ShopArticleDto shopArticle);

  /**
   * Edits the existing shop article.
   *
   * @param shopArticle
   * @return the result of {@link ShopArticleDto}
   */
  ShopArticleDto editShopArticle(ShopArticleDto shopArticle);

  /**
   * Removes the existing shop articles.
   *
   * @param modifiedUserId the current user id
   * @param shopArticleId the shop article id
   * @return the result of {@link ShopArticleDto}
   */
  ShopArticleDto removeShopArticle(Long modifiedUserId, Long shopArticleId);
}
