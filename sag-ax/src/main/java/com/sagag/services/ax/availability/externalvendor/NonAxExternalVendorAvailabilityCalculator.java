package com.sagag.services.ax.availability.externalvendor;

import java.util.Date;
import java.util.List;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.domain.vendor.VendorDeliveryInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;

public class NonAxExternalVendorAvailabilityCalculator extends VenAvailabilityCalculator {

  @Override
  public boolean isSupportedType(String availabilityTypeId) {
    return false;
  }

  @Override
  public List<AxAvailabilityType> virtualWarehouseAvailabilityTypes() {
    return null;
  }

  @Override
  public Date firstHandleOfExternalStockDeliveryTime(ArticleSearchCriteria artCriteria,
      String countryName, VendorDeliveryInfo info) {
    return null;
  }

  @Override
  public List<Availability> calculatePotentialAvailabilities(ArticleDocDto article,
      ArticleSearchCriteria artCriteria, Date vendorDeliveryTime, Integer returnedQuantity,
      List<DeliveryProfileDto> deliveryProfile, String branchId, String countryName) {
    return null;
  }

}
