package com.sagag.services.ax.availability.externalvendor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.springframework.stereotype.Component;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.domain.vendor.VendorDeliveryInfo;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@DynamicAxProfile
public class MozartCalculator extends VenAvailabilityCalculator {
  
  @Override
  public boolean isSupportedType(String availabilityTypeId) {
    List<String> supportedTypes =
        Arrays.asList(AxAvailabilityType.VEN).stream().map(Enum::name).collect(Collectors.toList());
    return supportedTypes.contains(availabilityTypeId);
  }

  @Override
  public List<AxAvailabilityType> virtualWarehouseAvailabilityTypes() {
    return Arrays.asList(AxAvailabilityType.VWH, AxAvailabilityType.VWI);
  }

  @Override
  public Date firstHandleOfExternalStockDeliveryTime(ArticleSearchCriteria artCriteria,
      String countryName, VendorDeliveryInfo info) {
    return info.getVendorDeliveryTime();
  }

  @Override
  public List<Availability> calculatePotentialAvailabilities(ArticleDocDto article,
      ArticleSearchCriteria artCriteria, Date vendorDeliveryTime, Integer returnedQuantity,
      List<DeliveryProfileDto> deliveryProfiles, String branchId, String countryName) {
    final SupportedAffiliate affiliate = artCriteria.getAffiliate();
    final String deliveryType = artCriteria.getDeliveryType();
    List<Availability> result = new ArrayList<>();
    CollectionUtils.emptyIfNull(deliveryProfiles).forEach(profile -> {
      final DateTime arrivalTime = calculateSingleVenAvailability(vendorDeliveryTime, profile,
          branchId, countryName, affiliate);
      if (arrivalTime != null) {
        result.add(
            AxArticleUtils.initAvailability(article, arrivalTime, deliveryType, returnedQuantity));
      }
    });
    return result;
  }

  private DateTime calculateSingleVenAvailability(Date vendorDeliveryTime,
      DeliveryProfileDto deliveryProfile, String branchId, String countryName,
      final SupportedAffiliate affiliate) {
    final Integer nextDay = deliveryProfile.getNextDay();
    final Integer deliveryDuration = deliveryProfile.getDeliveryDuration();
    final LocalTime latestTime = DateUtils.toLocalTime(deliveryProfile.getLatestTime());
    final LocalTime lastDelivery = DateUtils.toLocalTime(deliveryProfile.getLastDelivery());

    final DateTime arrivalTime = deliveryProfile.deliverOverNight()
        ? deliverTakeOverNightToDistribution(vendorDeliveryTime, deliveryDuration, affiliate,
            branchId, latestTime, nextDay, countryName)
        : deliverSameDayToDistribution(vendorDeliveryTime, affiliate, branchId, deliveryDuration,
            latestTime, lastDelivery);

    log.debug("Ven calculated arrivalTime = {}", arrivalTime);
    return arrivalTime;
  }
  

  private DateTime deliverSameDayToDistribution(Date vendorDeliveryTime,
      final SupportedAffiliate affiliate, final String branchId, Integer deliveryDuration,
      LocalTime latestTime, LocalTime lastDelivery) {
    return lastDelivery.isAfter(new LocalTime(vendorDeliveryTime))
        ? caseVendorDeliveryDateBeforeDistributionLastDelivery(new DateTime(vendorDeliveryTime),
            deliveryDuration)
        : caseVendorDeliveryDateAfterDistributionLastDelivery(new DateTime(vendorDeliveryTime),
            deliveryDuration, affiliate, branchId, latestTime);
  }

  private DateTime deliverTakeOverNightToDistribution(Date vendorDeliveryTime,
      Integer deliveryDuration, SupportedAffiliate affiliate, String branchId, LocalTime latestTime,
      Integer nextDay, String countryName) {
    LocalTime time = latestTime.plusSeconds(deliveryDuration);
    Date nextWorkingDay = nextWorkingDateHelper.getNextWorkingDayByDays(vendorDeliveryTime,
        countryName, affiliate.getCompanyName(), branchId, nextDay - 1);

    return new DateTime(nextWorkingDay).withTime(time);
  }

  private DateTime caseVendorDeliveryDateBeforeDistributionLastDelivery(
      DateTime vendordDeliveryDate, Integer deliveryDuration) {
    LocalTime vendordDeliveryTime = vendordDeliveryDate.toLocalTime();
    LocalTime time = vendordDeliveryTime.plusSeconds(deliveryDuration);
    return vendordDeliveryDate.withTime(time);
  }

  private DateTime caseVendorDeliveryDateAfterDistributionLastDelivery(DateTime vendordDeliveryDate,
      Integer deliveryDuration, SupportedAffiliate affiliate, String branchId,
      LocalTime latestTime) {
    Date nextWorkingDate = nextWorkingDateHelper.getNextWorkingDate(affiliate.getCompanyName(),
        branchId, vendordDeliveryDate.toDate());
    LocalTime time = latestTime.plusSeconds(deliveryDuration);
    return new DateTime(nextWorkingDate).withTime(time);
  }
}
