package com.sagag.services.autonet.erp.converter;

import java.util.Optional;

import javax.xml.bind.JAXBElement;

import com.sagag.services.autonet.erp.wsdl.tmconnect.MemoType;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AutonetMemoConverter {

  public static ErpArticleMemo fromMemoType(MemoType memoType) {
    return ErpArticleMemo.builder().type(memoType.getType())
        .text(Optional.ofNullable(memoType.getText()).map(JAXBElement::getValue).orElse(null))
        .type(Optional.ofNullable(memoType.getType()).map(Integer::intValue).orElse(null)).build();
  }
}
