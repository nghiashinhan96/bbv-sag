package com.sagag.services.tools.support;

import com.sagag.services.tools.domain.target.MappingUserIdEblConnect;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@Data
public class CommonInitialResource implements Serializable {

  private static final long serialVersionUID = 5342496340914055835L;

  private Map<Long, Integer> organisationIdMap;

  private Map<String, Integer> currencyIdMap;

  private Map<Long, MappingUserIdEblConnect> mappingUserIdMap;

  private Map<String, Integer> languageMap;

  private Set<Long> sourceOrgSet;

  public Integer getCurrencyId(String isoCode) {
    if (MapUtils.isEmpty(currencyIdMap)) {
      return null;
    }
    return currencyIdMap.get(isoCode);
  }

  public Integer getOrgId(Long eblOrgId) {
    if (MapUtils.isEmpty(organisationIdMap) || eblOrgId == null) {
      return null;
    }
    Integer id = organisationIdMap.get(eblOrgId);
    if (id == null) {
      return null;
    }
    return id.intValue() == NumberUtils.INTEGER_ZERO ? null : id;
  }

  public Long getDefaultUserId(Long eblUserId, Long defaultUserId) {
    if (eblUserId != null && eblUserId.equals(defaultUserId)) {
      return defaultUserId;
    }
    return getUserId(eblUserId);
  }

  public Long getDefaultCreatedUserId(Long eblUserId, Long defaultUserId) {
    if (eblUserId != null && eblUserId.equals(defaultUserId)) {
      return defaultUserId;
    }
    Long createdUserId = getUserId(eblUserId);
    return createdUserId == null ? defaultUserId : createdUserId;
  }

  public Long getDefaultModifiedUserId(Long eblUserId, Long defaultUserId) {
    if (eblUserId == null || eblUserId == NumberUtils.LONG_ZERO) {
      return null;
    }
    return getDefaultUserId(eblUserId, defaultUserId);
  }

  public Long getUserId(Long eblUserId) {
    MappingUserIdEblConnect connect = getMappingConnectByUserId(eblUserId);
    if (connect == null) {
      return null;
    }
    return connect.getConnectUserId().longValue() == NumberUtils.LONG_ZERO ? null : connect.getConnectUserId();
  }

  public Integer getOrgIdByEblUserId(Long eblUserId) {
    MappingUserIdEblConnect connect = getMappingConnectByUserId(eblUserId);
    if (connect == null) {
      return null;
    }
    return connect.getConnectOrgId() == NumberUtils.INTEGER_ZERO ? null : connect.getConnectOrgId();
  }

  public MappingUserIdEblConnect getMappingConnectByUserId(Long eblUserId) {
    if (MapUtils.isEmpty(mappingUserIdMap)) {
      return null;
    }
    return mappingUserIdMap.get(eblUserId);
  }

  public Integer getLanguageId(String isoCode) {
    if (MapUtils.isEmpty(languageMap)) {
      return 1; // Default: DE
    }
    return languageMap.getOrDefault(isoCode, 1);
  }

  public Integer getDeliveryTypeId(String deliveryType) {
    if (StringUtils.isBlank(deliveryType)) {
      return DeliveryType.OTHER.getId();
    }
    return DeliveryType.idOf(deliveryType);
  }
}
