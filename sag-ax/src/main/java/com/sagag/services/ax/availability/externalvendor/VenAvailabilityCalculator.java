package com.sagag.services.ax.availability.externalvendor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.availability.calculator.ExternalSourceTourArrivalTimeCalculator;
import com.sagag.services.ax.availability.tourtime.NextWorkingDateHelper;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;
import com.sagag.services.ax.domain.vendor.VendorDeliveryInfo;
import com.sagag.services.ax.enums.SendMethod;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.sag.erp.Availability;

public abstract class VenAvailabilityCalculator {

  @Autowired(required = false)
  private ExternalSourceTourArrivalTimeCalculator tourCalculator;

  @Autowired(required = false)
  protected NextWorkingDateHelper nextWorkingDateHelper;

  protected static final String WORKING_DAY_CODE = "WORKING_DAY";

  public abstract boolean isSupportedType(String availabilityTypeId);

  /**
   * Returns the list of virtual warehouse case.
   *
   */
  public abstract List<AxAvailabilityType> virtualWarehouseAvailabilityTypes();

  public abstract Date firstHandleOfExternalStockDeliveryTime(ArticleSearchCriteria artCriteria,
      String countryName, VendorDeliveryInfo info);

  public abstract List<Availability> calculatePotentialAvailabilities(ArticleDocDto article,
      ArticleSearchCriteria artCriteria, Date vendorDeliveryTime, Integer returnedQuantity,
      List<DeliveryProfileDto> list, String branchId, String countryName);

  /**
   * Calculates availability of articles.
   *
   * @param artCriteria
   * @param availCriteria
   * @param countryName
   * @param stockInfos
   * @return the calculated availabilities of articles.
   */
  public void calculateAvailability(ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, String countryName,
      ExternalStockInfo stockInfo) {
    fillUpAvailabilityBaseOnStockInfo(artCriteria, availCriteria, countryName, stockInfo);
  }

  protected void fillUpAvailabilityBaseOnStockInfo(ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, String countryName,
      ExternalStockInfo validStockInfo) {

    final ArticleDocDto article = validStockInfo.getArticle();
    final Integer requestedQuantity = article.getAmountNumber();

    List<VendorDeliveryInfo> vendorsDeliveryInfo = validStockInfo.getVendorsDeliveryInfo();
    if (CollectionUtils.isEmpty(vendorsDeliveryInfo)) {
      return;
    }
    vendorsDeliveryInfo.stream().map(info -> {
      final Integer returnedQuantity = findRequestedQuantity(requestedQuantity, info);

      Date afterFirstHandleStockDeliveryTime =
          firstHandleOfExternalStockDeliveryTime(artCriteria, countryName, info);

      List<Availability> potentialAvailabilities = calculatePotentialAvailabilities(article,
          artCriteria, afterFirstHandleStockDeliveryTime, returnedQuantity,
          validStockInfo.getDeliveryProfiles(), availCriteria.getBranchId(), countryName);
      flagAsVen(validStockInfo, potentialAvailabilities);
      if (StringUtils.equalsIgnoreCase(artCriteria.getDeliveryType(), SendMethod.TOUR.name())) {
        additionaCalculationTourCase(artCriteria, availCriteria, countryName, info,
            potentialAvailabilities);
      }

      Optional<Availability> earliestByVendor = potentialAvailabilities.stream()
          .sorted(Comparator.comparing(Availability::getArrivalTime)).findFirst();
      return earliestByVendor.orElse(null);
    }).filter(Objects::nonNull).sorted(Comparator.comparing(Availability::getArrivalTime)).findFirst()
        .ifPresent(ealierAvailability -> {
          List<Availability> avais = new ArrayList<>();
          avais.add(ealierAvailability);
          article.setAvailabilities(avais);
          article.setVenExternalAvailability(SerializationUtils.clone(ealierAvailability));
        });;
  }

  private void additionaCalculationTourCase(ArticleSearchCriteria artCriteria,
      AdditionalArticleAvailabilityCriteria availCriteria, String countryName,
      VendorDeliveryInfo info, List<Availability> venAvailabilities) {
    final Optional<Date> nextWorkingDateForToday = nextWorkingDateHelper.getNextWorkingDate(
        artCriteria.getNextWorkingDateForToday(), availCriteria.getOpeningHours());
    CollectionUtils.emptyIfNull(venAvailabilities)
        .forEach(ven -> tourCalculator.calculateArrivalTime(ven, nextWorkingDateForToday,
            artCriteria.getAffiliate(), info.getBranchId(), availCriteria.getTourTimeList(),
            availCriteria.getOpeningHours(), countryName));
  }

  private Integer findRequestedQuantity(final Integer requestedQuantity, VendorDeliveryInfo info) {
    final Integer returnedQuantity = Objects.nonNull(info.getReturnedQuantity())
        && info.getReturnedQuantity() < requestedQuantity ? info.getReturnedQuantity()
            : requestedQuantity;
    return returnedQuantity;
  }

  private final void flagAsVen(ExternalStockInfo item,
      final List<Availability> availabilities) {
    if (CollectionUtils.isEmpty(availabilities)) {
      return;
    }
    availabilities.forEach(avail -> {
      avail.setVendorId(Long.valueOf(item.getVendorId()));
      avail.setVenExternalSource(true);
      ExternalVendorDto externalVendor = item.getExternalVendor();
      avail.setVendorName(externalVendor.getVendorName());
      avail.setExternalVendorTypeId(
          Objects.nonNull(externalVendor) ? externalVendor.getAvailabilityTypeId() : null);
    });
  }

}
