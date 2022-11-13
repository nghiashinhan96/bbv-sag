package com.sagag.services.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Exception class for margin article group validation.
 */
@Getter
public class WssMarginArticleGroupException extends ValidationException {

  private static final long serialVersionUID = -3044190048709185020L;

  public WssMarginArticleGroupException(WssMarginArticleGroupErrorCase error, String message) {
    super(message);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * Margin article group error cases enumeration definition.
   */
  public enum WssMarginArticleGroupErrorCase implements IBusinessCode {
    WMAGE_001("DUPLICATED_MARGIN_ARTICLE_GROUP"),
    WMAGE_002("NOT_EXISTED_MARGIN_ARTICLE_GROUP"),
    WMAGE_003("OBJECT_VALIDATOR"),
    WMAGE_004("INVALID_INDEX_ARTICLE_GROUP"),
    WMAGE_005("INVALID_ARTICLE_GROUP_UPDATE"),
    WMAGE_006("NO_DEFAULT_ARTICLE_GROUP_SETTING"),
    WMAGE_007("INVALID_ARTICLE_GROUP_DELETE"),
    WMAGE_008("EMPTY_ARTICLE_GROUP_CSV_IMPORT")
    ;

    private String key;

    WssMarginArticleGroupErrorCase(String key) {
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
