package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.TourTimeRepository;
import com.sagag.eshop.repo.entity.TourTime;
import com.sagag.eshop.service.api.DeliveryTypeAdditionalService;
import com.sagag.services.common.profiles.AxCzProfile;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;
import com.sagag.services.domain.eshop.dto.PaymentSettingDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AxCzProfile
public class AxCzDeliveryTypeAdditionalServiceImpl implements DeliveryTypeAdditionalService {

  private static final String DELIVERY_TYPE_TOUR = "TOUR";

  @Autowired
  private TourTimeRepository tourTimeRepository;

  @Override
  public void additionalHandleForDeliveryType(String orgCode, PaymentSettingDto paymentSettingDto) {
    List<TourTime> tourTimes = tourTimeRepository.findByCustomerNumber(orgCode);
    if (CollectionUtils.isEmpty(tourTimes)) {
      List<DeliveryTypeDto> deliveryTypeWithoutTour = paymentSettingDto
          .getDeliveryTypes().stream().filter(deliveryType -> !StringUtils
              .equalsIgnoreCase(deliveryType.getDescCode(), DELIVERY_TYPE_TOUR))
          .collect(Collectors.toList());
      paymentSettingDto.setDeliveryTypes(deliveryTypeWithoutTour);
    }
  }


}
