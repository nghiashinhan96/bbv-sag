package com.sagag.services.ax.availability.externalvendor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.ax.domain.vendor.ExternalStockInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

public abstract class AbstractExternalVendorAvailabilityFilter
    implements ExternalVendorAvailabilityFilter {

  private static final int DEFAULT_INVALID_BRANCH_ID = -1;
  protected static final String SOURCING_TYPE_KSO = "KSO";

  protected List<ExternalVendorDto> selectExternalVendorsByType(
      List<ExternalVendorDto> externalVendors) {
    return CollectionUtils.emptyIfNull(externalVendors).stream()
        .filter(externalVendor -> availabilityTypes()
            .contains(AxAvailabilityType.from(externalVendor.getAvailabilityTypeId())))
        .collect(Collectors.toList());
  }

  protected List<ExternalStockInfo> getStockInfos(List<ExternalVendorDto> extVendors,
      List<VendorDto> axVendors, List<ArticleDocDto> articles,
      List<DeliveryProfileDto> allDeliveryProfile, List<String> branchIds) {

    final Map<VendorDto, ExternalVendorDto> vendorsMap = new HashMap<>();
    axVendors.stream()
        .forEach(axVendor -> findExternalVendorByAxVendor(axVendor, extVendors, articles,
            allDeliveryProfile, branchIds)
                .ifPresent(externalVendor -> vendorsMap.put(axVendor, externalVendor)));

    return vendorsMap.entrySet().stream()
        .map(item -> buildStockInfo(articles, allDeliveryProfile, branchIds, item).orElse(null))
        .filter(Objects::nonNull).collect(Collectors.toList());
  }

  protected List<ExternalStockInfo> distinctExternalStockInfoIfNecessaryBaseOnCountry(
      final List<ExternalStockInfo> stockInfos) {
    final List<String> articleIds = stockInfos.stream()
        .map(stock -> stock.getArticle().getIdSagsys()).distinct().collect(Collectors.toList());

    return articleIds.stream().map(id -> findStockInfo(stockInfos, id).orElse(null))
        .filter(Objects::nonNull).collect(Collectors.toList());
  }

  private Optional<ExternalStockInfo> buildStockInfo(List<ArticleDocDto> articles,
      List<DeliveryProfileDto> allDeliveryProfile, List<String> branchIds,
      Entry<VendorDto, ExternalVendorDto> item) {
    final VendorDto axVendor = item.getKey();
    final ExternalVendorDto externalVendor = item.getValue();
    final ArticleDocDto foundArticle =
        articles.stream().filter(article -> article.getIdSagsys().equals(axVendor.getArticleId()))
            .findAny().orElse(null);
    if (Objects.isNull(foundArticle)) {
      return Optional.empty();
    }

    final List<DeliveryProfileDto> foundDeliveryProfiles =
        findDeliveryProfilesByDeliveryProfileIdAndBranchId(allDeliveryProfile,
            externalVendor.getDeliveryProfileId(), branchIds);
    if (Objects.isNull(foundDeliveryProfiles)) {
      return Optional.empty();
    }

    return Optional.of(ExternalStockInfo.builder()
        .vendor(axVendor).externalVendor(externalVendor)
        .article(foundArticle).deliveryProfiles(foundDeliveryProfiles)
        .build());
  }

  private Optional<ExternalStockInfo> findStockInfo(List<ExternalStockInfo> stockInfos, String id) {
    return stockInfos.stream().filter(item -> id.equals(item.getArticle().getIdSagsys()))
        .findFirst();
  }

  private Optional<ExternalVendorDto> findExternalVendorByAxVendor(VendorDto axVendor,
      List<ExternalVendorDto> externalVendors, List<ArticleDocDto> articles,
      List<DeliveryProfileDto> allDeliveryProfile, List<String> branchIds) {
    Optional<ArticleDocDto> articleOpt = articles.stream()
        .filter(item -> axVendor.getArticleId().equals(item.getIdSagsys())).findAny();
    if (!articleOpt.isPresent()) {
      return Optional.empty();
    }

    return externalVendors.stream()
        .filter(externalVendor -> matchExternalVendorByConditions(externalVendor, axVendor,
            articleOpt.get(), allDeliveryProfile, branchIds))
         .sorted(ExternalVendorUtils.compareByLayerRule())
        .findFirst();
  }

  private boolean matchExternalVendorByConditions(ExternalVendorDto externalVendor,
      VendorDto axVendor, ArticleDocDto article, List<DeliveryProfileDto> allDeliveryProfile,
      List<String> branchIds) {
    Long vendorId = axVendor.getVendorIdLong();
    final List<String> sagProductGroups =
        Arrays.asList(article.getSagProductGroup(), article.getSagProductGroup2(),
            article.getSagProductGroup3(), article.getSagProductGroup4());
    final String idDlnr = article.getIdDlnr();

    final List<DeliveryProfileDto> foundDeliveryProfiles =
        findDeliveryProfilesByDeliveryProfileIdAndBranchId(allDeliveryProfile,
            externalVendor.getDeliveryProfileId(), branchIds);

    return deliveryProfileMatchingCondition(externalVendor, foundDeliveryProfiles)
        && ExternalVendorUtils.matchVendorId(String.valueOf(vendorId), externalVendor.getVendorId())
        && ExternalVendorUtils.matchSagProductGroups(externalVendor.getSagArticleGroup(),
            sagProductGroups)
        && ExternalVendorUtils.matchBrandId(externalVendor.getBrandId(), idDlnr);
  }

  //PBI-6121: ignore delivery profile checking for VWH case
  private boolean deliveryProfileMatchingCondition(ExternalVendorDto externalVendor,
      List<DeliveryProfileDto> foundDeliveryProfile) {
    return CollectionUtils.isNotEmpty(foundDeliveryProfile) || (StringUtils
        .equals(externalVendor.getAvailabilityTypeId(), AxAvailabilityType.VWH.name()));
  }

  private List<DeliveryProfileDto> findDeliveryProfilesByDeliveryProfileIdAndBranchId(
      List<DeliveryProfileDto> allDeliveryProfile, Integer deliveryProfileId,
      List<String> branchIds) {
    return allDeliveryProfile.stream().filter(
        deliveryProfile -> Objects.equals(deliveryProfileId, deliveryProfile.getDeliveryProfileId())
            && branchIds.contains(
                Optional.ofNullable(deliveryProfile.getDeliveryBranchId())
                    .orElse(DEFAULT_INVALID_BRANCH_ID).toString()))
        .collect(Collectors.toList());
  }
}
