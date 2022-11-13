package com.sagag.services.article.api.executor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.sagag.services.article.api.domain.article.AdditionalSearchCriteria;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.enums.PriceDisplayTypeEnum;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.NextWorkingDates;
import com.sagag.services.domain.eshop.dto.TourTimeDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.sag.external.CustomerApprovalType;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.domain.sag.external.GrantedBranch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class to provide the articles info request when execute update articles info from ERP.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSearchCriteria implements Serializable {

  private static final long serialVersionUID = -1624824036689303492L;

  private SupportedAffiliate affiliate;

  private String custNr;

  private String companyName;

  private String defaultBrandId;

  private String availabilityUrl;

  private List<ArticleDocDto> articles;

  private VehicleDto vehicle;

  private String addressId;

  private String deliveryType;

  private boolean updatePrice;

  private boolean updateStock;

  private boolean updateAvailability;

  private boolean updateVendors;

  private boolean filterArticleBefore;

  private boolean isCartMode;

  private boolean isDvseMode;

  private boolean grossPrice;

  private boolean specialNetPriceArticleGroup;

  private boolean partialDelivery;

  private boolean isExcludeSubArticles;

  private String pickupBranchId;

  private NextWorkingDates nextWorkingDate;

  private List<ExternalVendorDto> externalVendors;

  private List<DeliveryProfileDto> deliveryProfiles;

  private List<CustomerApprovalType> custApprovalTypes;

  private List<TourTimeDto> customerTourTimes;

  private boolean isFetchAll;

  private PriceDisplayTypeEnum priceTypeDisplayEnum;

  /** External User Information for SOAP ERP */
  private String extUsername;

  private String extCustomerId;

  private String extLanguage;

  private String extSecurityToken;

  /** END External User Information for SOAP ERP */

  private String language;

  private boolean isFinalCustomerUser;

  private AdditionalSearchCriteria additional;

  private Integer finalCustomerMarginGroup;

  private Integer wssOrgId;

  private WssDeliveryProfileDto wssDeliveryProfile;

  private boolean isDropShipment;

  private double vatRate;

  private Integer wssMaxAvailabilityDayRange;

  private boolean wholeSalerHasNetPrice;

  private List<GrantedBranch> grantedBranches;

  private List<CustomerBranch> companyBranches;

  private boolean isFinalCustomerHasNetPrice;

  private List<VendorDto> vendors;

  private boolean calculateVatPriceRequired;

  private Integer finalCustomerOrgId;

  private String custDisposalNumber;

  private boolean allowShowPfandArticle;

  private Optional<Date> nextWorkingDateForToday;

  @JsonIgnore
  public String getDeliveryType() {
    if (deliveryType == null) {
      throw new IllegalArgumentException("The given send method must not be null");
    }
    if (isFinalCustomerUser && Objects.nonNull(wssDeliveryProfile)) {
      return ErpSendMethodEnum.TOUR.name();
    }
    return this.deliveryType;
  }

  @JsonIgnore
  public String getFinalUserDeliveryType() {
    if (deliveryType == null) {
      throw new IllegalArgumentException("The given send method must not be null");
    }
    return this.deliveryType;
  }

  public void addVendors(List<VendorDto> vendors) {
    this.vendors.addAll(ListUtils.defaultIfNull(vendors, Lists.newArrayList()));
  }

  @JsonIgnore
  public String getAffiliateShortName() {
    return Optional.ofNullable(this.affiliate).map(SupportedAffiliate::getAffiliate)
      .orElse(StringUtils.EMPTY);
  }
  
  public List<String> findCustomerBranchIds() {
    return CollectionUtils.emptyIfNull(this.getCustomerTourTimes()).stream()
        .map(TourTimeDto::getBranchId).distinct().collect(Collectors.toList());
  }
}
