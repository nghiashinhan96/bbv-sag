package com.sagag.eshop.service.api.impl.price;

import com.sagag.eshop.repo.api.PriceDisplayTypeRepository;
import com.sagag.eshop.repo.entity.CustomerSettings;
import com.sagag.eshop.service.api.PriceDisplaySettingService;
import com.sagag.services.common.profiles.ChProfile;
import com.sagag.services.domain.eshop.dto.PriceDisplaySettingDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@ChProfile
public class CHPriceDisplaySettingImpl implements PriceDisplaySettingService {

  @Autowired
  private PriceDisplayTypeRepository priceDisplayTypeRepo;

  @Override
  public List<PriceDisplaySettingDto> getPriceDisplaySetting(CustomerSettings customerSettings) {
    return priceDisplayTypeRepo.findAll().stream().map(price -> PriceDisplaySettingDto.builder()
        .id(price.getId())
        .descriptionCode(price.getDescCode())
        .enable(Objects.equals(price.getId(), customerSettings.getPriceDisplayId()))
        .editable(true)
        .build()).collect(Collectors.toList());
  }
}
