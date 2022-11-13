package com.sagag.services.ax.common.filter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.common.filter.ErpArticleIdFilter;
import com.sagag.services.common.profiles.WintProfile;

@Component
@WintProfile
public class StringErpArticleIdFilterImpl implements ErpArticleIdFilter {

  @Override
  public boolean test(String artId) {
    return !StringUtils.isBlank(artId);
  }

}
