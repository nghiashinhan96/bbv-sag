package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.parser.ArticleFreetextStringParser;
import com.sagag.services.elasticsearch.query.articles.article.AllDataArticleQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class AllDataArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private AllDataArticleQueryBuilder builder;
  @Mock
  private ArticleFreetextStringParser freetextStringParser;

  private KeywordArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS);
  }

  @Test
  public void shouldBuildQuery_With_audi() {
    final String freeText = "audi";
    criteria.setText(freeText);
    builder.buildQuery(criteria, Pageable.unpaged());
  }

  @Test
  public void shouldBuildQuery_With_PartCode() {
    final String freeText = "1J0 411 105 CD";
    criteria.setText(freeText);
    builder.buildQuery(criteria, Pageable.unpaged());
  }

  @Test
  public void shouldBuildQuery_With_ArtNrStr() {
    final String freeText = "714098190219";
    criteria.setText(freeText);
    builder.buildQuery(criteria, Pageable.unpaged());
  }

  @Test
  public void shouldBuildQuery_With_ArtNrRandomStr() {
    final String freeText = "714098190219dsfsadfsadfsadf";
    criteria.setText(freeText);
    builder.buildQuery(criteria, Pageable.unpaged());
  }

  @Test
  public void shouldBuildQuery_With_ArtNrRandomStrContainOperator() {
    final String freeText = "714098190219dsandfsoradfsadfsadf";
    criteria.setText(freeText);
    builder.buildQuery(criteria, Pageable.unpaged());
  }

  @Test
  public void shouldBuildQuery_With_ArtNrRandomStrContainOperator_2() {
    final String freeText = "714098190219 and dsandfsoradfsadfs or adf";
    criteria.setText(freeText);
    builder.buildQuery(criteria, Pageable.unpaged());
  }
}
