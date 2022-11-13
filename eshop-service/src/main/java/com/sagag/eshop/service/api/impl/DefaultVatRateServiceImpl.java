package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.VatRateRepository;
import com.sagag.eshop.service.api.VatRateService;
import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;
import com.sagag.services.common.utils.SagBeanUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@EnablePriceDiscountPromotion(true)
public class DefaultVatRateServiceImpl implements VatRateService {

  @Autowired
  private VatRateRepository vatRateRepository;

  @Override
  public List<VatRateDto> getAll() {
    return vatRateRepository.findAll().stream()
        .map(vatRate -> SagBeanUtils.map(vatRate, VatRateDto.class)).collect(Collectors.toList());
  }
}
