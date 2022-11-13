package com.sagag.eshop.service.validator.favorite.impl;

import com.sagag.eshop.repo.entity.EshopFavorite;
import com.sagag.eshop.repo.enums.EshopFavoriteType;
import com.sagag.eshop.service.exception.EshopFavoriteException;
import com.sagag.eshop.service.validator.criteria.EshopFavoriteValidateCriteria;
import com.sagag.eshop.service.validator.favorite.EshopFavoriteValidator;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ArticleFavoriteTypeValidator extends EshopFavoriteValidator {

  private Optional<EshopFavorite> favorite = Optional.empty();

  @Override
  public boolean validate(EshopFavoriteValidateCriteria criteria) throws ValidationException {
    Assert.notNull(criteria, "Criteria mus not be null");
    Assert.notNull(criteria.getUserId(), "UserId mus not be null");
    Assert.notNull(criteria.isAdd(), "Add Flag mus not be null");
    Assert.notNull(criteria.getFavoriteItem(), "Favorite Item validate mus not be null");
    EshopFavoriteDto item = criteria.getFavoriteItem();

    String itemName = new StringBuilder(EshopFavoriteType.ARTICLE.name()).append(SagConstants.COLON)
        .append(item.getArticleId()).toString();

    Assert.hasText(item.getArticleId(), "The given article ID  must not be empty");
    favorite = eshopFavoriteRepository()
      .findFavoriteItemTypeArticle(criteria.getUserId(), item.getArticleId());
    if ((criteria.isAdd() && favorite.isPresent())
        || (!criteria.isAdd() && !favorite.isPresent())) {
      throwException(criteria.isAdd(), itemName);
    }
    return true;
  }

  @Override
  public void throwException(boolean isAdd, String itemName) throws EshopFavoriteException {
    if (isAdd) {
      throw new EshopFavoriteException(
          EshopFavoriteException.UnipartsFavoriteErrorCase.UNIF_DUB_002,
          String.format("This favorite type article: %s was existed", itemName));
    } else {
      throw new EshopFavoriteException(
          EshopFavoriteException.UnipartsFavoriteErrorCase.UNIF_NEI_002,
          String.format("This favorite type article: %s was not existed", itemName));
    }
  }

  @Override
  public boolean supportMode(EshopFavoriteType favoriteType) {
    return EshopFavoriteType.ARTICLE == favoriteType;
  }

  @Override
  public Optional<EshopFavorite> getFavoriteItemValid() {
    return favorite;
  }

}
