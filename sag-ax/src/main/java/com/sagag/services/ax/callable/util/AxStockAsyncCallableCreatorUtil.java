package com.sagag.services.ax.callable.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.common.enums.AvailabilityTypeEnum;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.sag.erp.ArticleStock;

public final class AxStockAsyncCallableCreatorUtil {

  private static final String OWN_SAG_VENDOR_ID = "0";

  public final static List<ArticleStock> findDeliverableStockByDistributedBranch(
      ArticleSearchCriteria criteria, List<ArticleStock> stocks, String branchId) {
    List<Integer> distributedBranchs = loadDistributedBranchFromDefaultBranch(branchId,
        criteria.getExternalVendors(), criteria.getDeliveryProfiles());
    if (CollectionUtils.isEmpty(distributedBranchs) || CollectionUtils.isEmpty(stocks)) {
      return new ArrayList<>();
    }

    return stocks.stream()
        .filter(stock -> distributedBranchs.contains(Integer.valueOf(stock.getBranchId())))
        .collect(Collectors.toList());
  }

  private static List<Integer> loadDistributedBranchFromDefaultBranch(final String branchId,
      final List<ExternalVendorDto> externalVendors,
      final List<DeliveryProfileDto> deliveryProfiles) {
    List<DeliveryProfileDto> matchedDeliveryProfiles = new ArrayList<>();
    Optional<ExternalVendorDto> ownSagVendorOpt = CollectionUtils.emptyIfNull(externalVendors)
        .stream().filter(matchingOwnSagVendor()).findFirst();
    if (ownSagVendorOpt.isPresent()) {
      matchedDeliveryProfiles = CollectionUtils.emptyIfNull(deliveryProfiles).stream()
          .filter(matchingDeliveryProfiles(branchId, ownSagVendorOpt)).collect(Collectors.toList());
    }
    return CollectionUtils.emptyIfNull(matchedDeliveryProfiles).stream()
        .filter(matchedDp -> Objects.nonNull(matchedDp.getDistributionBranchId()))
        .map(DeliveryProfileDto::getDistributionBranchId).collect(Collectors.toList());
  }

  private static Predicate<DeliveryProfileDto> matchingDeliveryProfiles(final String branchId,
      Optional<ExternalVendorDto> ownSagExternalVendorOpt) {
    return dp -> Objects.equals(dp.getDeliveryProfileId(),
        ownSagExternalVendorOpt.get().getDeliveryProfileId())
        && String.valueOf(dp.getDeliveryBranchId()).equals(branchId)
        && !String.valueOf(dp.getDistributionBranchId()).equals(branchId);
  }

  private static Predicate<ExternalVendorDto> matchingOwnSagVendor() {
    return ext -> StringUtils.isEmpty(ext.getSagArticleGroup()) && Objects.isNull(ext.getBrandId())
        && StringUtils.equals(OWN_SAG_VENDOR_ID, ext.getVendorId())
        && ext.getAvailabilityTypeId().equals(AvailabilityTypeEnum.OWN.name())
        && Objects.equals(0, ext.getDeliveryProfileId());
  }
}
