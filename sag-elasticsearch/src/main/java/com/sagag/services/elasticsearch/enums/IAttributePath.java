package com.sagag.services.elasticsearch.enums;

import com.sagag.services.common.contants.SagConstants;

import org.apache.commons.lang3.StringUtils;

/**
 * Attribute path interfacing providing the common attribute.
 */
public interface IAttributePath {

  /**
   * Returns the field value of the attribute.
   *
   * @return the field value
   */
  String field();

  /**
   * Returns the path value of the attribute.
   *
   * @return the path
   */
  String path();

  /**
   * Returns the aggregation terms.
   *
   * @return the aggregation terms
   */
  String aggTerms();

  /**
   * Returns the full qualified path of the field.
   *
   * @return the full qualified path
   */
  default String fullQField() {
    if (!isNested()) {
      return field();
    }
    return StringUtils.join(new String[] { path(), field() }, SagConstants.DOT);
  }

  default boolean isNested() {
    return !StringUtils.isBlank(path());
  }
}
