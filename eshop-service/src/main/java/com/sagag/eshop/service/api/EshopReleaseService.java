package com.sagag.eshop.service.api;

import com.sagag.services.domain.eshop.dto.EshopReleaseDto;

import java.util.Optional;

public interface EshopReleaseService {

  Optional<EshopReleaseDto> getEshopRelease();

}
