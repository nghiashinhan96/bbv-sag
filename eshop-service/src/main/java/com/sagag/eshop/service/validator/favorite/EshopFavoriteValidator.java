package com.sagag.eshop.service.validator.favorite;

import com.sagag.eshop.repo.api.EshopFavoriteRepository;
import com.sagag.eshop.repo.entity.EshopFavorite;
import com.sagag.eshop.repo.enums.EshopFavoriteType;
import com.sagag.eshop.service.exception.EshopFavoriteException;
import com.sagag.eshop.service.validator.criteria.EshopFavoriteValidateCriteria;
import com.sagag.services.common.validator.IDataValidator;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Some functions to support eshop favorite type
 */
public abstract class EshopFavoriteValidator
    implements IDataValidator<EshopFavoriteValidateCriteria> {

  @Autowired
  private EshopFavoriteRepository favoriteRepository;

  public abstract boolean supportMode(EshopFavoriteType favoriteType);

  public abstract Optional<EshopFavorite> getFavoriteItemValid();

  public abstract void throwException(boolean isAdd, String itemName) throws EshopFavoriteException;

  public EshopFavoriteRepository eshopFavoriteRepository() {
    return favoriteRepository;
  }

}
