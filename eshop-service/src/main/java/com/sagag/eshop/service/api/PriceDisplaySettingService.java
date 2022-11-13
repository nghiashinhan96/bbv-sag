package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.services.domain.eshop.dto.PriceDisplaySettingDto;

import java.util.List;

public interface PriceDisplaySettingService {

  List<PriceDisplaySettingDto> getPriceDisplaySetting(CustomerSettings customerSettings);

}
