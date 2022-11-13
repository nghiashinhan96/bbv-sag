package com.sagag.services.hazelcast.api.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import com.sagag.eshop.repo.api.TourTimeRepository;
import com.sagag.eshop.repo.entity.TourTime;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.hazelcast.api.TourTimeCacheService;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TourTimeCacheServiceImpl extends CacheDataProcessor implements TourTimeCacheService {

  @Autowired
  private HazelcastInstance hazelcastInstance;
  @Autowired
  private TourTimeRepository tourTimeRepo;

  @Override
  public HazelcastInstance hzInstance() {
    return hazelcastInstance;
  }

  @Override
  public void refreshCacheAll() {
    log.info("Caching all available tour time table data from DB to Hazelcast instance");
    hazelcastInstance.getMap(defName()).evictAll();
  }

  @Override
  public List<TourTimeDto> searchTourTimesByCustNr(String custNr) {
    return searchTourTimesByFunctionProvider(custNr, "customerNumber",
        custNumber -> tourTimeRepo.findByCustomerNumber(custNumber));
  }

  private List<TourTimeDto> searchTourTimesByFunctionProvider(String searchTerm, String searchParam,
      Function<String, List<TourTime>> function) {
    final IMap<Integer, TourTimeDto> tourTimeMap = hazelcastInstance.getMap(defName());
    List<TourTimeDto> tourTimeDtoList = tourTimeMap
        .values(Predicates.equal(searchParam, searchTerm)).stream().collect(Collectors.toList());

    if (CollectionUtils.isEmpty(tourTimeDtoList)) {
      tourTimeDtoList = function.apply(searchTerm).stream()
          .map(tour -> SagBeanUtils.map(tour, TourTimeDto.class)).collect(Collectors.toList());
      tourTimeDtoList.stream().forEach(tourTime -> tourTimeMap.put(tourTime.getId(), tourTime));
    }
    return tourTimeDtoList;
  }

}
