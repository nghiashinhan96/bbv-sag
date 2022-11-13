package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.MatchCodeArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.tyres.MatchCodeTyreArticleQueryBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class MatchCodeTyreArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private MatchCodeTyreArticleQueryBuilder builder;

  private MatchCodeArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new MatchCodeArticleSearchCriteria();
    criteria.setMatchCode("1000001");
  }

  @Test
  public void shouldBuildQuery_WithEmptyValue() {
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_WithArtDisplay() {
    criteria.setMatchCode("205/55R16 91T UG 9+ MS");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_WithTyreCode() {
    criteria.setMatchCode("PW2055516NTNGOODYEAR");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_WithFullValues() {
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    criteria.setGaIds(Arrays.asList("1"));
    criteria.onAggregated();
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }
}
