package com.sagag.services.ivds.oates.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.ivds.oates.OatesCacheService;
import com.sagag.services.oates.dto.OatesApplicationDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

@Service
public class OatesCacheServiceImpl implements OatesCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Override
  public void saveOatesApplications(String userKey, List<OatesApplicationDto> applications) {
    Assert.notEmpty(applications, "The given applications must not be empty");
    hzInstance().getMap(getCacheName()).put(userKey, applications);
  }

  @Override
  public List<OatesApplicationDto> getOatesApplicationsByUserKey(String userKey) {
    if (StringUtils.isBlank(userKey)) {
      return Collections.emptyList();
    }
    IMap<String, List<OatesApplicationDto>> applicationMap = hzInstance().getMap(getCacheName());
    return applicationMap.getOrDefault(userKey, Collections.emptyList());
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

}
