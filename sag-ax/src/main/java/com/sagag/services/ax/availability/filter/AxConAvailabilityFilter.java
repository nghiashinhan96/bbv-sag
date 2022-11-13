package com.sagag.services.ax.availability.filter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sagag.services.article.api.availability.AvailabilityFilter;
import com.sagag.services.ax.availability.calculator.ArrivalTimeCalculator;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.WorkingHours;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.ax.utils.AxBranchUtils;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.AxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

@Component
@AxProfile
@Slf4j
public class AxConAvailabilityFilter implements AvailabilityFilter {

  @Autowired
  private NextWorkingDateHelper nextWorkingDateHelper;

  @Autowired
  private List<ArrivalTimeCalculator> arrivalTimeCalculators;

  @Override
  public List<Availability> filterAvailabilities(ArticleDocDto article,
      ArticleSearchCriteria artCriteria, List<TourTimeDto> tourTimeList,
      List<WorkingHours> openingHours, String countryName) {
    final AxConArticleSearchCriteria conArtSearchCriteria =
        (AxConArticleSearchCriteria) artCriteria;
    final ExternalStockInfo stockInfo = conArtSearchCriteria.getStockInfo();

    final Long vendorId = stockInfo.getVendorIdLong();
    final String vendorName = stockInfo.getVendorName();
    List<DeliveryProfileDto> deliveryProfiles = stockInfo.getDeliveryProfiles();
    if (CollectionUtils.isEmpty(deliveryProfiles)) {
      return Lists.newArrayList();
    }
    Optional<Availability> earliestAvailability =
        deliveryProfiles.stream().filter(DeliveryProfileDto::isValidConDelvieryProfile)
            .map(profile -> calculateSingleAvailabilityByDeliveryProfile(article, artCriteria,
                tourTimeList, openingHours, countryName, profile))
            .sorted(Comparator.comparing(Availability::getArrivalTime)).findFirst();
    List<Availability> result = new ArrayList<>();
    earliestAvailability.ifPresent(availability -> {
      availability.setVendorId(vendorId);
      availability.setVendorName(vendorName);
      availability.setExternalSource(true);
      availability.setConExternalSource(true);
      result.add(availability);

    });

    return result;
  }

  private Availability calculateSingleAvailabilityByDeliveryProfile(ArticleDocDto article,
      ArticleSearchCriteria artCriteria, List<TourTimeDto> tourTimeList,
      List<WorkingHours> openingHours, String countryName,
      final DeliveryProfileDto deliveryProfile) {

    final SupportedAffiliate affiliate = artCriteria.getAffiliate();
    final String companyName = affiliate.getCompanyName();
    final String deliveryType = artCriteria.getDeliveryType();
    final ErpSendMethodEnum sendMethod = ErpSendMethodEnum.valueOf(deliveryType);
    final String branchId = AxBranchUtils.defaultBranchId(sendMethod,
        artCriteria.getPickupBranchId(), artCriteria.getDefaultBrandId());

    Integer deliveryDuration = deliveryProfile.getDeliveryDuration();
    final LocalTime latestTime = DateUtils.toLocalTime(deliveryProfile.getLatestTime());
    final LocalTime cutOffTime = DateUtils.toLocalTime(deliveryProfile.getVendorCutOffTime());
    final Date now = Calendar.getInstance().getTime();
    final Integer nextDays = deliveryProfile.getNextDay();
    int nextWorkingDaysByCutOffTime =
        cutOffTime.isAfter(new LocalTime(now)) ? nextDays : nextDays + 1;

    DateTime arrivalTime = getArrivalTimeOfConVendor(latestTime, deliveryDuration,
        nextWorkingDaysByCutOffTime, companyName, branchId, countryName);

    log.debug("Con calculated arrivalTime = {}", arrivalTime);
    Integer initQuantity = article.getAmountNumber() - article.getBackorderFalseAmount();
    Availability availability =
        AxArticleUtils.initAvailability(article, arrivalTime, deliveryType, initQuantity);

    arrivalTimeCalculators.stream()
        .filter(calculator -> calculator.sendMethodMode().equals(sendMethod)).findFirst()
        .ifPresent(calculator -> calculator.calculateArrivalTime(availability, Optional.empty(),
            affiliate, branchId, tourTimeList, openingHours, countryName));
    return availability;
  }

  private DateTime getArrivalTimeOfConVendor(LocalTime latestTime, Integer deliveryDuration,
      int nextDay, String companyName, String branchId, String countryName) {
    Date now = Calendar.getInstance().getTime();
    Date nextWorkingDay = nextWorkingDateHelper
        .getNextWorkingDayByDays(now, countryName, companyName, branchId, nextDay);

    LocalTime time = latestTime.plusSeconds(deliveryDuration);

    return new DateTime(nextWorkingDay).withTime(time);
  }
}
