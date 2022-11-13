package com.sagag.services.autonet.erp.converter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.autonet.erp.wsdl.tmconnect.AvailabilityStateType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.EntityLinkType;
import com.sagag.services.autonet.erp.wsdl.tmconnect.ErpInformationType;
import com.sagag.services.common.profiles.AutonetProfile;
import com.sagag.services.domain.sag.erp.ErpArticleAvailability;

@Component
@AutonetProfile
public class GetErpInformationAvailabilitiesFinder extends AbstractGetErpInformationFinder {

  public Optional<ErpArticleAvailability> process(String uuid, List<ErpInformationType> erpInfos,
      List<AvailabilityStateType> availabilityStateTypes) {

    if (StringUtils.isEmpty(uuid) || CollectionUtils.isEmpty(erpInfos)
        || CollectionUtils.isEmpty(availabilityStateTypes)) {
      return Optional.empty();
    }

    ErpInformationType erpInformationType = erpInfos.stream()
        .filter(item -> uuid.equals(getUuidForErpInformationType(item))).findAny().orElse(null);
    if (Objects.isNull(erpInformationType)) {
      return Optional.empty();
    }

    EntityLinkType entityLinkType = Optional.ofNullable(erpInformationType.getAvailabilityState())
        .map(JAXBElement::getValue).orElse(null);
    String availabilityUuid =
        Optional.ofNullable(entityLinkType).map(EntityLinkType::getGuid).orElse(StringUtils.EMPTY);

    AvailabilityStateType availabilityStateType = availabilityStateTypes.stream()
        .filter(item -> availabilityUuid.endsWith(item.getGuid())).findFirst().orElse(null);

    if (Objects.isNull(availabilityStateType)) {
      return Optional.empty();
    }

    return Optional
        .of(AutonetAvailabilityStateConverter.fromAvailabilityStateType(availabilityStateType));
  }
}
