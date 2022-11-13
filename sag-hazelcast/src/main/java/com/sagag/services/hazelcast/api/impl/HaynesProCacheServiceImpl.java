package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.common.utils.VehicleUtils;
import com.sagag.services.domain.sag.haynespro.LabourTimeJobDto;
import com.sagag.services.hazelcast.api.HaynesProCacheService;
import com.sagag.services.hazelcast.domain.HaynesProCacheData;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheJobDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheSmartCartDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheVehicleDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HaynesProCacheServiceImpl implements HaynesProCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;

  @Override
  public void saveHpSmartCart(String key, HaynesProCacheSmartCartDto hpCart) {
    log.debug("Saving HaynesPro Smart Cart by key = {} - hpCart = {}", key, hpCart);
    final HaynesProCacheData cacheData = getDefaultHpCacheData(key);
    cacheData.addSmartCart(hpCart);
    putHpCacheData(key, cacheData);
  }

  @Override
  public Optional<HaynesProCacheSmartCartDto> getHpSmartCart(String key) {
    log.debug("Getting HaynesPro Smart Cart by key = {}", key);
    if (!findCacheMap().containsKey(key)) {
      return Optional.empty();
    }
    return Optional.ofNullable(findCacheMap().get(key).getCart())
        .filter(smartCart -> smartCart != null && smartCart.hasValidParts());
  }

  @Override
  public void clearSmartCart(String key) {
    log.debug("Clearing smart cart by key = {}", key);
    final HaynesProCacheData cacheData = getDefaultHpCacheData(key);
    cacheData.clearSmartCart();
    putHpCacheData(key, cacheData);
  }

  @Override
  public List<LabourTimeJobDto> getLabourTimes(String key, String vehicleId, double vatRate) {
    log.debug("Getting labour times by key = {} -  vehicleId = {}", key, vehicleId);
    Assert.notNull(vehicleId, "vehicleId should not be null");
    if (!findCacheMap().containsKey(key)) {
      return Collections.emptyList();
    }
    String labourTimeId = VehicleUtils.getMotorNrs(vehicleId);

    List<HaynesProCacheJobDto> haynesProJobs = findCacheMap().get(key).getLabourTimes()
        .get(labourTimeId);

    if (CollectionUtils.isEmpty(haynesProJobs)) {
      labourTimeId = VehicleUtils.getKTypeNr(vehicleId);
      haynesProJobs = findCacheMap().get(key).getLabourTimes().get(labourTimeId);
      if (CollectionUtils.isEmpty(haynesProJobs)) {
        return Collections.emptyList();
      }
    }

    return haynesProJobs.stream().map(job -> job.toLabourTimeJobDto(vatRate))
        .collect(Collectors.toList());
  }

  @Override
  public void addLabourTimes(String key, String vehicleId, List<HaynesProCacheJobDto> hpJobs) {
    log.debug("Adding labour times by key = {} - vehicleId = {} and hpJobs = {}", key, vehicleId,
        hpJobs);
    if (StringUtils.isAnyBlank(key, vehicleId) || CollectionUtils.isEmpty(hpJobs)) {
      return;
    }
    final String motorId = VehicleUtils.getMotorNrs(vehicleId);
    final String labourTimeId = HaynesProCacheVehicleDto.of(vehicleId)
        .getLabourTimeIdAfterFilterMotorId(motorId);
    log.debug("addLabourTime jobs with labourTimeId {}", labourTimeId);

    final HaynesProCacheData cacheData = getDefaultHpCacheData(key);
    cacheData.getLabourTimes().put(labourTimeId, hpJobs);
    putHpCacheData(key, cacheData);
  }

  @Override
  public void saveLabourTimes(String key, HaynesProCacheVehicleDto vehicle, final String vehId,
      List<HaynesProCacheJobDto> hpJobs) {
    log.debug("Saving labour times by key = {}, vehicle = {}, vehId = {} and hpJobs = {}", key,
        vehicle, vehId,  SagJSONUtil.convertObjectToPrettyJson(hpJobs));

    final String labourTimeId = vehicle.getLabourTimeIdAfterFilterMotorId(
        VehicleUtils.getMotorNrs(vehId));
    log.debug("addLabourTime jobs with labourTimeId {}: ", labourTimeId);

    final HaynesProCacheData cacheData = getDefaultHpCacheData(key);
    cacheData.getLabourTimes().put(labourTimeId, hpJobs);
    putHpCacheData(key, cacheData);
  }

  @Override
  public void removeLabourTime(String key, String vehicleId, String awNumber) {
    log.debug("Removing labour times by key = {} - vehicleId = {} and awNumber = {}", key,
        vehicleId, awNumber);
    if (StringUtils.isEmpty(vehicleId) || StringUtils.isEmpty(awNumber)) {
      throw new IllegalArgumentException("vehicleId and awNumber should not be empty!");
    }

    String labourTimeId = VehicleUtils.getMotorNrs(vehicleId);
    final HaynesProCacheData cacheData = getDefaultHpCacheData(key);
    List<HaynesProCacheJobDto> savedJobs = cacheData.getLabourTimes().get(labourTimeId);
    if (CollectionUtils.isEmpty(savedJobs)) {
      labourTimeId = VehicleUtils.getKTypeNr(vehicleId);
      savedJobs = cacheData.getLabourTimes().get(labourTimeId);
      if (CollectionUtils.isEmpty(savedJobs)) {
        return;
      }
    }
    final List<HaynesProCacheJobDto> filteredJobs = savedJobs.stream()
        .filter(job -> !awNumber.equals(job.getAwNumber())).collect(Collectors.toList());
    log.debug("removeLabourTime filteredJobs by labourTimeId = {} \nfilteredJobs = {}",
        labourTimeId, filteredJobs);

    if (filteredJobs.isEmpty()) {
      cacheData.getLabourTimes().remove(labourTimeId);
    } else {
      cacheData.getLabourTimes().put(labourTimeId, filteredJobs);
    }
    putHpCacheData(key, cacheData);
  }

  @Override
  public void clearLabourTimes(String key) {
    log.debug("Clearing labour times by key = {}", key);
    if (StringUtils.isBlank(key)) {
      return;
    }

    final HaynesProCacheData cacheData = getDefaultHpCacheData(key);
    cacheData.clearLabourTimes();
    putHpCacheData(key, cacheData);
  }


  private IMap<String, HaynesProCacheData> findCacheMap() {
    return hazelcastInstance.getMap(defName());
  }

  private HaynesProCacheData getDefaultHpCacheData(String key) {
    return findCacheMap().containsKey(key) ? findCacheMap().get(key) : new HaynesProCacheData();
  }

  private void putHpCacheData(String key, HaynesProCacheData cacheData) {
    findCacheMap().put(key, cacheData);
  }

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }
}
