package com.sagag.services.common.number.impl;

import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.common.number.NumberFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component
public class DefaultNumberFormatterImpl implements NumberFormatter {

  private static final int DF_MAX_FRACTION_DIGITS = 2;

  private static final int DF_MIN_FRACTION_DIGITS = 2;

  @Autowired
  private LocaleContextHelper localeContextHelper;

  @Override
  public NumberFormat getNumberFormatter(String localeStr) {
    final Locale curLocale = localeContextHelper.toLocaleOrigin(localeStr);
    final NumberFormat formatter = NumberFormat.getNumberInstance(curLocale);
    formatter.setMaximumFractionDigits(DF_MAX_FRACTION_DIGITS);
    formatter.setMinimumFractionDigits(DF_MIN_FRACTION_DIGITS);
    return formatter;
  }

}
