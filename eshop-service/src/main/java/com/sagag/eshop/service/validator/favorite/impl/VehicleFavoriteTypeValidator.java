package com.sagag.eshop.service.validator.favorite.impl;

import com.sagag.eshop.repo.entity.EshopFavorite;
import com.sagag.eshop.repo.enums.EshopFavoriteType;
import com.sagag.eshop.service.exception.EshopFavoriteException;
import com.sagag.eshop.service.validator.criteria.EshopFavoriteValidateCriteria;
import com.sagag.eshop.service.validator.favorite.EshopFavoriteValidator;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

@Component
public class VehicleFavoriteTypeValidator extends EshopFavoriteValidator {

  private Optional<EshopFavorite> favorite = Optional.empty();

  @Override
  public boolean validate(EshopFavoriteValidateCriteria criteria) throws ValidationException {
    Assert.notNull(criteria, "Criteria mus not be null");
    Assert.notNull(criteria.getUserId(), "UserId mus not be null");
    Assert.notNull(criteria.isAdd(), "Add Flag mus not be null");
    Assert.notNull(criteria.getFavoriteItem(), "Favorite Item validate mus not be null");
    EshopFavoriteDto item = criteria.getFavoriteItem();

    String itemName = new StringBuilder(EshopFavoriteType.VEHICLE.name()).append(SagConstants.COLON)
        .append(item.getVehicleId()).toString();

    Assert.hasText(item.getVehicleId(), "The given vehicle ID  must not be empty");
    favorite = eshopFavoriteRepository()
      .findFavoriteItemTypeVehicle(criteria.getUserId(), item.getVehicleId());
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
          String.format("This favorite type vehicle: %s was existed", itemName));
    } else {
      throw new EshopFavoriteException(
          EshopFavoriteException.UnipartsFavoriteErrorCase.UNIF_NEI_002,
          String.format("This favorite type vehicle: %s was not existed", itemName));
    }
  }

  @Override
  public boolean supportMode(EshopFavoriteType favoriteType) {
    return EshopFavoriteType.VEHICLE == favoriteType;
  }

  @Override
  public Optional<EshopFavorite> getFavoriteItemValid() {
    return favorite;
  }

}
