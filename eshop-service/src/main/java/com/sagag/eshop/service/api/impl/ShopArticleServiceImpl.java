package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.offer.ShopArticleRepository;
import com.sagag.eshop.repo.criteria.offer.ShopArticleSearchCriteria;
import com.sagag.eshop.repo.entity.offer.ShopArticle;
import com.sagag.eshop.repo.specification.offer.ShopArticleSpecifications;
import com.sagag.eshop.service.api.ShopArticleService;
import com.sagag.eshop.service.converter.ShopArticleConverters;
import com.sagag.eshop.service.dto.offer.ShopArticleDto;
import com.sagag.services.common.enums.offer.OfferConstants;
import com.sagag.services.common.enums.offer.OfferTecStateType;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation class of shop article.
 */
@Service
@Slf4j
public class ShopArticleServiceImpl implements ShopArticleService {

  /* Validation messages */
  private static final String SHOP_ARTICLE_MUST_NOT_BE_NULL_MSG =
      "The given request must not be null";

  private static final String SHOP_ARTICLE_TYPE_MUST_NOT_BE_EMPTY_MSG =
      "The given shop article type must not be empty";

  private static final String SHOP_ARTICLE_ID_MUST_NOT_BE_NULL_MSG =
      "The shop article id must not be null";

  private static final String MODIFIED_USER_ID_MUST_NOT_BE_NULL_MSG =
      "The modified user id must not be null";

  private static final String NOT_FOUND_SHOP_ARTICLE_MSG =
      "Not found any shop article has id is %s";

  private static final String SHOP_ARTICLE_SEARCH_CRITERIA_MUST_NOT_NULL_MSG =
      "The given search criteria must not be null";

  private static final String ORG_ID_MUST_NOT_BE_NULL_MSG =
      "The given organisation id must not be null";

  private static final String USER_ID_MUST_NOT_BE_NULL_MSG =
      "The current user id must not be null";

  private static final String SHOP_ARTICLE_NUMBER_MUST_NOT_BE_EMPTY_MSG =
      "The given article number must not be empty";

  @Autowired
  private ShopArticleRepository shopArticleRepo;

  @Override
  public Optional<ShopArticleDto> getShopArticleDetails(Long id) {
    log.debug("Get shop article details of id = {}", id);

    Assert.notNull(id, SHOP_ARTICLE_ID_MUST_NOT_BE_NULL_MSG);
    ShopArticle entity = shopArticleRepo.findById(id).orElse(null);
    if (Objects.isNull(entity)) {
      return Optional.empty();
    }
    return Optional.of(entity).map(ShopArticleConverters.optionalShopArticleConverter());
  }

  @Override
  @Transactional
  public Page<ShopArticleDto> getShopArticlesByCriteria(ShopArticleSearchCriteria criteria,
      Pageable pageable) {
    log.debug("Get shop articles by criteria = {}, pageable = {}", criteria, pageable);

    // Validate required fields to build query
    Assert.notNull(criteria, SHOP_ARTICLE_SEARCH_CRITERIA_MUST_NOT_NULL_MSG);
    Assert.notNull(criteria.getOrganisationId(), ORG_ID_MUST_NOT_BE_NULL_MSG);
    Assert.hasText(criteria.getType(), SHOP_ARTICLE_TYPE_MUST_NOT_BE_EMPTY_MSG);

    Specification<ShopArticle> spec =
        ShopArticleSpecifications.searchShopArticlesByCriteria(criteria);

    return shopArticleRepo.findAll(spec, pageable)
        .map(ShopArticleConverters.optionalShopArticleConverter());
  }

