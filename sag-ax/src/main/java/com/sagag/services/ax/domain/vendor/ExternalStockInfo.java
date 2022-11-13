package com.sagag.services.ax.domain.vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalStockInfo {

  public static final int MISSING_VENDOR_E_PRIORITY_ = -1;
  private static final int INVALID_VENDOR_PRIORITY = 0;
  private static final int MAXIMUM_VENDOR_PRIORITY = 9;
  
  private ArticleDocDto article;
  private VendorDto vendor;
  private ExternalVendorDto externalVendor;
  private List<DeliveryProfileDto> deliveryProfiles;
  private List<VendorDeliveryInfo> vendorsDeliveryInfo;

  public String getVendorId() {
    return Optional.ofNullable(vendor).map(VendorDto::getVendorId).orElse(StringUtils.EMPTY);
  }

  public Long getVendorIdLong() {
    return Optional.ofNullable(vendor).map(VendorDto::getVendorIdLong).orElse(null);
  }

  public String getVendorName() {
    return Optional.ofNullable(vendor).map(VendorDto::getVendorName).orElse(StringUtils.EMPTY);
  }

  public String getExternalArticleId() {
    return Optional.ofNullable(vendor).map(VendorDto::getExternalArticleId)
        .orElse(StringUtils.EMPTY);
  }

  public String getArticleId() {
    return Optional.ofNullable(article).map(ArticleDocDto::getIdSagsys).orElse(StringUtils.EMPTY);
  }

  public String getAvailabilityTypeid() {
    return Optional.ofNullable(externalVendor).map(ExternalVendorDto::getAvailabilityTypeId)
        .orElse(null);
  }

  public boolean isInValidVenItem() {
    return CollectionUtils.emptyIfNull(vendorsDeliveryInfo).stream()
        .allMatch(item -> item.getReturnedQuantity() == null || item.getReturnedQuantity() == 0);
  }

  public boolean isValidVenItem() {
    return !isInValidVenItem();
  }

  public boolean isValidConItem() {
    return CollectionUtils.emptyIfNull(deliveryProfiles).stream()
        .anyMatch(deliveryProfile -> Objects.nonNull(deliveryProfile.getNextDay()));
  }
  
  public void addDeliveryProfile(VendorDeliveryInfo info) {
    if (CollectionUtils.isEmpty(this.vendorsDeliveryInfo)) {
      this.vendorsDeliveryInfo = new ArrayList<>();
      this.vendorsDeliveryInfo.add(info);
    } else {
      this.vendorsDeliveryInfo.add(info);
    }
  }

  public void updatePriority(Integer priority) {
    final Integer beUpdated = Optional.ofNullable(priority).orElse(INVALID_VENDOR_PRIORITY);
    if (beUpdated == MISSING_VENDOR_E_PRIORITY_) {
      return;
    }
    Optional.ofNullable(this.externalVendor).ifPresent(ext -> {
      ext.setVendorPriority(
          Math.min(MAXIMUM_VENDOR_PRIORITY, Math.max(INVALID_VENDOR_PRIORITY, beUpdated)));
    });
  }

  public boolean isValid() {
    return Objects.nonNull(article) && Objects.nonNull(vendor) && Objects.nonNull(externalVendor)
        && this.externalVendor.getVendorPriority() > INVALID_VENDOR_PRIORITY;
  }
}
