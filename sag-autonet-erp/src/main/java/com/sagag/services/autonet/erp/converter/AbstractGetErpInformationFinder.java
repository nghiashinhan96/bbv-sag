package com.sagag.services.autonet.erp.converter;

import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;

import com.sagag.services.autonet.erp.wsdl.tmconnect.EntityLinkType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ErpInformationType;

public abstract class AbstractGetErpInformationFinder {

  protected String getUuidForErpInformationType(ErpInformationType erpInfo) {
    JAXBElement<EntityLinkType> entityLinkType = erpInfo.getItem();
    if (Objects.isNull(entityLinkType)) {
      return StringUtils.EMPTY;
    }
    return Optional.ofNullable(entityLinkType.getValue()).map(EntityLinkType::getGuid)
        .orElse(StringUtils.EMPTY);
  }
}
