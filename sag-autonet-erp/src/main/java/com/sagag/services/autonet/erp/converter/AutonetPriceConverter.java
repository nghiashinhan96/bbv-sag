package com.sagag.services.autonet.erp.converter;

import java.math.BigDecimal;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import com.sagag.services.autonet.erp.wsdl.tmconnect.PriceType;
import com.sagag.services.domain.sag.erp.ErpArticlePrice;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AutonetPriceConverter {

  public static ErpArticlePrice fromPriceType(PriceType priceType) {
    return ErpArticlePrice.builder().type(priceType.getType())
        .description(
            Optional.ofNullable(priceType.getDescription()).map(JAXBElement::getValue).orElse(null))
        .value(Optional.ofNullable(priceType.getValue()).map(BigDecimal::doubleValue).orElse(null))
        .rebateValue(Optional.ofNullable(priceType.getRebateValue()).map(BigDecimal::doubleValue)
            .orElse(null))
        .rebate(
            Optional.ofNullable(priceType.getRebate()).map(BigDecimal::doubleValue).orElse(null))
        .currencySymbol(Optional.ofNullable(priceType.getCurrencySymbol())
            .map(JAXBElement::getValue).orElse(null))
        .currencyCode(Optional.ofNullable(priceType.getCurrencyCodeIso4217())
            .map(JAXBElement::getValue).orElse(null))
        .priceUnit(
            Optional.ofNullable(priceType.getPriceUnit()).map(BigDecimal::doubleValue).orElse(null))
        .vat(Optional.ofNullable(priceType.getVAT()).map(BigDecimal::doubleValue).orElse(null))
        .taxIncluded(priceType.isTaxIncluded()).build();
  }
}
