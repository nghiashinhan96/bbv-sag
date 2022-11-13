package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopReleaseRepository;
import com.sagag.eshop.repo.entity.EshopRelease;
import com.sagag.eshop.service.api.EshopReleaseService;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.domain.eshop.dto.EshopReleaseDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class EshopReleaseServiceImpl implements EshopReleaseService {

  @Autowired
  private EshopReleaseRepository eshopReleaseRepo;

  @Override
  public Optional<EshopReleaseDto> getEshopRelease() {
    List<EshopRelease> eshopReleases = eshopReleaseRepo.findAll();
    return eshopReleases.stream().findFirst()
        .map(release -> SagBeanUtils.map(release, EshopReleaseDto.class));
  }
}
