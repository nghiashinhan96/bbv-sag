package com.sagag.services.autonet.erp.converter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfPriceType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ErpInformationType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.PriceType;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.sag.erp.ErpArticlePrice;

@Component
@AutonetProfile
public class GetErpInformationPricesFinder extends AbstractGetErpInformationFinder {

  public Optional<List<ErpArticlePrice>> process(String uuid, List<ErpInformationType> erpInfos) {
    List<PriceType> priceTypes =
        erpInfos.stream().filter(item -> uuid.equals(getUuidForErpInformationType(item))).findAny()
            .map(this::getPriceTypes).orElse(Collections.emptyList());

    if (CollectionUtils.isEmpty(priceTypes)) {
      return Optional.empty();
    }
    return Optional.of(
        priceTypes.stream().map(AutonetPriceConverter::fromPriceType).collect(Collectors.toList()));
  }

  private List<PriceType> getPriceTypes(ErpInformationType erpInfo) {
    JAXBElement<ArrayOfPriceType> arrayOfPriceType = erpInfo.getPrices();
    if (Objects.isNull(arrayOfPriceType)) {
      return Collections.emptyList();
    }
    return Optional.ofNullable(arrayOfPriceType.getValue()).map(ArrayOfPriceType::getPrice)
        .orElse(Collections.emptyList());
  }
}
