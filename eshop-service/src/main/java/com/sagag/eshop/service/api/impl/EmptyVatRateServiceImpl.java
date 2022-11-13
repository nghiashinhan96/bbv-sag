package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.service.api.VatRateService;
import com.sagag.eshop.service.dto.VatRateDto;
import com.sagag.services.common.profiles.EnablePriceDiscountPromotion;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@EnablePriceDiscountPromotion(false)
public class EmptyVatRateServiceImpl implements VatRateService {

  private static final String WARN_MSG = "No support Vat Rate for this System";

  @Override
  public List<VatRateDto> getAll() {
    log.debug(WARN_MSG);
    return Collections.emptyList();
  }
}
