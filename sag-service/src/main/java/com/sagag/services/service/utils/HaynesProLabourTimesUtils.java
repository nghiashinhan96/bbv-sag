package com.sagag.services.service.utils;

import com.sagag.eshop.service.dto.offer.OfferPositionDto;
import com.sagag.services.hazelcast.domain.haynespro.HaynesProCacheJobDto;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public class HaynesProLabourTimesUtils {

  public static Function<OfferPositionDto, HaynesProCacheJobDto> hpCacheJobConverter() {
    return offerPos -> {
      final HaynesProCacheJobDto hpJob = new HaynesProCacheJobDto();
      hpJob.setName(offerPos.getArticleDescription());
      hpJob.setAwNumber(offerPos.getAwNumber());
      hpJob.setTime(String.valueOf(offerPos.getQuantity()));
      hpJob.setLabourRate(String.valueOf(offerPos.getGrossPrice()));
      return hpJob;
    };
  }

  public static BiFunction<String, List<OfferPositionDto>, List<OfferPositionDto>> reMapper(
      OfferPositionDto offerPosition) {
    return (vehicleId, labourTimes) -> {
      if (labourTimes == null) {
        labourTimes = new ArrayList<>();
      }
      labourTimes.add(offerPosition);
      return labourTimes;
    };
  }

}
