package com.sagag.services.ivds.api.impl;

import com.sagag.eshop.repo.api.SupportedAffiliateRepository;
import com.sagag.eshop.service.api.EshopFavoriteService;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.article.api.builder.AdditionalCriteriaBuilder;
import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.contants.TyreConstants.MotorbikeCategory;
import com.sagag.services.common.contants.TyreConstants.Season;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.domain.article.ArticleFilteringResponseDto;
import com.sagag.services.domain.sag.erp.BulkArticleResult;
import com.sagag.services.elasticsearch.api.ArticleSearchService;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.MatchCodeArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.MotorArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.TyreArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.MotorTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.TyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;
import com.sagag.services.hazelcast.api.ContextService;
import com.sagag.services.hazelcast.api.FormatGaCacheService;
import com.sagag.services.hazelcast.api.GenArtCacheService;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.ivds.converter.article.ArticleConverter;
import com.sagag.services.ivds.executor.IvdsArticleTaskExecutors;
import com.sagag.services.ivds.filter.articles.FilterMode;
import com.sagag.services.ivds.filter.impl.CompositeArticleFilterContext;
import com.sagag.services.ivds.request.filter.ArticleFilterRequest;
import com.sagag.services.ivds.request.filter.MotorTyreArticleSearchRequest;
import com.sagag.services.ivds.request.filter.TyreArticleSearchRequest;
import com.sagag.services.ivds.response.CustomArticleResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static java.util.Collections.singletonList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * UT to verify {@link IvdsArticleServiceImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class IvdsArticleServiceImplTest {

  @InjectMocks
  private IvdsArticleServiceImpl ivdsArticleService;

  @Mock
  private ArticleSearchService articleSearchService;

  @Mock
  private TyreArticleResponse tyreArticleResponse;

  @Mock
  private Page<ArticleDoc> page;

  @Mock
  private ContextService contextService;

  @Mock
  private EshopContext eshopContext;

  @Mock
  private ArticleExternalService articleExternalService;

  @Mock
  private List<BulkArticleResult> articles;

  @Mock
  private GenArtCacheService genArtCacheService;

  @Mock
  private FormatGaCacheService formatGaCacheService;

  @Mock
  private UserInfo user;

  @Mock
  private SupportedAffiliateRepository supportedAffiliateRepo;

  @Mock
  private CompositeArticleFilterContext articleFilterContext;

  @Mock
  private TyreArticleSearchServiceImpl tyreArticleSearchService;

  @Mock
  private MatchCodeArticleSearchServiceImpl matchCodeArticleSearchService;

  @Mock
  private MotorArticleSearchServiceImpl motorArticleSearchService;

  @Mock
  private IvdsArticleTaskExecutors ivdsArticleTaskExecutors;

  @Mock
  private ArticleConverter articleConverter;

  @Mock
  private AdditionalCriteriaBuilder additionalCriBuilder;

  @Mock
  private EshopFavoriteService eshopFavoriteService;

  @Before
  public void setUp() {
    when(supportedAffiliateRepo.findFirstByShortName(any())).thenReturn(Optional.empty());
  }

  @Test
  public void testSearchArticlesByNumber() {
    // size = 0 is not yet happened in real life so i don't focus to test
    int amountNumber = 0;

    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria("50c9", ArrayUtils.EMPTY_STRING_ARRAY);
    when(articleSearchService.searchArticlesByNumber(criteria, PageUtils.DEF_PAGE))
        .thenReturn(Page.empty());

    amountNumber = 0;
    ivdsArticleService.searchArticlesByNumber(user, "50c9", amountNumber, PageUtils.DEF_PAGE, false);
    verify(articleSearchService, never()).searchArticleByNumberDeepLink("50c9", PageUtils.MAX_PAGE);

    amountNumber = 1;
    ivdsArticleService.searchArticlesByNumber(user, "50c9", amountNumber, PageUtils.DEF_PAGE, false);
    verify(articleSearchService, never()).searchArticleByNumberDeepLink("50c9", PageUtils.MAX_PAGE);
    verify(articleSearchService, atLeastOnce()).searchArticlesByNumber(criteria, PageUtils.DEF_PAGE);
    amountNumber = 12;
    ivdsArticleService.searchArticlesByNumber(user, "50c9", amountNumber, PageUtils.DEF_PAGE, false);
    verify(articleSearchService, never()).searchArticleByNumberDeepLink("50c9", PageUtils.MAX_PAGE);
    verify(articleSearchService, atLeastOnce()).searchArticlesByNumber(criteria, PageUtils.DEF_PAGE);
  }

  @Test
  public void testSearchTyreArticlesByRequestWithSummerSeason() throws Exception {

    final Season season = TyreConstants.Season.SUMMER;
    TyreArticleSearchRequest request =
        TyreArticleSearchRequest.builder().season(season.name()).build();

    TyreArticleSearchCriteria criteria = new TyreArticleSearchCriteria();
    criteria.setSeasonGenArtIds(season.getGenArtIds().stream().collect(Collectors.toSet()));
    TyreArticleResponse response =
        TyreArticleResponse.builder().articles(Page.empty())
            .aggregations(Collections.emptyMap()).build();

    when(tyreArticleSearchService.search(criteria, PageUtils.MAX_PAGE))
        .thenReturn(response);

    CustomArticleResponseDto responseDto =
        ivdsArticleService.searchTyreArticlesByRequest(user, request, PageUtils.MAX_PAGE);

    Assert.assertThat(responseDto.getTotalElements(),
        Matchers.greaterThanOrEqualTo(Long.valueOf(0)));

    verify(tyreArticleSearchService, times(1)).search(criteria, PageUtils.MAX_PAGE);
  }

  @Test
  public void testSearchTyreArticlesByRequestWithSummerSeasonHasArticles() {

    final PageRequest pageRq = PageUtils.DEF_PAGE;
    final Season season = TyreConstants.Season.SUMMER;
    final String widthCvp = "14.5";

    TyreArticleSearchRequest request =
        TyreArticleSearchRequest.builder().season(season.name()).width(widthCvp).build();
    final TyreArticleSearchCriteria criteria = new TyreArticleSearchCriteria();
    criteria.setWidthCvp(widthCvp);
    criteria.setSeasonGenArtIds(season.getGenArtIds().stream().collect(Collectors.toSet()));

    when(tyreArticleSearchService.search(criteria, pageRq))
        .thenReturn(tyreArticleResponse);
    when(tyreArticleResponse.totalArticles())
        .thenReturn(Long.valueOf(TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE));

    CustomArticleResponseDto responseDto =
        ivdsArticleService.searchTyreArticlesByRequest(user, request, pageRq);

    Assert.assertThat(responseDto.getTotalElements(),
        Matchers.equalTo(Long.valueOf(TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE)));

    verify(tyreArticleSearchService, times(1)).search(criteria, pageRq);
  }

  @Test
  public void testSearchTyreArticlesByRequestWithMatchCode() {

    final String matchCode = "13502428511011";
    TyreArticleSearchRequest request =
        TyreArticleSearchRequest.builder().matchCode(matchCode).build();

    TyreArticleResponse response =
        TyreArticleResponse.builder().articles(Page.empty())
            .aggregations(Collections.emptyMap()).build();

    when(matchCodeArticleSearchService.search(any(), eq(PageUtils.MAX_PAGE)))
        .thenReturn(response);

    CustomArticleResponseDto responseDto =
        ivdsArticleService.searchTyreArticlesByRequest(user, request, PageUtils.MAX_PAGE);

    Assert.assertThat(responseDto.getTotalElements(), Matchers.equalTo(Long.valueOf(0)));

    verify(matchCodeArticleSearchService, times(1)).search(any(),
      eq(PageUtils.MAX_PAGE));
  }

  @Test
  public void testSearchTyreArticlesByRequestWithMatchCodeHasArticles() throws Exception {

    final String matchCode = "13502428511011";
    final PageRequest pageRq = PageUtils.DEF_PAGE;
    TyreArticleSearchRequest request =
        TyreArticleSearchRequest.builder().matchCode(matchCode).build();

    when(matchCodeArticleSearchService.search(any(), eq(pageRq)))
        .thenReturn(tyreArticleResponse);
    when(tyreArticleResponse.totalArticles())
        .thenReturn(Long.valueOf(TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE));

    CustomArticleResponseDto responseDto =
        ivdsArticleService.searchTyreArticlesByRequest(user, request, pageRq);

    Assert.assertThat(responseDto.getTotalElements(),
        Matchers.equalTo(Long.valueOf(TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE)));

    verify(matchCodeArticleSearchService, times(1)).search(any(), eq(pageRq));
  }

  @Test
  public void testSearchMotorTyreArticlesByRequestWithCategoryHasArticles() {
    final PageRequest pageRq = PageUtils.DEF_PAGE;
    final MotorbikeCategory category = TyreConstants.MotorbikeCategory.OFFROAD;
    final String widthCvp = "14.5";

    MotorTyreArticleSearchRequest request =
        MotorTyreArticleSearchRequest.builder().category(category.name()).width(widthCvp).build();
    final MotorTyreArticleSearchCriteria criteria = new MotorTyreArticleSearchCriteria();
    criteria.setWidthCvp(widthCvp);
    criteria.setCategoryGenArtIds(category.getGenArtIds().stream().collect(Collectors.toSet()));

    when(motorArticleSearchService.search(criteria, pageRq))
        .thenReturn(tyreArticleResponse);
    when(tyreArticleResponse.totalArticles())
        .thenReturn(Long.valueOf(TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE));

    CustomArticleResponseDto responseDto =
        ivdsArticleService.searchMotorTyreArticlesByRequest(user, request, pageRq);

    Assert.assertThat(responseDto.getTotalElements(),
        Matchers.equalTo(Long.valueOf(TyreConstants.VIEW_TYRE_ARTICLES_MAX_PAGE_SIZE)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSearchArticlesByBarCode_EmptyCode() throws Exception {
    final String code = "";
    ivdsArticleService.searchArticlesByBarCode(user, code);
  }

  @Test
  public void testSearchArticlesByFilteringWithEANCode() throws Exception {

    final String code = "3165143377147";

    final ArticleFilterRequest request =
        ArticleFilterRequest.builder().keyword(code).filterMode(FilterMode.EAN_CODE.name()).build();

    when(articleFilterContext.execute(eq(user), eq(request), eq(PageUtils.DEF_PAGE), any()))
        .thenReturn(ArticleFilteringResponseDto.builder()
            .articles(Page.empty()).build());

    final ArticleFilteringResponseDto responseDto =
        ivdsArticleService.searchArticlesByFilteringRequest(user, request, PageUtils.DEF_PAGE);

    Assert.assertThat(responseDto.getArticles().getTotalElements(),
        Matchers.equalTo(Long.valueOf(0)));

    verify(articleFilterContext, times(1)).execute(eq(user), eq(request), eq(PageUtils.DEF_PAGE),
        any());
  }

  @Test
  public void shouldSearchArticlesByNumberSuccessfully() {
    // Given
    int amountNumber = 1;
    List<ArticleDoc> articles = singletonList(mock(ArticleDoc.class));
    Page<ArticleDoc> articleDocs = new PageImpl<>(articles);
    KeywordArticleSearchCriteria criteria =
        new KeywordArticleSearchCriteria("50c9", ArrayUtils.EMPTY_STRING_ARRAY);
    when(articleSearchService.searchArticlesByNumber(criteria, PageUtils.DEF_PAGE))
        .thenReturn(articleDocs);
    // When
    ivdsArticleService.searchArticlesByNumber(user, "50c9", amountNumber,
        PageUtils.DEF_PAGE, false);
    // Then
    verify(articleSearchService, never())
        .searchArticleByNumberDeepLink("50c9", PageUtils.MAX_PAGE);
    verify(articleSearchService, atLeastOnce())
        .searchArticlesByNumber(criteria, PageUtils.DEF_PAGE);
  }
}
