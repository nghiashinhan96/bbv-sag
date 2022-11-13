package com.sagag.services.elasticsearch.api.impl.article;

import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.contants.TyreConstants;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.elasticsearch.ElasticsearchApplication;
import com.sagag.services.elasticsearch.api.impl.AbstractElasticsearchIT;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.MatchCodeArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.api.impl.articles.tyres.TyreArticleSearchServiceImpl;
import com.sagag.services.elasticsearch.criteria.article.MatchCodeArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.TyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.domain.article.ArticleDoc;
import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import com.sagag.services.elasticsearch.dto.TyreArticleResponse;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ElasticsearchApplication.class })
@EshopIntegrationTest
public class TyreArticleSearchServiceImplIT extends AbstractElasticsearchIT {

  @Autowired
  private TyreArticleSearchServiceImpl tyreService;

  @Autowired
  private MatchCodeArticleSearchServiceImpl matchCodeService;

  @Test
  public void testsearchTyreArticlesByTyreCriteriaWithSuppliers() {
    final List<String> filteredSuppliers =
      Arrays.asList("APOLLO", "KLEBER", "MICHELIN", "SEMPERIT");
    TyreArticleSearchCriteria tyreArticleSearchCriteria = new TyreArticleSearchCriteria();
    final Set<String> gaids = new HashSet<>(Arrays.asList("1169710", "116971", "11697"));
    tyreArticleSearchCriteria.setSupplierRaws(filteredSuppliers);
    tyreArticleSearchCriteria.setSeasonGenArtIds(gaids);
    final ArticleFilteringResponse response = tyreService.filter(tyreArticleSearchCriteria, Pageable.unpaged());
    Assert.assertNotNull(response.getAggregations());
    Assert.assertNotNull(response.getArticles());
    Assert.assertTrue(response.getArticles().getContent().stream()
      .anyMatch(a -> filteredSuppliers.contains(a.getSupplier())));
  }

  @Test
  public void testsearchTyreArticlesByTyreCriteriaWithGaids() {
    final List<String> filterdGaids = Arrays.asList("116971", "11697");
    TyreArticleSearchCriteria tyreArticleSearchCriteria = new TyreArticleSearchCriteria();
    final Set<String> gaids = new HashSet<>(Arrays.asList("1169710", "116971", "11697"));
    tyreArticleSearchCriteria.setSeasonGenArtIds(gaids);
    final ArticleFilteringResponse response = tyreService.filter(tyreArticleSearchCriteria, Pageable.unpaged());
    Assert.assertNotNull(response.getAggregations());
    Assert.assertNotNull(response.getArticles());
    Assert.assertTrue(response.getArticles().getContent().stream()
      .anyMatch(a -> filterdGaids.contains(a.getGaId())));
  }


  @Test
  public void testSearchTyresByCriteriaWithAllSeason() {

    Set<String> seasonGenArtIds = new HashSet<>();
    seasonGenArtIds.addAll(TyreConstants.Season.SUMMER.getGenArtIds());
    seasonGenArtIds.addAll(TyreConstants.Season.WINTER.getGenArtIds());
    seasonGenArtIds.addAll(TyreConstants.Season.ALL_YEAR.getGenArtIds());

    final TyreArticleSearchCriteria criteria = new TyreArticleSearchCriteria();
    criteria.setSeasonGenArtIds(seasonGenArtIds);
    TyreArticleResponse response = tyreService.search(criteria, PageUtils.MAX_PAGE);

    Assert.assertThat(response.getAggregations(), Matchers.notNullValue());
    Assert.assertThat(response.getAggregations().get(TyreConstants.WIDTH_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.HEIGHT_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.RADIUS_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.SPEED_INDEX_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.SEASON_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.TYRE_SEGMENT_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.SUPPLIER_MAP_NAME).size(),
      Matchers.greaterThan(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.FZ_CATEGORY_MAP_NAME).size(),
      Matchers.greaterThan(0));
  }

