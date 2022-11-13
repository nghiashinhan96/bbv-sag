package com.sagag.services.ax.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.ax.domain.AxAvailability;
import com.sagag.services.ax.translator.AxSendMethodTranslator;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.domain.sag.erp.Availability;

/**
 * Mapper class to combine AX availability response with e-connect availabilty.
 *
 */
@Component
@AxProfile
public class AxAvailabilityConverter {

  @Autowired
  private AxSendMethodTranslator axSendMethodTranslator;

  public Map<String, List<Availability>> convert(List<AxAvailability> availabilities) {
    if (CollectionUtils.isEmpty(availabilities)) {
      return Collections.emptyMap();
    }

    Map<String, List<Availability>> availabilitiesMap = new HashMap<>();
    String articleId;
    for (AxAvailability axAvailability : availabilities) {
      if (!axAvailability.isValid()) {
        continue;
      }
      articleId = axAvailability.getArticleId();
      availabilitiesMap.compute(articleId, groupAvailabilities(axAvailability));
    }
    return availabilitiesMap;
  }

  private BiFunction<String, List<Availability>, List<Availability>> groupAvailabilities(
      AxAvailability axAvailability) {
    return (articleId, availabilities) -> {
      if (axAvailability == null) {
        return null;
      }
      final Availability availability = axAvailability.toAvailabilityDto();
      availability.setSendMethodCode(
          axSendMethodTranslator.translateToConnect(availability.getSendMethodCode()));

      // Initial case the availabilities always is null
      if (availabilities == null) {
        availabilities = new ArrayList<>();
      }
      availabilities.add(availability);
      return availabilities;
    };
  }

}
