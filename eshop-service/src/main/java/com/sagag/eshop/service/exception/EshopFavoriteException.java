package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;
import lombok.Getter;

/**
 * Generic exception class for Eshop Favorite validation.
 */
@Getter
public class EshopFavoriteException extends ValidationException {

  private static final long serialVersionUID = 3210723865326280711L;

  public EshopFavoriteException(String msg) {
    super(msg);
  }

  public EshopFavoriteException(UnipartsFavoriteErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * Eshop Favorite error cases enumeration definition.
   */
  public enum UnipartsFavoriteErrorCase implements IBusinessCode {
    UNIF_DUB_001("FAVORITE_ITEM_LEAF_DUPLICATED"), UNIF_NEI_001(
        "FAVORITE_ITEM_LEAF_NOT_EXIST"), UNIF_DUB_002(
            "FAVORITE_ITEM_ARTICLE_DUPLICATED"), UNIF_NEI_002("FAVORITE_ITEM_ARTICLE_NOT_EXIST");
    ;

    private String key;

    UnipartsFavoriteErrorCase(String key) {
      this.key = key;
    }

    @Override
    public String code() {
      return this.name();
    }

    @Override
    public String key() {
      return this.key;
    }

  }
}