  @Override
  @Transactional
  public ShopArticleDto createNewShopArticle(ShopArticleDto shopArticle) {
    log.debug("Create new shop article = {}", shopArticle);

    Assert.notNull(shopArticle, SHOP_ARTICLE_MUST_NOT_BE_NULL_MSG);
    Assert.notNull(shopArticle.getCreatedUserId(), USER_ID_MUST_NOT_BE_NULL_MSG);
    Assert.notNull(shopArticle.getOrganisationId(), ORG_ID_MUST_NOT_BE_NULL_MSG);
    Assert.hasText(shopArticle.getArticleNumber(), SHOP_ARTICLE_NUMBER_MUST_NOT_BE_EMPTY_MSG);
    Assert.notNull(shopArticle.getType(), SHOP_ARTICLE_TYPE_MUST_NOT_BE_EMPTY_MSG);

    // Create shop article
    ShopArticle entity = new ShopArticle();
    entity.setArticleNumber(shopArticle.getArticleNumber());
    entity.setName(shopArticle.getName());
    entity.setDescription(shopArticle.getDescription());
    entity.setAmount(shopArticle.getAmount());
    entity.setPrice(shopArticle.getPrice());
    entity.setType(shopArticle.getType().name());
    entity.setTecstate(OfferTecStateType.ACTIVE.name());
    entity.setVersion(OfferConstants.FIRST_VERSION);

    // Update organisation, created user id in first
    Date now = Calendar.getInstance().getTime();
    Long createdUserId = shopArticle.getCreatedUserId();
    entity.setCreatedUserId(createdUserId);
    entity.setCreatedDate(now);
    entity.setOrganisationId(shopArticle.getOrganisationId());
    entity = shopArticleRepo.save(entity);

    return ShopArticleConverters.convert(entity);
  }

  @Override
  @Transactional
  public ShopArticleDto editShopArticle(ShopArticleDto shopArticle) {
    log.debug("Edit the existing shop article = {}", shopArticle);

    Assert.notNull(shopArticle, SHOP_ARTICLE_MUST_NOT_BE_NULL_MSG);
    Assert.notNull(shopArticle.getId(), SHOP_ARTICLE_ID_MUST_NOT_BE_NULL_MSG);

    // Edit shop article
    final ShopArticle entity = shopArticleRepo.findById(shopArticle.getId()).orElse(null);
    if (entity == null) {
      String message = String.format(NOT_FOUND_SHOP_ARTICLE_MSG, shopArticle.getId());
      throw new NoSuchElementException(message);
    }
    entity.setArticleNumber(shopArticle.getArticleNumber());
    entity.setName(shopArticle.getName());
    entity.setDescription(shopArticle.getDescription());
    entity.setAmount(shopArticle.getAmount());
    entity.setPrice(shopArticle.getPrice());
    editCommonShopArticleInfo(entity, shopArticle.getModifiedUserId());

    return ShopArticleConverters.convert(shopArticleRepo.save(entity));
  }

  @Override
  @Transactional
  public ShopArticleDto removeShopArticle(Long modifiedUserId, Long shopArticleId) {
    log.debug("Remove the existing shop article id = {} by modified user id = ",
        shopArticleId, modifiedUserId);

    Assert.notNull(shopArticleId, SHOP_ARTICLE_ID_MUST_NOT_BE_NULL_MSG);
    Assert.notNull(modifiedUserId, MODIFIED_USER_ID_MUST_NOT_BE_NULL_MSG);

    // Remove shop article by id
    final ShopArticle entity = shopArticleRepo.findById(shopArticleId).orElse(null);
    if (entity == null) {
      String message = String.format(NOT_FOUND_SHOP_ARTICLE_MSG, shopArticleId);
      throw new NoSuchElementException(message);
    }
    entity.setTecstate(OfferTecStateType.INACTIVE.name());
    editCommonShopArticleInfo(entity, modifiedUserId);

    return ShopArticleConverters.convert(shopArticleRepo.save(entity));
  }

  /**
   * Edits some common shop article info.
   *
   * @param entity the destination entity to edit.
   * @param modifiedUserId the modified user id
   */
  private static void editCommonShopArticleInfo(ShopArticle entity, Long modifiedUserId) {
    entity.setModifiedUserId(modifiedUserId);
    entity.setModifiedDate(Calendar.getInstance().getTime());
    entity.setVersion(entity.getVersion() + NumberUtils.INTEGER_ONE);
  }

}
