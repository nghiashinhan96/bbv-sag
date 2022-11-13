package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.eshop.repo.api.EshopGlobalSettingRepository;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.hazelcast.api.EshopGlobalSettingCacheService;
import com.sagag.services.hazelcast.domain.EshopGlobalSettingDto;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EshopGlobalSettingCacheServiceImpl extends CacheDataProcessor
    implements EshopGlobalSettingCacheService {

  @Autowired
  private EshopGlobalSettingRepository eshopGlobalSettingRepo;

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Override
  public boolean isAllowedToUse(String settingType, String codeValue) {
    if (StringUtils.isAnyBlank(settingType, codeValue)) {
      return false;
    }
    final IMap<String, EshopGlobalSettingDto> cachedGlobalSettings =
        hazelcastInstance.getMap(defName());
    if (!cachedGlobalSettings.containsKey(codeValue)) {
      return false;
    }
    EshopGlobalSettingDto item = cachedGlobalSettings.get(codeValue);
    return StringUtils.equalsIgnoreCase(item.getSettingType(), settingType) && item.isEnabled();
  }

  @Override
  public void refreshCacheAll() {
    hazelcastInstance.getMap(defName()).evictAll();

    final List<EshopGlobalSettingDto> items = eshopGlobalSettingRepo.findAll().stream()
        .map(item -> SagBeanUtils.map(item, EshopGlobalSettingDto.class))
        .collect(Collectors.toList());
    final IMap<String, EshopGlobalSettingDto> cachedGlobalSettings =
        hazelcastInstance.getMap(defName());
    items.forEach(item -> cachedGlobalSettings.put(item.getCode(), item));
  }

  @Override
  public boolean exists() {
    return !hazelcastInstance.getList(defName()).isEmpty();
  }

  @Override
  public Map<String, Boolean> getSettingTypeSettings(String settingType) {
    if (StringUtils.isBlank(settingType)) {
      return Collections.emptyMap();
    }
    final Map<String, Boolean> settingsMap = new HashMap<>();
    final IMap<String, EshopGlobalSettingDto> cachedGlobalSettings =
        hazelcastInstance.getMap(defName());
    cachedGlobalSettings.values(Predicates.equal("settingType", settingType))
    .forEach(setting -> settingsMap.put(setting.getCode(), setting.isEnabled()));
    return settingsMap;
  }

  @Override
  public Optional<EshopGlobalSettingDto> getSettingByCode(String code) {
    if (StringUtils.isBlank(code)) {
      return Optional.empty();
    }
    final IMap<String, EshopGlobalSettingDto> cachedGlobalSettings =
        hazelcastInstance.getMap(defName());
    return Optional.ofNullable(cachedGlobalSettings.get(code));
  }
  
  @Override
  public Map<String, EshopGlobalSettingDto> getSettingByCodes(List<String> codes) {
    Set<String> distincCodes = CollectionUtils.emptyIfNull(codes).stream()
        .filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    if (CollectionUtils.isEmpty(distincCodes)) {
      return Collections.emptyMap();
    }
    final IMap<String, EshopGlobalSettingDto> cachedGlobalSettings =
        hazelcastInstance.getMap(defName());
    return cachedGlobalSettings.getAll(distincCodes);
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

}
