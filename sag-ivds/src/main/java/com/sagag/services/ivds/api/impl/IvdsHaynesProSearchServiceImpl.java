package com.sagag.services.ivds.api.impl;

import com.sagag.services.common.exception.ServiceException;
import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.haynespro.api.HaynesProService;
import com.sagag.services.haynespro.dto.HaynesProShoppingCart;
import com.sagag.services.hazelcast.api.HaynesProCacheService;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheJobDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCachePartDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheSmartCartDto;
import com.sagag.services.ivds.api.IvdsHaynesProSearchService;
import com.sagag.services.ivds.api.IvdsOilSearchService;
import com.sagag.services.ivds.api.IvdsVehicleService;
import com.sagag.services.ivds.response.HaynesProClientResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class IvdsHaynesProSearchServiceImpl implements IvdsHaynesProSearchService {

  @Autowired
  private HaynesProService haynesProService;

  @Autowired
  private HaynesProCacheService haynesProCacheService;

  @Autowired
  private IvdsOilSearchService ivdsOilSerchService;

  @Autowired
  private IvdsVehicleService ivdsVehicleService;

  @Override
  public void handleHaynesProCallback(String userKey, String vehId, BufferedReader bufferedReader) {
    log.debug("Handling HaynesPro callback by userKey = {} - vehId = {}", userKey, vehId);
    haynesProService.getHaynesProShoppingCart(userKey, bufferedReader)
    .map(hpSmartCartConverter())
    .ifPresent(processHaynesProSmartCart(userKey, vehId));
  }

  private Function<HaynesProShoppingCart, HaynesProCacheSmartCartDto> hpSmartCartConverter() {
    return hpShoppingCart -> {
      final HaynesProCacheSmartCartDto smartCart = SagBeanUtils.map(hpShoppingCart,
          HaynesProCacheSmartCartDto.class);

      if (hpShoppingCart.hasJobs()) {
        // Copy HaynesPro Jobs
        final List<HaynesProCacheJobDto> jobs = Stream.of(hpShoppingCart.getJobs().getJob())
            .map(job -> SagBeanUtils.map(job, HaynesProCacheJobDto.class))
            .collect(Collectors.toList());
        smartCart.setJobs(jobs);
      }

      if (!hpShoppingCart.hasParts()) {
        log.warn("HaynesPro parts is empty");
        return smartCart;
      }

      // Copy HaynesPro Parts
      final List<HaynesProCachePartDto> parts = Stream.of(hpShoppingCart.getParts().getPart())
          .map(part -> SagBeanUtils.map(part, HaynesProCachePartDto.class))
          .collect(Collectors.toList());
      smartCart.setParts(parts);
      return smartCart;
    };
  }

  private Consumer<HaynesProCacheSmartCartDto> processHaynesProSmartCart(final String userKey,
      final String vehId) {
    return cart -> {
      log.info("Shopping Cart to save to cache = {}", SagJSONUtil.convertObjectToPrettyJson(cart));
      haynesProCacheService.saveHpSmartCart(userKey, cart);
      List<HaynesProCacheJobDto> hpJobs = cart.getJobs();
      if (!Objects.isNull(hpJobs)) {
        haynesProCacheService.saveLabourTimes(userKey, cart.getVehicle(), vehId, hpJobs);
      }
    };
  }

  @Override
  public HaynesProClientResponse getHaynesProResponse(String userKey,
      String vehicleId) throws ServiceException {

    final Optional<HaynesProCacheSmartCartDto> smartCartOpt =
        haynesProCacheService.getHpSmartCart(userKey);

    final HaynesProClientResponse response = smartCartOpt
        .map(cart -> HaynesProClientResponse.of(vehicleId, cart))
        .orElse(HaynesProClientResponse.empty());
    if (response.isEmpty()) {
      return response;
    }

    final Set<String> genArtIds = response.getGenArts();
    final List<String> oilGaIds = ivdsOilSerchService.extractOilGenericArticleIds(genArtIds);
    if (!CollectionUtils.isEmpty(oilGaIds)) {
      final Optional<VehicleDto> vehicleOpt =
          ivdsVehicleService.searchVehicleByVehId(response.getVehicleId());
      Optional.ofNullable(
          ivdsOilSerchService.getOilTypesByVehicleId(vehicleOpt, oilGaIds, Collections.emptyList()))
      .filter(CollectionUtils::isNotEmpty)
      .ifPresent(response::setOilTypeIdsDtos);
    }

    // Clear cache on HaynesPro data return after use.
    haynesProCacheService.clearSmartCart(userKey);
    return response;
  }
}
