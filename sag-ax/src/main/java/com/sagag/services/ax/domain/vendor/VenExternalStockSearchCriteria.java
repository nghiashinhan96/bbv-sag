package com.sagag.services.ax.domain.vendor;

import java.util.List;

import com.sagag.services.article.api.domain.vendor.ExternalStockSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VenExternalStockSearchCriteria extends ExternalStockSearchCriteria {

  private List<ExternalVendorDto> externalVendors;
  private List<DeliveryProfileDto> deliveryProfiles;

  @Builder
  public VenExternalStockSearchCriteria(String companyName, String deliveryType,
      String pickupBranch, String defaultBranch, List<ExternalVendorDto> externalVendors,
      List<DeliveryProfileDto> deliveryProfiles) {
    super(companyName, deliveryType, pickupBranch, defaultBranch);
    this.externalVendors = externalVendors;
    this.deliveryProfiles = deliveryProfiles;
  }

  public ArticleSearchCriteria toArticleSearchCriteria() {
    return ArticleSearchCriteria.builder().companyName(getCompanyName())
        .deliveryType(getDeliveryType()).pickupBranchId(getPickupBranch())
        .defaultBrandId(getDefaultBranch()).externalVendors(externalVendors)
        .deliveryProfiles(deliveryProfiles).build();
  }
}
