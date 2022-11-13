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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class LeafNodeFavoriteTypeValidator extends EshopFavoriteValidator {

  private Optional<EshopFavorite> favorite = Optional.empty();

  @Override
  public boolean validate(EshopFavoriteValidateCriteria criteria) throws ValidationException {
    Assert.notNull(criteria, "Criteria mus not be null");
    Assert.notNull(criteria.getUserId(), "UserId mus not be null");
    Assert.notNull(criteria.isAdd(), "Add Flag mus not be null");
    Assert.notNull(criteria.getFavoriteItem(), "Favorite Item validate mus not be null");

    EshopFavoriteDto item = criteria.getFavoriteItem();
    String itemName = new StringBuilder(EshopFavoriteType.LEAF_NODE.name())
        .append(SagConstants.COLON).append(item.getTreeId())
        .append(StringUtils.trim(SagConstants.HYPHEN_HAS_SPACE_DELIMITER)).append(item.getLeafId())
        .append(StringUtils.trim(SagConstants.HYPHEN_HAS_SPACE_DELIMITER))
        .append(StringUtils.defaultString(item.getGaId())).toString();

    Assert.hasText(item.getTreeId(), "The given Tree ID  must not be empty");
    Assert.hasText(item.getLeafId(), "The given Leaf ID  must not be empty");
    favorite = eshopFavoriteRepository().findFavoriteItemTypeLeaf(criteria.getUserId(),
      item.getTreeId(), item.getLeafId(), StringUtils.defaultString(item.getGaId()));


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
          EshopFavoriteException.UnipartsFavoriteErrorCase.UNIF_DUB_001,
          String.format("This favorite type leaf node: %s was existed", itemName));
    } else {
      throw new EshopFavoriteException(
          EshopFavoriteException.UnipartsFavoriteErrorCase.UNIF_NEI_001,
          String.format("This favorite type leaf node: %s was not existed", itemName));
    }
  }

  @Override
  public boolean supportMode(EshopFavoriteType favoriteType) {
    return EshopFavoriteType.LEAF_NODE == favoriteType;
  }

  @Override
  public Optional<EshopFavorite> getFavoriteItemValid() {
    return favorite;
  }
}
