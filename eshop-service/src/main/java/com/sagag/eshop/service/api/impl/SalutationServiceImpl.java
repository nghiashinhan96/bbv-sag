package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.SalutationRepository;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.service.api.SalutationService;
import com.sagag.services.common.enums.SalutationType;
import com.sagag.services.domain.eshop.dto.SalutationDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalutationServiceImpl implements SalutationService {

  @Autowired
  private SalutationRepository salutationRepo;

  @Override
  public List<SalutationDto> getProfileSalutations() {
    List<Salutation> salutations = salutationRepo.findAllByType(SalutationType.PROFILE.name());
    if (CollectionUtils.isEmpty(salutations)) {
      return Collections.emptyList();
    }
    return salutations.stream()
        .map(salutationConverter()).collect(Collectors.toList());
  }

  private static Function<Salutation, SalutationDto> salutationConverter() {
    return salutation -> {
      SalutationDto salutationDto = new SalutationDto();
      salutationDto.setId(salutation.getId());
      salutationDto.setDescription(salutation.getDescription());
      salutationDto.setCode(salutation.getCode());
      return salutationDto;
    };
  }

  @Override
  public Salutation getById(int id) {
    return salutationRepo.findById(id).orElse(null);
  }

  @Override
  public List<String> getOfferSalutations() {
    final List<Salutation> salutations = salutationRepo.findAllByType(SalutationType.OFFER.name());
    if (CollectionUtils.isEmpty(salutations)) {
      return Collections.emptyList();
    }
    return salutations.stream().map(Salutation::getCode).collect(Collectors.toList());
  }

  @Override
  public List<SalutationDto> getSalutationByCodes(String... codes) {
    if (ArrayUtils.isEmpty(codes)) {
      return Collections.emptyList();
    }
    return salutationRepo.findByCodeIn(Arrays.asList(codes)).stream().map(salutationConverter())
        .collect(Collectors.toList());
  }
}
