package com.sagag.services.stakis.erp.translator;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.enums.SendMethodType;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.translator.IDataTranslator;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.stakis.erp.enums.StakisSendMethodType;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfDispatchType;
import com.sagag.services.stakis.erp.wsdl.cis.DispatchType;
import com.sagag.services.stakis.erp.wsdl.cis.DispatchTypeList;

@Component
@CzProfile
public class CisDeliveryInfoTranslator
    implements IDataTranslator<DispatchTypeList, Map<SendMethodType, List<DeliveryTypeDto>>> {

  private static final String DEFAULT_DESCIPTION_PICKUP = "OSOBNÍ ODBĚR NA POBOČCE";
  private static final SendMethodType DF_SEND_METHOD = StakisSendMethodType.SELF.getSendMethod();

  @Override
  public Map<SendMethodType, List<DeliveryTypeDto>> translateToConnect(
      DispatchTypeList dispatchTypeList) {
    Map<SendMethodType, List<DeliveryTypeDto>> result =
        new HashMap<SendMethodType, List<DeliveryTypeDto>>();
    SendMethodType defSendMethod = DF_SEND_METHOD;
    List<DeliveryTypeDto> deliveryTypes = new ArrayList<DeliveryTypeDto>();
    deliveryTypes.add(buildDefaultPickupDeliveryType());
    final String defDeliveryId =
        getValueOpt(dispatchTypeList.getDefault()).orElse(StringUtils.EMPTY);
    if (StringUtils.isNoneBlank(defDeliveryId)) {
      final List<DispatchType> dispatchTypes = getValueOpt(dispatchTypeList.getDispatchTypes())
          .map(ArrayOfDispatchType::getDispatchType).orElse(Collections.emptyList());
      Optional<DispatchType> defDispatchTypeOpt = dispatchTypes.stream()
          .filter(type -> defDeliveryId.equals(getValueOpt(type.getId()).orElse(StringUtils.EMPTY)))
          .findFirst();
      if (defDispatchTypeOpt.isPresent()) {
        final Optional<StakisSendMethodType> defStakisSendMethodOpt =
            getValueOpt(defDispatchTypeOpt.get().getId()).map(StakisSendMethodType::valueOf);
        defSendMethod =
            defStakisSendMethodOpt.map(StakisSendMethodType::getSendMethod).orElse(DF_SEND_METHOD);
        dispatchTypes.stream().map(d -> convertToDeliveryType(d))
            .collect(Collectors.toCollection(() -> deliveryTypes));
      }
    }
    result.put(defSendMethod, deliveryTypes.stream()
        .filter(distinctByKey(DeliveryTypeDto::getDescCode)).collect(Collectors.toList()));

    return result;
  }

  private DeliveryTypeDto buildDefaultPickupDeliveryType() {
    return DeliveryTypeDto.builder().allowChoose(true).descCode(DF_SEND_METHOD.name())
        .description(DEFAULT_DESCIPTION_PICKUP).type(DEFAULT_DESCIPTION_PICKUP)
        .id(StakisSendMethodType.SELF.getId()).build();
  }

  private DeliveryTypeDto convertToDeliveryType(DispatchType dispatchType) {
    final String descCodeStr = getValueOpt(dispatchType.getId()).orElse(StringUtils.EMPTY);
    return DeliveryTypeDto.builder().allowChoose(true)
        .descCode(StakisSendMethodType.valueOf(descCodeStr).getSendMethod().name())
        .description(getValueOpt(dispatchType.getDescription()).orElse(StringUtils.EMPTY))
        .type(getValueOpt(dispatchType.getDescription()).orElse(StringUtils.EMPTY))
        .id(StakisSendMethodType.valueOf(descCodeStr).getId()).build();
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }
}
