package com.sagag.services.tools.utils;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utilities for any queries
 */
@UtilityClass
public final class QueryUtils {

  public String addElasticsearchLikeString(final String str) {
    if (StringUtils.isBlank(str)) {
      return StringUtils.EMPTY;
    }
    return new StringBuilder().append(ToolConstants.ELASTICSEARCH_LIKE_CHARACTER)
      .append(str).append(ToolConstants.ELASTICSEARCH_LIKE_CHARACTER).toString();
  }

  public List<String> getDistinctTrimedValues(List<String> custNrs) {
    return ListUtils.emptyIfNull(custNrs)
        .stream().filter(StringUtils::isNotBlank)
        .map(StringUtils::trim)
        .distinct().collect(Collectors.toList());
  }
}
