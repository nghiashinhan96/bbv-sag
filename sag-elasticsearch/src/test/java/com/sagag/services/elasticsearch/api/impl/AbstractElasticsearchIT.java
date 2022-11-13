package com.sagag.services.elasticsearch.api.impl;

import com.sagag.services.common.utils.PageUtils;

import org.junit.Before;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;

import java.util.Locale;

/**
 * Abstract class for integration test of Elasticsearch service.
 */
public abstract class AbstractElasticsearchIT {

  protected static final Pageable DEF_PAGE = PageUtils.DEF_PAGE;

  protected static final String[] LOCKS_DCH = new String[] { "dch" };

  protected static final String[] LOCKS_TECHNO = new String[] { "tm" };

  protected static final String[] LOCKS_DAT = new String[] { "dat" };

  protected static final String[] LOCKS_MAT = new String[] { "mat" };

  protected static final String[] LOCKS_AT = new String[] { "mat", "dat" };

  @Before
  public void setup() {
    LocaleContextHolder.setLocale(Locale.GERMAN);
  }
}
