package com.sagag.services.hazelcast.domain;

import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheJobDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheSmartCartDto;

import lombok.Data;

import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class HaynesProCacheData implements Serializable {

  private static final long serialVersionUID = 739409986536464229L;

  private HaynesProCacheSmartCartDto cart;

  private Map<String, List<HaynesProCacheJobDto>> labourTimes;

  public void addSmartCart(HaynesProCacheSmartCartDto cart) {
    setCart(cart);
  }

  public void clearSmartCart() {
    setCart(null);
  }

  public void clearLabourTimes() {
    if (MapUtils.isEmpty(labourTimes)) {
      return;
    }
    labourTimes.clear();
  }

  public Map<String, List<HaynesProCacheJobDto>> getLabourTimes() {
    if (MapUtils.isEmpty(labourTimes)) {
      labourTimes = new HashMap<>();
    }
    return labourTimes;
  }
}
