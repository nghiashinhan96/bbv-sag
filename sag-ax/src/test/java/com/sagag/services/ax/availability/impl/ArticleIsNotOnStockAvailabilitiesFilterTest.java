package com.sagag.services.ax.availability.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.availability.AdditionalArticleAvailabilityCriteria;
import com.sagag.services.article.api.domain.vendor.VendorDto;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ExternalVendorSearchExecutor;
import com.sagag.services.article.api.executor.callable.ErpCallableCreator;
import com.sagag.services.ax.AxDataTestUtils;
import com.sagag.services.ax.availability.AxAvailabilityDataProvider;
import com.sagag.services.ax.availability.externalvendor.VenExternalVendorAvailabilityFilter;
import com.sagag.services.ax.availability.externalvendor.ConExternalVendorAvailabilityFilter;
import com.sagag.services.ax.availability.externalvendor.ExternalVendorAvailabilityFilter;
import com.sagag.services.ax.availability.stock.ArticleIsNotOnStockAvailabilitiesFilter;
import com.sagag.services.ax.availability.stock.ArticleIsOnStockAvailabilitiesFilter;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.domain.article.ArticleDocDto;

@RunWith(MockitoJUnitRunner.class)
public class ArticleIsNotOnStockAvailabilitiesFilterTest {

  private static final String CH_COUNTRY = "CH";

  @InjectMocks
  private ArticleIsNotOnStockAvailabilitiesFilter filter;

  @Spy
  private List<ExternalVendorAvailabilityFilter> externalVendorAvailabilityFilters =
      new ArrayList<>();

  @Mock
  private ArticleExternalService articleExtService;

  @Mock
  private ExternalVendorSearchExecutor externalVendorExecutor;

  @Mock
  private VenExternalVendorAvailabilityFilter venExternalVendorAvailabilityFilter;

  @Mock
  private ConExternalVendorAvailabilityFilter conExternalVendorAvailabilityFilter;

  @Mock
  private ArticleIsOnStockAvailabilitiesFilter articleWithStockAvailabilitiesFilter;

  @Mock
  private ErpCallableCreator axAvailabilityAsyncCallableCreator;

  @Before
  public void init() {
    externalVendorAvailabilityFilters.clear();
    externalVendorAvailabilityFilters.add(conExternalVendorAvailabilityFilter);
    externalVendorAvailabilityFilters.add(venExternalVendorAvailabilityFilter);
  }

  @Test
  public void shouldReturnFilteredAvailabilitiesByVen() {
    final List<ArticleDocDto> articles =
        AxAvailabilityDataProvider.buildMockedArticlesNoStockForVendor();
    final ArticleSearchCriteria artCriteria =
        AxAvailabilityDataProvider.buildSearchCriteriaForVendor();
    final AdditionalArticleAvailabilityCriteria avaiCriteria =
        new AdditionalArticleAvailabilityCriteria(AxDataTestUtils.getDummyTourTimeList(),
            AxDataTestUtils.mockWorkingHours(), AxDataTestUtils.defaultBranchId());

    mockForVenCase(artCriteria);
    List<VendorDto> axVendors = new ArrayList<>();

    final List<ArticleDocDto> filteredArticles =
        filter.doFilterAvailabilities(articles, CH_COUNTRY, artCriteria, avaiCriteria, axVendors);

    Assert.assertThat(filteredArticles.isEmpty(), Matchers.is(false));

    assertVenFilterCalling();
  }

  @Test
  public void shouldReturnFilteredAvailabilitiesByCon() {
    final List<ArticleDocDto> articles =
        AxAvailabilityDataProvider.buildMockedArticlesNoStockForVendor();
    final ArticleSearchCriteria artCriteria =
        AxAvailabilityDataProvider.buildSearchCriteriaForVendor();
    final AdditionalArticleAvailabilityCriteria avaiCriteria =
        new AdditionalArticleAvailabilityCriteria(AxDataTestUtils.getDummyTourTimeList(),
            AxDataTestUtils.mockWorkingHours(), AxDataTestUtils.defaultBranchId());

    mockForConCase(artCriteria);
    List<VendorDto> axVendors = new ArrayList<>();
    final List<ArticleDocDto> filteredArticles =
        filter.doFilterAvailabilities(articles, CH_COUNTRY, artCriteria, avaiCriteria, axVendors);

    Assert.assertThat(filteredArticles.isEmpty(), Matchers.is(false));

    assertConFilterCalling();
  }

  @Test
  public void shouldReturnEmptyAvailabilities() {
    final List<ArticleDocDto> articles = Collections.emptyList();
    final ArticleSearchCriteria artCriteria =
        AxAvailabilityDataProvider.buildSearchCriteriaForVendor();
    final AdditionalArticleAvailabilityCriteria avaiCriteria =
        new AdditionalArticleAvailabilityCriteria(AxDataTestUtils.getDummyTourTimeList(),
            AxDataTestUtils.mockWorkingHours(), AxDataTestUtils.defaultBranchId());
    List<VendorDto> axVendors = new ArrayList<>();

    final List<ArticleDocDto> filteredArticles =
        filter.doFilterAvailabilities(articles, CH_COUNTRY, artCriteria, avaiCriteria, axVendors);

    Assert.assertThat(filteredArticles.isEmpty(), Matchers.is(true));
  }

