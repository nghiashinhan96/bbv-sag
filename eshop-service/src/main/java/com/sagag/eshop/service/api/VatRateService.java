package com.sagag.eshop.service.api;

import com.sagag.eshop.service.dto.VatRateDto;

import java.util.List;

public interface VatRateService {

  /**
   * Get all vat rate
   *
   */
  List<VatRateDto> getAll();

}

