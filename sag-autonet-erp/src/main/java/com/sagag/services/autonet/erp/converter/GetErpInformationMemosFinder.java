package com.sagag.services.autonet.erp.converter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.autonet.erp.wsdl.tmconnect.ArrayOfMemoType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ErpInformationType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.MemoType;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;

@Component
@AutonetProfile
public class GetErpInformationMemosFinder extends AbstractGetErpInformationFinder {

  public Optional<List<ErpArticleMemo>> process(String uuid, List<ErpInformationType> erpInfos) {
    List<MemoType> memoTypes =
        erpInfos.stream().filter(item -> uuid.equals(getUuidForErpInformationType(item))).findAny()
            .map(this::getMemoTypes).orElse(Collections.emptyList());

    if (CollectionUtils.isEmpty(memoTypes)) {
      return Optional.empty();
    }

    return Optional.of(
        memoTypes.stream().map(AutonetMemoConverter::fromMemoType).collect(Collectors.toList()));
  }

  private List<MemoType> getMemoTypes(ErpInformationType erpInfo) {
    JAXBElement<ArrayOfMemoType> arrayOfMemoType = erpInfo.getMemos();
    if (Objects.isNull(arrayOfMemoType)) {
      return Collections.emptyList();
    }
    return Optional.ofNullable(arrayOfMemoType.getValue()).map(ArrayOfMemoType::getMemo)
        .orElse(Collections.emptyList());
  }
}