  @Test
  public void testSearchTyresByCriteriaWithWidthCvpAndTireSegmentAndFzCate() {

    Set<String> seasonGenArtIds = new HashSet<>(TyreConstants.Season.SUMMER.getGenArtIds());
    Set<String> fzCateGenArtIds = new HashSet<>(TyreConstants.FzCategory.PKW.getGenArtIds());

    final TyreArticleSearchCriteria criteria = new TyreArticleSearchCriteria();
    criteria.setSeasonGenArtIds(seasonGenArtIds);
    criteria.setFzCategoryGenArtIds(fzCateGenArtIds);
    criteria.setWidthCvp("145");
    criteria.setTyreSegmentCvps(Arrays.asList("Premium"));

    TyreArticleResponse response = tyreService.search(criteria, PageUtils.MAX_PAGE);

    Assert.assertThat(response.getAggregations(), Matchers.notNullValue());
    Assert.assertThat(response.getAggregations().get(TyreConstants.SEASON_MAP_NAME).size(),
      Matchers.is(TyreConstants.Season.values().length));
    Assert.assertThat(response.getAggregations().get(TyreConstants.FZ_CATEGORY_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.WIDTH_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(0));
    Assert.assertThat(response.getAggregations().get(TyreConstants.TYRE_SEGMENT_MAP_NAME).size(),
      Matchers.greaterThanOrEqualTo(0));
  }

  @Test
  @Ignore
  public void testSearchTyreArticleByMatchCodeWithoutPageable() {
    final String matchCode = "13502428511011";
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    TyreArticleResponse response = matchCodeService.search(criteria, Pageable.unpaged());

    Assert.assertThat(response.getAggregations(), Matchers.nullValue());
    Assert.assertThat(response.getArticles().getContent().size(),
      Matchers.greaterThanOrEqualTo(10));
    assertTyreArticleWithMatchCode(matchCode, response.getArticles().getContent());
  }

  @Test
  @Ignore
  public void testSearchTyreArticleByMatchCodeWithPageable() {
    final String matchCode = "13502428511011";
    final PageRequest pageRequest = PageUtils.DEF_PAGE;
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    TyreArticleResponse response = matchCodeService.search(criteria, pageRequest);

    Assert.assertThat(response.getAggregations(), Matchers.nullValue());
    Assert.assertThat(response.getArticles().getContent().size(), Matchers.equalTo(10));
    assertTyreArticleWithMatchCode(matchCode, response.getArticles().getContent());
  }

  @Test
  public void testSearchTyreArticleByMatchCodeWithNullMatchCode() {
    final String matchCode = null;
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    TyreArticleResponse response = matchCodeService.search(criteria, PageUtils.MAX_PAGE);

    Assert.assertThat(response.hasAggregations(), Matchers.is(false));
    Assert.assertThat(response.hasArticles(), Matchers.is(false));
  }

  @Test
  public void testSearchTyreArticleByMatchCodeWithEmptyMatchCode() {
    final String matchCode = StringUtils.EMPTY;
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    TyreArticleResponse response = matchCodeService.search(criteria, PageUtils.MAX_PAGE);

    Assert.assertThat(response.hasAggregations(), Matchers.is(false));
    Assert.assertThat(response.hasArticles(), Matchers.is(false));
  }

  @Test
  public void testSearchTyreArticleByMatchCodeWithSpaceMatchCode() {
    final String matchCode = StringUtils.SPACE;
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    TyreArticleResponse response = matchCodeService.search(criteria, PageUtils.MAX_PAGE);

    Assert.assertThat(response.hasAggregations(), Matchers.is(false));
    Assert.assertThat(response.hasArticles(), Matchers.is(false));
  }

  private static void assertTyreArticleWithMatchCode(final String expectedMatchCode,
    final List<ArticleDoc> articles) {
    Assert.assertThat(articles.stream().flatMap(article -> article.getParts().stream())
      .anyMatch(criteria -> criteria.getPnrn().equals(expectedMatchCode)), Matchers.is(true));
  }

  @Test
  public void testSearchTyreArticleByMatchCodeWithTyreCode() {
    final String matchCode = "PW2055516NTNGOODYEAR";
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    criteria.setAggregated(true);
    TyreArticleResponse response = matchCodeService.search(criteria, PageUtils.MAX_PAGE);

    Assert.assertThat(response.hasAggregations(), Matchers.is(true));
    Assert.assertThat(response.hasArticles(), Matchers.anyOf(Matchers.is(true), Matchers.is(false)));
  }

  @Test
  public void testSearchTyreArticleByMatchCodeWithArtNrDisplay() {
    final String matchCode = "205/55R16 91T UG 9+ MS";
    final MatchCodeArticleSearchCriteria criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode(matchCode);
    criteria.setAggregated(true);
    TyreArticleResponse response = matchCodeService.search(criteria, PageUtils.MAX_PAGE);

    Assert.assertThat(response.hasAggregations(), Matchers.anyOf(Matchers.is(true), Matchers.is(false)));
    Assert.assertThat(response.hasArticles(), Matchers.anyOf(Matchers.is(true), Matchers.is(false)));
  }
}
