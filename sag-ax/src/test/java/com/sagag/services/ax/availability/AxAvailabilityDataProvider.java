package com.sagag.services.ax.availability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.domain.vendor.VendorStockItemDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.ax.availability.externalvendor.AxAvailabilityType;
import com.sagag.services.common.enums.SupportedAffiliate;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.DeliveryProfileDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;
import com.sagag.services.domain.sag.erp.ArticleStock;
import com.sagag.services.domain.sag.erp.Availability;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AxAvailabilityDataProvider {

  public List<ArticleDocDto> buildMockedArticlesNoStockForVendor() {
    List<ArticleDocDto> articles = new ArrayList<>();
    articles.add(buildArticleDoc("1000494876", NumberUtils.DOUBLE_ZERO));
    articles.add(buildArticleDoc("1000020807", NumberUtils.DOUBLE_ZERO));
    return articles;
  }

  public List<ArticleDocDto> buildMockedArticlesStockForVendor() {
    List<ArticleDocDto> articles = new ArrayList<>();
    articles.add(buildArticleDoc("1000494876", NumberUtils.DOUBLE_ZERO));
    articles.add(buildArticleDoc("1000020807", NumberUtils.DOUBLE_ZERO));
    articles.add(buildArticleDoc("1000020800", NumberUtils.DOUBLE_ONE));
    return articles;
  }

  private ArticleDocDto buildArticleDoc(final String articleId, final double stock) {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys(articleId);
    article.setAmountNumber(1);
    article.setStock(new ArticleStock(articleId, stock));
    return article;
  }

  public ArticleSearchCriteria buildSearchCriteriaForVendor() {
    return ArticleSearchCriteria.builder().affiliate(SupportedAffiliate.DERENDINGER_AT)
        .companyName(SupportedAffiliate.DERENDINGER_AT.getCompanyName()).pickupBranchId("1001")
        .externalVendors(buildExternalVendors()).deliveryProfiles(buildDeliveryProfiles())
        .deliveryType("TOUR").isDropShipment(true).customerTourTimes(Collections.emptyList())
        .build();
  }

  public List<ExternalVendorDto> buildExternalVendors() {
    List<ExternalVendorDto> extVendors = new ArrayList<>();
    extVendors.add(buildExternalVendor(AxAvailabilityType.VEN));
    extVendors.add(buildExternalVendor(AxAvailabilityType.VEN));
    extVendors.add(buildExternalVendor(AxAvailabilityType.CON));
    extVendors.add(buildExternalVendor(AxAvailabilityType.VWH));
    return extVendors;
  }

  private ExternalVendorDto buildExternalVendor(AxAvailabilityType availType) {
    final ExternalVendorDto extVendor = new ExternalVendorDto();
    extVendor.setAvailabilityTypeId(availType.name());
    extVendor.setVendorId("859067");
    extVendor.setVendorPriority(1);
    return extVendor;
  }

  private List<DeliveryProfileDto> buildDeliveryProfiles() {
    List<DeliveryProfileDto> deliveryProfiles = new ArrayList<>();
    deliveryProfiles.add(buildDeliveryProfile());
    return deliveryProfiles;
  }

  private DeliveryProfileDto buildDeliveryProfile() {
    DeliveryProfileDto dProfile = new DeliveryProfileDto();
    return dProfile;
  }

  public List<VendorDto> buildVendors() {
    List<VendorDto> vendors = new ArrayList<>();
    vendors.add(buildVendor("1000494876", "ZFR6T-11G", "859067"));
    vendors.add(buildVendor("1000020807", "ZFR5P-G", "859067"));
    return vendors;
  }

  private VendorDto buildVendor(String artId, String extArtId, String vendorId) {
    VendorDto vendor = new VendorDto();
    vendor.setVendorId(vendorId);
    vendor.setExternalArticleId(extArtId);
    vendor.setArticleId(artId);
    return vendor;
  }

  public VendorStockDto buildVendorStock() {
    VendorStockDto vendorStock = new VendorStockDto();

    final Map<String, VendorStockItemDto> stockItems = new HashMap<>();
    stockItems.put("1000494876", buildVendorStockItem("ZFR6T-11G"));

    vendorStock.setVendorStockItems(stockItems);
    return vendorStock;
  }

  public VendorStockItemDto buildVendorStockItem(String extArticleId) {
    VendorStockItemDto vStockItem = new VendorStockItemDto();
    vStockItem.setVendorArticleId(extArticleId);
    vStockItem.setQuantity(10);
    return vStockItem;
  }

  public List<Availability> buildAvailabilities() {
    List<Availability> avails = new ArrayList<>();
    avails.add(buildAvailability());
    return avails;
  }

  private Availability buildAvailability() {
    Availability availability = new Availability();

    return availability;
  }


  public List<ArticleDocDto> buildArticles() {
    List<ArticleDocDto> articles = new ArrayList<>();

    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1000494876");
    article.setAvailabilities(buildAvailabilities());
    articles.add(article);
    return articles;
  }

}
