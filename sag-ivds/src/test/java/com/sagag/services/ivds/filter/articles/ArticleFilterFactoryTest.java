package com.sagag.services.ivds.filter.articles;

import com.sagag.services.ivds.filter.articles.impl.AccessoryListArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.ArticleNumberFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.BatteryArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.BublsArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.EanCodeArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.FreetextArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.IdPimArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.MatchCodeArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.MotorTyresArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.OilArticleFilterImpl;
import com.sagag.services.ivds.filter.articles.impl.TyresArticleFilterImpl;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * UT for {@link com.sagag.services.ivds.filter.articles.ArticleFilterFactory}.
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class ArticleFilterFactoryTest {

  @InjectMocks
  private ArticleFilterFactory factory;

  @Mock
  private ArticleNumberFilterImpl artNrFilterImpl;

  @Mock
  private BatteryArticleFilterImpl batteryArtFilterImpl;

  @Mock
  private BublsArticleFilterImpl bublsArtFilterImpl;

  @Mock
  private EanCodeArticleFilterImpl eanCodeArtFilterImpl;

  @Mock
  private FreetextArticleFilterImpl freetextArtFilterImpl;

  @Mock
  private IdPimArticleFilterImpl idPimArticleFilterImpl;

  @Mock
  private MatchCodeArticleFilterImpl matchCodeArtFilterImpl;

  @Mock
  private MotorTyresArticleFilterImpl mtTyresArtFilterImpl;

  @Mock
  private OilArticleFilterImpl oilArticleFilterImpl;

  @Mock
  private TyresArticleFilterImpl tyresArticleFilterImpl;
  
  @Mock
  private AccessoryListArticleFilterImpl accessoryListArticleFilterImpl;

  @Mock
  private List<IArticleFilter> articleFilters;

  @Before
  public void setup() {
    articleFilters = new ArrayList<>();
    articleFilters.add(artNrFilterImpl);
    articleFilters.add(batteryArtFilterImpl);
    articleFilters.add(bublsArtFilterImpl);
    articleFilters.add(eanCodeArtFilterImpl);
    articleFilters.add(freetextArtFilterImpl);
    articleFilters.add(idPimArticleFilterImpl);
    articleFilters.add(matchCodeArtFilterImpl);
    articleFilters.add(mtTyresArtFilterImpl);
    articleFilters.add(oilArticleFilterImpl);
    articleFilters.add(tyresArticleFilterImpl);
    articleFilters.add(accessoryListArticleFilterImpl);
  }

  @Test
  public void givenArticleNr_shouldReturnFilter() {
    final IArticleFilter filter = factory.getArticleFilter(FilterMode.ARTICLE_NUMBER);
    Assert.assertThat(filter, Matchers.notNullValue());
  }

  @Test
  public void givenIdSagsys_shouldReturnFilter() {
    final IArticleFilter filter = factory.getArticleFilter(FilterMode.ID_SAGSYS);
    Assert.assertThat(filter, Matchers.notNullValue());
  }

  @Test
  public void givenFreetext_shouldReturnFilter() {
    final IArticleFilter filter = factory.getArticleFilter(FilterMode.FREE_TEXT);
    Assert.assertThat(filter, Matchers.notNullValue());
  }

  @Test
  public void givenTyresSearch_shouldReturnFilter() {
    final IArticleFilter filter = factory.getArticleFilter(FilterMode.TYRES_SEARCH);
    Assert.assertThat(filter, Matchers.notNullValue());
  }

  @Test
  public void givenMotorTyresSearch_shouldReturnFilter() {
    final IArticleFilter filter = factory.getArticleFilter(FilterMode.MOTOR_TYRES_SEARCH);
    Assert.assertThat(filter, Matchers.notNullValue());
  }

  @Test
  public void givenBatterySearch_shouldReturnFilter() {
    final IArticleFilter filter = factory.getArticleFilter(FilterMode.BATTERY_SEARCH);
    Assert.assertThat(filter, Matchers.notNullValue());
  }
  
  @Test
  public void givenAccessorySearch_shouldReturnFilter() {
    final IArticleFilter filter = factory.getArticleFilter(FilterMode.ACCESSORY_LIST);
    Assert.assertThat(filter, Matchers.notNullValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void givenNullSearch_shouldThrowIllegalException() {
    factory.getArticleFilter(null);
  }
}
