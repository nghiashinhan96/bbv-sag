package com.sagag.services.common.currency.impl;

import com.sagag.services.common.currency.SagCurrencyParser;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;

@Component
@EnablePriceDiscountPromotion(false)
public class DefaultSagCurrencyParserImpl implements SagCurrencyParser {

  @Value("${country.config.currency:}")
  private String currencySymbol;

  @Override
  public String parse(Double price, NumberFormat formatter, SupportedAffiliate affiliate,
      String currencySymbol) {
    return defaultParse(price, formatter, affiliate, this.currencySymbol);
  }
}
