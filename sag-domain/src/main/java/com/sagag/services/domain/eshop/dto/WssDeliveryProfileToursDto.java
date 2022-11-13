package com.sagag.services.domain.eshop.dto;

import com.sagag.services.common.enums.WssDeliveryProfileDay;
import com.sagag.services.common.enums.WeekDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WssDeliveryProfileToursDto implements Serializable {

  private static final String TOUR_DEPARTURE_TIME_NOT_AVAILABLE_TEXT = "-";

  private static final long serialVersionUID = 2330408389274265669L;

  private Integer id;

  private String supplierTourTime;

  private boolean isOverNight;

  private Integer pickupWaitDuration;

  private WssDeliveryProfileDay supplierTourDay;

  private WssTourDto wssTour;

  public Integer getWssTourId() {
    return wssTour != null ? wssTour.getId() : null;

  }

  public String getTourName() {
    return wssTour != null ? wssTour.getName() : null;
  }
  public String getTourDepartureTime() {
    if (wssTour == null
        || CollectionUtils.isEmpty(wssTour.getWssTourTimesDtos())) {
      return TOUR_DEPARTURE_TIME_NOT_AVAILABLE_TEXT;
    }
    if (WssDeliveryProfileDay.ALL.equals(supplierTourDay)) {
      return TOUR_DEPARTURE_TIME_NOT_AVAILABLE_TEXT;
    }
    WeekDay wssTourWeekDay =
        WeekDay.findByValue(supplierTourDay.getValue());
    if (isOverNight) {
      wssTourWeekDay = getNextDay(wssTourWeekDay);
    }
    final WeekDay selectedWeekDay = wssTourWeekDay;
    List<WssTourTimesDto> wssTourTimes = wssTour.getWssTourTimesDtos();
    WssTourTimesDto tourDay = wssTourTimes.stream()
        .filter(t -> selectedWeekDay.equals(t.getWeekDay())).findAny().orElse(null);
    if (tourDay != null) {
      return tourDay.getDepartureTime();
    }
    return TOUR_DEPARTURE_TIME_NOT_AVAILABLE_TEXT;
  }

  private WeekDay getNextDay(WeekDay wssTourWeekDay) {
    return wssTourWeekDay.getValue() + 1 > WeekDay.SUNDAY.getValue()
        ? WeekDay.MONDAY
        : WeekDay.findByValue(wssTourWeekDay.getValue() + 1);
  }
}
