package com.sagag.services.ax.common.filter.impl;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.common.filter.ErpArticleIdFilter;
import com.sagag.services.common.profiles.DynamicAxProfile;

@Component
@DynamicAxProfile
public class NumberErpArticleIdFilterImpl implements ErpArticleIdFilter {

  @Override
  public boolean test(String artId) {
    return NumberUtils.isDigits(artId);
  }

}