  @Test
  public void shouldReturnEmptyVendors() {
    final List<ArticleDocDto> articles =
        AxAvailabilityDataProvider.buildMockedArticlesNoStockForVendor();
    final ArticleSearchCriteria artCriteria =
        AxAvailabilityDataProvider.buildSearchCriteriaForVendor();
    final AdditionalArticleAvailabilityCriteria avaiCriteria =
        new AdditionalArticleAvailabilityCriteria(AxDataTestUtils.getDummyTourTimeList(),
            AxDataTestUtils.mockWorkingHours(), AxDataTestUtils.defaultBranchId());

    mockForVenCase(artCriteria);
    List<VendorDto> axVendors = new ArrayList<>();
    final List<ArticleDocDto> filteredArticles =
        filter.doFilterAvailabilities(articles, CH_COUNTRY, artCriteria, avaiCriteria, axVendors);

    Assert.assertThat(filteredArticles.isEmpty(), Matchers.is(false));

    Mockito.verify(articleExtService, Mockito.times(0)).searchVendorStock(Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.any());
    assertVenFilterCalling();
  }

  @Test
  public void shouldReturnFilteredAvailabilitiesCalculatedByCurrentLogic() {
    final List<ArticleDocDto> articles =
        AxAvailabilityDataProvider.buildMockedArticlesNoStockForVendor();
    final ArticleSearchCriteria artCriteria =
        AxAvailabilityDataProvider.buildSearchCriteriaForVendor();
    final AdditionalArticleAvailabilityCriteria avaiCriteria =
        new AdditionalArticleAvailabilityCriteria(AxDataTestUtils.getDummyTourTimeList(),
            AxDataTestUtils.mockWorkingHours(), AxDataTestUtils.defaultBranchId());

    Mockito.when(venExternalVendorAvailabilityFilter.filter(Mockito.any(), Mockito.eq(artCriteria),
        Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

    Mockito.when(conExternalVendorAvailabilityFilter.filter(Mockito.any(), Mockito.eq(artCriteria),
        Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());

    Mockito.when(
        AxArticleUtils.updateArticlesAvailability(Mockito.any(), Mockito.any()))
        .thenReturn(articles);

    List<VendorDto> axVendors = new ArrayList<>();
    final List<ArticleDocDto> filteredArticles =
        filter.doFilterAvailabilities(articles, CH_COUNTRY, artCriteria, avaiCriteria, axVendors);

    Assert.assertThat(filteredArticles.isEmpty(), Matchers.is(false));

    Mockito.verify(articleExtService, Mockito.times(0)).searchVendorStock(Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.any());
    assertVenFilterCalling();
  }

  @Test
  public void shouldReturnExtractedArticles() {
    final List<ArticleDocDto> articles = filter
        .extractBeforeCalculation(AxAvailabilityDataProvider.buildMockedArticlesStockForVendor());
    Assert.assertThat(articles.isEmpty(), Matchers.is(false));
    Assert.assertThat(articles.size(), Matchers.is(2));
  }

  @Test
  public void shouldReturnEmptyAfterExtractedArticles() {
    final List<ArticleDocDto> articles = filter.extractBeforeCalculation(Collections.emptyList());
    Assert.assertThat(articles.isEmpty(), Matchers.is(true));
  }

  private void mockForVenCase(ArticleSearchCriteria artCriteria) {
    Mockito
        .when(venExternalVendorAvailabilityFilter.filter(Mockito.any(), Mockito.eq(artCriteria),
            Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(AxAvailabilityDataProvider.buildArticles());

    Mockito.when(conExternalVendorAvailabilityFilter.filter(Mockito.any(), Mockito.eq(artCriteria),
        Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());
  }

  private void mockForConCase(ArticleSearchCriteria artCriteria) {
    Mockito
        .when(conExternalVendorAvailabilityFilter.filter(Mockito.any(), Mockito.eq(artCriteria),
            Mockito.any(), Mockito.any(), Mockito.any()))
        .thenReturn(AxAvailabilityDataProvider.buildArticles());

    Mockito.when(venExternalVendorAvailabilityFilter.filter(Mockito.any(), Mockito.eq(artCriteria),
        Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());
  }

  private void assertVenFilterCalling() {
    Mockito.verify(venExternalVendorAvailabilityFilter, Mockito.atLeast(1)).filter(Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
  }

  private void assertConFilterCalling() {
    Mockito.verify(conExternalVendorAvailabilityFilter, Mockito.atLeast(1)).filter(Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
  }
}
