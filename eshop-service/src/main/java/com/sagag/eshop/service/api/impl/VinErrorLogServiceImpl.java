package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.VinErrorLogRepository;
import com.sagag.eshop.repo.entity.VinErrorLog;
import com.sagag.eshop.service.api.VinErrorLogService;
import com.sagag.eshop.service.converter.VinErrorLogConverters;
import com.sagag.services.domain.eshop.dto.VinErrorLogDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * VIN error log service implementation class.
 */
@Service
public class VinErrorLogServiceImpl implements VinErrorLogService {

  @Autowired
  private VinErrorLogRepository vinErrorLogRepository;

  @Override
  public void addVinErrorLog(VinErrorLogDto vinErrorLogDto) {
    Assert.notNull(vinErrorLogDto, "The given VIN error log must not be null");
    VinErrorLog vinErrorLog = VinErrorLogConverters.vinErrorLogEntityConverter().apply(vinErrorLogDto);
    vinErrorLogRepository.save(vinErrorLog);
  }
}
