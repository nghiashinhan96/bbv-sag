package com.sagag.services.article.api.utils;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.ArticleAvailabilityResult;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

@UtilityClass
@Slf4j
public class ArticleApiUtils {

  protected static final String[] INCORRECT_ARRIVAL_TIME_VALUES = new String[] { "1900", "1970" };

  public boolean isValidArrivalTime(Availability availability) {
    return Stream.of(ArticleApiUtils.INCORRECT_ARRIVAL_TIME_VALUES)
        .noneMatch(fValue -> StringUtils.contains(availability.getArrivalTime(), fValue));
  }

  public static List<Availability> defaultAvailabilities(ArticleDocDto article, String deliveryType,
      ArticleAvailabilityResult defaultResult) {
    log.debug("Reset all avail when has no price or no availabilities");
    final Availability availability = new Availability();
    availability.setArticleId(article.getIdSagsys());
    availability.setQuantity(article.getAmountNumber());
    availability.setAvailState(defaultResult.getAvailablityStateCode());
    availability.setAvailStateColor(defaultResult.getAvailablityStateColor());
    availability.setSendMethodCode(getDefaultSendMethodName(deliveryType));

    return Lists.newArrayList(availability);
  }

  private static String getDefaultSendMethodName(final String deliveryType) {
    return StringUtils.isBlank(deliveryType) ? StringUtils.EMPTY
        : ErpSendMethodEnum.valueOf(deliveryType).name();
  }
}
