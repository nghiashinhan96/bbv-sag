package com.sagag.services.common.currency.impl;

import com.sagag.services.common.currency.SagCurrencyParser;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;

@EnablePriceDiscountPromotion(true)
@Component
public class PriceDiscountCurrencyParserImpl implements SagCurrencyParser {

  @Override
  public String parse(Double price, NumberFormat formatter, SupportedAffiliate affiliate,
      String currencySymbol) {
    return defaultParse(price, formatter, affiliate, currencySymbol);
  }
}
