package com.sagag.services.ax.availability.externalvendor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.eshop.repo.api.OpeningDaysCalendarRepository;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.domain.vendor.VendorStockDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria.ArticleSearchCriteriaBuilder;
import com.sagag.services.ax.availability.filter.AxAvailabilityFilter;
import com.sagag.services.domain.article.ArticleDocDto;

@RunWith(MockitoJUnitRunner.class)
public class MultipleExternalVendorsAvailabilityFilterTest
    extends AbstractExternalVendorAvailabilityFilterTest {

  @InjectMocks
  private VenExternalVendorAvailabilityFilter venExternalVendorAvailabilityFilter;

  @Spy
  private AxAvailabilityFilter axAvailabilityFilter;

  @Mock
  private ArticleExternalService articleExtService;

  @Mock
  private OpeningDaysCalendarRepository openingDaysRepo;

  @Test
  public void calculateArrivalTimeFromSingExternalVendor_typeVEN() {
    Map<Long, String> artAndVendorArt = new HashedMap<>();
    artAndVendorArt.put(1000480497L, "2457133");

    Map<String, Integer> artAndAmount = new HashMap<>();
    artAndAmount.put("1000480497", 5);

    VendorStockDto vendorStockDto = buildVendorStockDto(artAndVendorArt);
    String nowStr = "2021-11-17";
    vendorStockDto.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto.setCutoffTime(nowStr + "T23:00:00Z");
    Mockito.when(openingDaysRepo.findWorkingDayLaterFrom(Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.anyInt()))
    //2021-11-18
        .thenReturn(Optional.of(new Date(1637168400000l)));
    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto));

    ArticleSearchCriteriaBuilder articleSearchCriteriaBuilder =
        buildDefaultArticleSearchCriteria(false, "PICKUP").defaultBrandId("123")
            .pickupBranchId("123");
    List<ArticleDocDto> results = venExternalVendorAvailabilityFilter.filter(
        buildBasicArticleDocDtos(artAndAmount), articleSearchCriteriaBuilder.build(),
        buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);
    Assert.assertNotNull(results);
    Assert.assertThat(results.size(), Matchers.is(1));
    Assert.assertNotNull(results.get(0).getAvailabilities());
    Assert.assertThat(results.get(0).getAvailabilities().size(), Matchers.is(1));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getArrivalTime(), Matchers.equalTo("2021-11-18T04:00:00Z"));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getQuantity(), Matchers.equalTo(5));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).isVenExternalSource(), Matchers.equalTo(true));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getVendorId(), Matchers.equalTo(10002L));
  }

  @Test
  public void calculateArrivalTimeFromMultipleExternalVendor_thenSelectHigherPriority_typeVWH() {
    Map<Long, String> artAndVendorArt = new HashedMap<>();
    artAndVendorArt.put(1000480497L, "2457133");

    Map<String, Integer> artAndAmount = new HashMap<>();
    artAndAmount.put("1000480497", 5);

    VendorStockDto vendorStockDto = buildVendorStockDto(artAndVendorArt);
    String nowStr = "2021-11-17";
    vendorStockDto.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto.setCutoffTime(nowStr + "T23:00:00Z");
    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto));

    ArticleSearchCriteriaBuilder articleSearchCriteriaBuilder =
        buildDefaultArticleSearchCriteria(false, "PICKUP").defaultBrandId("123")
            .pickupBranchId("123");
    List<ArticleDocDto> articles = buildBasicArticleDocDtos(artAndAmount);
    articles.get(0).setSagProductGroup("1-11");
    List<ArticleDocDto> results = venExternalVendorAvailabilityFilter.filter(
        articles, articleSearchCriteriaBuilder.build(),
        buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);
    Assert.assertNotNull(results);
    Assert.assertThat(results.size(), Matchers.is(1));
    Assert.assertNotNull(results.get(0).getAvailabilities());
    Assert.assertThat(results.get(0).getAvailabilities().size(), Matchers.is(1));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getArrivalTime(), Matchers.equalTo("2021-11-17T14:00:00Z"));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getQuantity(), Matchers.equalTo(5));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).isVenExternalSource(), Matchers.equalTo(true));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getVendorId(), Matchers.equalTo(10191L));
  }

  @Test
  public void calculateArrivalTimeFromMultipleExternalVendor_NoVendorHasEnoughThenGetTheVendorWithHighestQuantity_typeVEN() {
    Map<Long, String> artAndVendorArt = new HashedMap<>();
    artAndVendorArt.put(1000480497L, "2457133");

    Map<String, Integer> artAndAmount = new HashMap<>();
    int requestedQuantity = 5;
    artAndAmount.put("1000480497", requestedQuantity);

    VendorStockDto vendorStockDto1 = buildVendorStockDto(artAndVendorArt);
    VendorStockDto vendorStockDto2 = buildVendorStockDto(artAndVendorArt);
    int quantityLessThanRequested_1 = 1;
    int quantityLessThanRequested_2 = 2;
    updateQuantityForSpecificArtId(quantityLessThanRequested_1, 1000480497L, vendorStockDto2);
    updateQuantityForSpecificArtId(quantityLessThanRequested_2, 1000480497L, vendorStockDto2);
    String nowStr = "2021-11-17";
    vendorStockDto1.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto1.setCutoffTime(nowStr + "T23:00:00Z");
    vendorStockDto2.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto2.setCutoffTime(nowStr + "T23:00:00Z");
    Mockito.when(openingDaysRepo.findWorkingDayLaterFrom(Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.anyInt()))
        .thenReturn(Optional.of(new Date(1637168400000l)));    //2021-11-18
    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.eq("10002"), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto1));
    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.eq("10191"), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto2));

    ArticleSearchCriteriaBuilder articleSearchCriteriaBuilder =
        buildDefaultArticleSearchCriteria(false, "PICKUP").defaultBrandId("123")
            .pickupBranchId("123");
    List<ArticleDocDto> articles = buildBasicArticleDocDtos(artAndAmount);
    articles.get(0).setSagProductGroup("1-11");
    List<ArticleDocDto> results = venExternalVendorAvailabilityFilter.filter(
        articles, articleSearchCriteriaBuilder.build(),
        buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);
    Assert.assertNotNull(results);
    Assert.assertThat(results.size(), Matchers.is(quantityLessThanRequested_1));
    Assert.assertNotNull(results.get(0).getAvailabilities());
    Assert.assertThat(results.get(0).getAvailabilities().size(), Matchers.is(quantityLessThanRequested_1));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getArrivalTime(), Matchers.equalTo("2021-11-18T04:00:00Z"));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getQuantity(), Matchers.equalTo(requestedQuantity));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).isVenExternalSource(), Matchers.equalTo(true));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getVendorId(), Matchers.equalTo(10002L));
  }

  @Test
  public void calculateArrivalTimeFromMultipleExternalVendor_HighPriorityVendorHasNotEnoughThenGetTheEnoughFromNextPrioVendor_typeVWH() {
    Map<Long, String> artAndVendorArt = new HashedMap<>();
    artAndVendorArt.put(1000480497L, "2457133");

    Map<String, Integer> artAndAmount = new HashMap<>();
    artAndAmount.put("1000480497", 5);

    VendorStockDto vendorStockDto1 = buildVendorStockDto(artAndVendorArt);
    VendorStockDto vendorStockDto2 = buildVendorStockDto(artAndVendorArt);
    int quantityLessThanRequested = 1;
    updateQuantityForSpecificArtId(quantityLessThanRequested, 1000480497L, vendorStockDto2);
    String nowStr = "2021-11-17";
    vendorStockDto1.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto1.setCutoffTime(nowStr + "T23:00:00Z");
    vendorStockDto2.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto2.setCutoffTime(nowStr + "T23:00:00Z");

    Mockito.when(openingDaysRepo.findWorkingDayLaterFrom(Mockito.any(), Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.anyInt()))
        .thenReturn(Optional.of(new Date(1637168400000l)));    //2021-11-18
    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.eq("10002"), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto1));
    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.eq("10191"), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto2));

    ArticleSearchCriteriaBuilder articleSearchCriteriaBuilder =
        buildDefaultArticleSearchCriteria(false, "PICKUP").defaultBrandId("123")
            .pickupBranchId("123");
    List<ArticleDocDto> articles = buildBasicArticleDocDtos(artAndAmount);
    articles.get(0).setSagProductGroup("1-11");
    List<ArticleDocDto> results = venExternalVendorAvailabilityFilter.filter(
        articles, articleSearchCriteriaBuilder.build(),
        buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);
    Assert.assertNotNull(results);
    Assert.assertThat(results.size(), Matchers.is(1));
    Assert.assertNotNull(results.get(0).getAvailabilities());
    Assert.assertThat(results.get(0).getAvailabilities().size(), Matchers.is(1));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getArrivalTime(), Matchers.equalTo("2021-11-18T04:00:00Z"));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getQuantity(), Matchers.equalTo(5));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).isVenExternalSource(), Matchers.equalTo(true));
    Assert.assertThat(results.get(0).getAvailabilities().get(0).getVendorId(), Matchers.equalTo(10002L));
  }

  @Test
  public void vendorStockReturnQuantityZero_thenNoVENAvailability() {
    Map<Long, String> artAndVendorArt = new HashedMap<>();
    artAndVendorArt.put(1000480497L, "2457133");

    Map<String, Integer> artAndAmount = new HashMap<>();
    artAndAmount.put("1000480497", 5);

    VendorStockDto vendorStockDto = buildVendorStockDto(artAndVendorArt);
    int returnZeroQuantity = 0;
    updateQuantityForSpecificArtId(returnZeroQuantity, 1000480497L, vendorStockDto);
    String nowStr = "2021-11-17";
    vendorStockDto.setDeliveryDate(nowStr + "T14:00:00Z");
    vendorStockDto.setCutoffTime(nowStr + "T23:00:00Z");
    Mockito.when(articleExtService.searchVendorStock(Mockito.any(), Mockito.any(), Mockito.any(),
        Mockito.any())).thenReturn(Optional.of(vendorStockDto));

    ArticleSearchCriteriaBuilder articleSearchCriteriaBuilder =
        buildDefaultArticleSearchCriteria(false, "PICKUP").defaultBrandId("123")
            .pickupBranchId("123");
    List<ArticleDocDto> articles = buildBasicArticleDocDtos(artAndAmount);
    articles.get(returnZeroQuantity).setSagProductGroup("1-11");
    List<ArticleDocDto> results = venExternalVendorAvailabilityFilter.filter(
        articles, articleSearchCriteriaBuilder.build(),
        buildAdditionalArticleAvailabilityCriteria(), buildAxVendors(), StringUtils.EMPTY);
    Assert.assertNotNull(results);
    Assert.assertThat(results.size(), Matchers.is(1));
    Assert.assertNull(results.get(0).getAvailabilities());
  }

}
