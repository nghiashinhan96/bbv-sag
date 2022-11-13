package com.sagag.services.ax.availability;

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
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.api.BranchOpeningTimeRepository;
import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.repo.entity.BranchOpeningTime;
import com.sagag.services.article.api.availability.StockAvailabilitiesFilter;
import com.sagag.services.article.api.executor.ArticleSearchCriteria;
import com.sagag.services.article.api.executor.ExternalVendorSearchExecutor;
import com.sagag.services.ax.availability.filter.impl.AxAvailabilitiesFilterContext;
import com.sagag.services.ax.availability.stock.ArticleIsNotOnStockAvailabilitiesFilter;
import com.sagag.services.ax.availability.stock.ArticleIsOnStockAvailabilitiesFilter;
import com.sagag.services.ax.utils.AxArticleUtils;
import com.sagag.services.domain.article.ArticleDocDto;

@RunWith(SpringRunner.class)
public class AvailabilitiesFilterContextTest {

  @InjectMocks
  private AxAvailabilitiesFilterContext context;

  @Spy
  private List<StockAvailabilitiesFilter> articleAvailabilitiesFilters = new ArrayList<>();

  @Mock
  private ArticleIsOnStockAvailabilitiesFilter articleIsOnStockAvailabilitiesFilter;

  @Mock
  private ArticleIsNotOnStockAvailabilitiesFilter articleIsNotOnStockAvailabilitiesFilter;

  @Mock
  private SupportedAffiliateRepository supportedAffRepo;

  @Mock
  private ExternalVendorSearchExecutor externalVendorSearchExecutor;

  @Mock
  private BranchOpeningTimeRepository branchOpeningTimeRepo;

  @Before
  public void init() {
    articleAvailabilitiesFilters.clear();
    articleAvailabilitiesFilters.add(articleIsOnStockAvailabilitiesFilter);
    articleAvailabilitiesFilters.add(articleIsNotOnStockAvailabilitiesFilter);
  }

  @Test
  public void shouldReturnedEmptyArticlesWhenAddEmptyOriginalArticles() {
    final List<ArticleDocDto> artiles = context.doFilterAvailabilities(Collections.emptyList(),
        AxAvailabilityDataProvider.buildSearchCriteriaForVendor());
    Assert.assertThat(artiles.isEmpty(), Matchers.is(true));
  }

  @Test
  public void shouldReturnedEmptyArticlesWhenAddNullCriteria() {
    final List<ArticleDocDto> artiles = context.doFilterAvailabilities(
        AxAvailabilityDataProvider.buildMockedArticlesStockForVendor(), null);
    Assert.assertThat(artiles.isEmpty(), Matchers.is(true));
  }

  @Test
  public void shouldReturnedArticles() {
    ArticleSearchCriteria criteria = AxAvailabilityDataProvider.buildSearchCriteriaForVendor();

    final List<ArticleDocDto> originalArticles =
        AxAvailabilityDataProvider.buildMockedArticlesStockForVendor();

    Mockito.when(supportedAffRepo.findCountryShortNameByAffShortName(Mockito.any()))
        .thenReturn("CH");
    Mockito.when(externalVendorSearchExecutor.execute(Mockito.any(), Mockito.any()))
    .thenReturn(Collections.emptyList());
    Mockito.when(AxArticleUtils
      .updateArticlesAvailability(Mockito.any(),Mockito.any()))
    .thenReturn(originalArticles);
    Mockito.when(branchOpeningTimeRepo.findByBranchNr(Mockito.any()))
    .thenReturn(Lists.newArrayList(new BranchOpeningTime()));
    Mockito.when(articleIsOnStockAvailabilitiesFilter.doFilterAvailabilities(Mockito.any(),
        Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(originalArticles);

    final List<ArticleDocDto> articles = context.doFilterAvailabilities(originalArticles, criteria);

    Assert.assertThat(articles.isEmpty(), Matchers.is(false));

    Mockito.verify(supportedAffRepo, Mockito.times(1))
        .findCountryShortNameByAffShortName(Mockito.any());
  }
}
