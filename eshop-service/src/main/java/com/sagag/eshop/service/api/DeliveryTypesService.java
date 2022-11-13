package com.sagag.eshop.service.api;

import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;

import java.util.List;

public interface DeliveryTypesService {

  List<DeliveryTypeDto> findAllDeliveryTypes();

  List<DeliveryTypeDto> findWholesalerDeliveryTypes(List<String> descCodes);

}
