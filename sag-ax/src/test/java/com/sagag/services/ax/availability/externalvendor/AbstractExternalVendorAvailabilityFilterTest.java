package com.sagag.services.ax.availability.externalvendor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;

import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.domain.vendor.VendorStockItemDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ArticleSearchCriteria.ArticleSearchCriteriaBuilder;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

public abstract class AbstractExternalVendorAvailabilityFilterTest {

  public List<ArticleDocDto> buildBasicArticleDocDtos(Map<String, Integer> artAndAmount) {
    return MapUtils.emptyIfNull(artAndAmount).keySet().stream()
        .map(item -> buildSingleBasicArticleDocDto(item, artAndAmount.get(item)))
        .collect(Collectors.toList());
  }
  private ArticleDocDto buildSingleBasicArticleDocDto(String artId, int amount) {
    ArticleDocDto article = new ArticleDocDto();
    article.setArtid(artId);
    article.setIdSagsys(artId);
    article.setAmountNumber(amount);
    article.setIdUmsart("1000000009991932170");
    return article;
  }

  protected ArticleSearchCriteriaBuilder buildDefaultArticleSearchCriteria(boolean isCartMode,
      String deliveryType) {
    return ArticleSearchCriteria.builder().affiliate(SupportedAffiliate.DERENDINGER_AT)
        .companyName("Company A").deliveryType(deliveryType).defaultBrandId("1026")
        .pickupBranchId("1026").custNr("1111101").isCartMode(isCartMode)
        .externalVendors(externalVendorDtos()).deliveryProfiles(deliveryProfileDtos());
  }

  protected AdditionalArticleAvailabilityCriteria buildAdditionalArticleAvailabilityCriteria() {
    AdditionalArticleAvailabilityCriteria criteria = new AdditionalArticleAvailabilityCriteria(
        AxDataTestUtils.getDummyTourTimeList(), AxDataTestUtils.mockWorkingHours(), "1026");
    return criteria;
  }

  protected List<VendorDto> buildAxVendors() {
    List<VendorDto> vendors = new ArrayList<>();
    vendors.add(buildAxVendor("1000494876", "ZFR6T-11G", "859067"));
    vendors.add(buildAxVendor("1000480497", "1624837", "10191"));
    vendors.add(buildAxVendor("1000480497", "2457133", "10002"));
    vendors.add(buildAxVendor("1001823630", "3003483", "10002"));
    return vendors;
  }

  private VendorDto buildAxVendor(String artId, String extArtId, String vendorId) {
    VendorDto vendor = new VendorDto();
    vendor.setVendorId(vendorId);
    vendor.setExternalArticleId(extArtId);
    vendor.setArticleId(artId);
    return vendor;
  }

  protected List<ExternalVendorDto> externalVendorDtos() {
    List<ExternalVendorDto> extVendors = new ArrayList<>();
    extVendors.add(externalVendor(859067l, 1, 1, null, null, AxAvailabilityType.VEN));
    extVendors.add(externalVendor(859067l, 2, 1, null, null, AxAvailabilityType.VEN));
    extVendors.add(externalVendor(859067l, 2, 1, null, null, AxAvailabilityType.CON));
    extVendors.add(externalVendor(10191L, 1, 2, "1-11", null, AxAvailabilityType.VWH));
    extVendors.add(externalVendor(10191L, 1, 2, "1-13", null, AxAvailabilityType.VWH));
    extVendors.add(externalVendor(10002L, 1, 1, null, null, AxAvailabilityType.VEN));
    extVendors.add(externalVendor(10002L, 2, 1, "1-11", null, AxAvailabilityType.VEN));
    extVendors.add(externalVendor(10002L, 2, 1, "1-12", null, AxAvailabilityType.VEN));
    return extVendors;
  }

  private ExternalVendorDto externalVendor(Long vendorId, Integer vendorPriority,
      Integer deliveryProfileId, String sagArticleGroup, Long brandId,
      AxAvailabilityType availType) {
    final ExternalVendorDto extVendor = new ExternalVendorDto();
    extVendor.setAvailabilityTypeId(availType.name());
    extVendor.setVendorId(String.valueOf(vendorId));
    extVendor.setVendorPriority(vendorPriority);
    extVendor.setDeliveryProfileId(deliveryProfileId);
    extVendor.setBrandId(brandId);
    extVendor.setSagArticleGroup(sagArticleGroup);
    return extVendor;
  }

  private List<DeliveryProfileDto> deliveryProfileDtos() {
    List<DeliveryProfileDto> profiles = new ArrayList<>();

    DeliveryProfileDto profile1 = DeliveryProfileDto.builder()
        .deliveryProfileId(1).nextDay(2)
        .vendorCutOffTime(LocalTime.of(17, 0)).lastDelivery(LocalTime.of(16, 0)).latestTime(LocalTime.of(9, 0))
        .deliveryDuration(7200)
        .deliveryBranchId(1026).distributionBranchId(1020)
        .build();

    DeliveryProfileDto profile2 = DeliveryProfileDto.builder()
        .deliveryProfileId(1).nextDay(2)
        .vendorCutOffTime(LocalTime.of(17, 0)).lastDelivery(LocalTime.of(16, 0)).latestTime(LocalTime.of(9, 0))
        .deliveryDuration(7200)
        .deliveryBranchId(123).distributionBranchId(123)
        .build();

    DeliveryProfileDto profile3 = DeliveryProfileDto.builder()
        .deliveryProfileId(2).nextDay(2)
        .vendorCutOffTime(LocalTime.of(17, 0)).lastDelivery(LocalTime.of(16, 0)).latestTime(LocalTime.of(9, 0))
        .deliveryDuration(7200)
        .deliveryBranchId(123).distributionBranchId(123)
        .build();
    profiles.add(profile1);
    profiles.add(profile2);
    profiles.add(profile3);
    return profiles;
  }

  public VendorStockDto buildVendorStockDto(Map<Long, String> artAndExtArt) {
    VendorStockDto vendorStock = new VendorStockDto();

    final Map<String, VendorStockItemDto> stockItems = new HashMap<>();
    artAndExtArt.entrySet().forEach(item -> stockItems.put(item.getKey().toString(),
        buildVendorStockItem(item.getValue(), 10)));

    vendorStock.setVendorStockItems(stockItems);
    return vendorStock;
  }

  public VendorStockItemDto buildVendorStockItem(String extArticleId, int quantity) {
    VendorStockItemDto vStockItem = new VendorStockItemDto();
    vStockItem.setVendorArticleId(extArticleId);
    vStockItem.setQuantity(quantity);
    return vStockItem;
  }

  public void updateQuantityForSpecificArtId(int quantity, Long articleId,
      VendorStockDto vendorStock) {
    MapUtils.emptyIfNull(vendorStock.getVendorStockItems()).entrySet().forEach(item -> {
      if (Long.compare(Long.valueOf(item.getKey()), articleId) == 0) {
        item.getValue().setQuantity(quantity);
      }
    });
  }
}
