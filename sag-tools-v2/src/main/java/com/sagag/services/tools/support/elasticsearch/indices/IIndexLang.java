package com.sagag.services.tools.support.elasticsearch.indices;

import org.apache.commons.lang3.StringUtils;

public interface IIndexLang {

  String index();

  String lang();

  default String lang(String value) {
    return StringUtils.lowerCase(value);
  }

}
