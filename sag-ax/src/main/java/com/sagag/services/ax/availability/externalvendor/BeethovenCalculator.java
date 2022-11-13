package com.sagag.services.ax.availability.externalvendor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.eshop.repo.entity.OpeningDaysCalendar;
import com.sagag.eshop.repo.entity.WorkingDay;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.domain.vendor.VendorDeliveryInfo;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.common.profiles.DynamicAxProfile;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

@Component
@DynamicAxProfile
public class BeethovenCalculator extends VenAvailabilityCalculator {

  @Autowired
  private OpeningDaysCalendarRepository openingDaysCalendarRepository;

  @Override
  public boolean isSupportedType(String availabilityTypeId) {
    List<String> supportedTypes =
        Arrays.asList(AxAvailabilityType.VWO, AxAvailabilityType.VWH, AxAvailabilityType.VWI)
            .stream().map(Enum::name).collect(Collectors.toList());
    return supportedTypes.contains(availabilityTypeId);
  }

  @Override
  public List<AxAvailabilityType> virtualWarehouseAvailabilityTypes() {
    return Arrays.asList(AxAvailabilityType.VWO, AxAvailabilityType.VWH,
        AxAvailabilityType.VWI);
  }

  @Override
  public Date firstHandleOfExternalStockDeliveryTime(ArticleSearchCriteria artCriteria,
      String countryName, VendorDeliveryInfo info) {
    Date arrivalTime = info.getVendorDeliveryTime();
    Optional<OpeningDaysCalendar> connectOpeningCalendar =
        openingDaysCalendarRepository.findByDateAndCountryName(arrivalTime, countryName);
    if (!connectOpeningCalendar.isPresent()) {
      return arrivalTime;
    }
    WorkingDay workingDay = connectOpeningCalendar.get().getWorkingDay();
    if (workingDay == null) {
      return null;
    }
    if (StringUtils.equalsIgnoreCase(workingDay.getCode(), WORKING_DAY_CODE)) {
      return arrivalTime;
    }
    Optional<Date> nextWorkingDay = openingDaysCalendarRepository.findNextWorkingDay(arrivalTime,
        countryName, artCriteria.getCompanyName(), artCriteria.getPickupBranchId());
    if (!nextWorkingDay.isPresent()) {
      return null;
    }
    return nextWorkingDay.get();
  }

  @Override
  public List<Availability> calculatePotentialAvailabilities(ArticleDocDto article,
      ArticleSearchCriteria artCriteria, Date vendorDeliveryTime, Integer returnedQuantity,
      List<DeliveryProfileDto> deliveryProfile, String branchId, String countryName) {
    return Arrays.asList(AxArticleUtils.initAvailability(article, new DateTime(vendorDeliveryTime),
        artCriteria.getDeliveryType(), returnedQuantity));
  }

}
