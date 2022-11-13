package com.sagag.services.hazelcast.api;

import com.sagag.services.hazelcast.app.HazelcastMaps;
import com.sagag.services.hazelcast.domain.EshopGlobalSettingDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EshopGlobalSettingCacheService extends CacheService {

  boolean isAllowedToUse(String settingType, String code);

  Map<String, Boolean> getSettingTypeSettings(String settingType);

  @Override
  default String defName() {
    return HazelcastMaps.GLOBAL_SETTING_MAP.name();
  }

  /**
   * Gets global setting by code.
   *
   * @param code
   * @return Optional of {@link EshopGlobalSettingDto}
   */
  Optional<EshopGlobalSettingDto> getSettingByCode(String code);
  
  /**
   * Gets global setting by codes.
   *
   * @param codes
   * @return Map of {@link EshopGlobalSettingDto}
   */
  Map<String, EshopGlobalSettingDto> getSettingByCodes(List<String> codes);

}
