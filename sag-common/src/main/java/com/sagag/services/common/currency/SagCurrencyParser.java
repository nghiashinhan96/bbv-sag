package com.sagag.services.common.currency;

import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.SupportedAffiliate;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public interface SagCurrencyParser {

  String parse(Double price, NumberFormat formatter, SupportedAffiliate affiliate, String currencySymbol); 

  default String defaultParse(Double price, NumberFormat formatter, SupportedAffiliate affiliate,
      String currencySymbol) {
    if (!affiliate.isCzAffiliate()) {
      return currencySymbol + StringUtils.SPACE + toCurrencyStr(price, formatter);
    }

    final DecimalFormat decimalFormatter = ((DecimalFormat) formatter);
    DecimalFormatSymbols decimalFormatSymbols = decimalFormatter.getDecimalFormatSymbols();
    decimalFormatSymbols.setGroupingSeparator(CharUtils.toChar(SagConstants.DOT));
    decimalFormatter.setDecimalFormatSymbols(decimalFormatSymbols);
    return currencySymbol + StringUtils.SPACE + toCurrencyStr(price, decimalFormatter);
  }
  
  default String toCurrencyStr(final Double value, NumberFormat formatter) {
    return value != null ? formatter.format(value) : StringUtils.EMPTY;
  }

}
