package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.DeliveryTypesRepository;
import com.sagag.eshop.service.api.DeliveryTypesService;
import com.sagag.services.domain.eshop.dto.DeliveryTypeDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryTypesServiceImpl implements DeliveryTypesService {

  @Autowired
  private DeliveryTypesRepository deliveryRepo;

  @Override
  public List<DeliveryTypeDto> findAllDeliveryTypes() {
    return deliveryRepo.findAll().stream().map(d -> DeliveryTypeDto.builder().id(d.getId())
        .descCode(d.getDescCode()).description(d.getDescription()).type(d.getType())
        .allowChoose(true).build()).collect(Collectors.toList());
  }

  @Override
  public List<DeliveryTypeDto> findWholesalerDeliveryTypes(List<String> descCodes) {
    return deliveryRepo.findByDescCodeIn(descCodes)
        .stream()
        .map(d -> DeliveryTypeDto.builder()
            .id(d.getId())
            .descCode(d.getDescCode())
            .description(d.getDescription())
            .type(d.getType())
            .allowChoose(true)
            .build())
        .collect(Collectors.toList());
  }
}
