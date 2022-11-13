package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.parser.OeAndArtNumStringParser;
import com.sagag.services.elasticsearch.query.articles.article.AggregationArticleListQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.AggregationOEAndArtNumQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.OEAndArtNumArticleQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class OEAndArtNumArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private OEAndArtNumArticleQueryBuilder builder;

  @Mock
  private ArticleIdFieldMapper articleIdFieldMapper;

  @Mock
  private AggregationOEAndArtNumQueryBuilder aggregationOEAndArtNumQueryBuilder;

  @Mock
  private OeAndArtNumStringParser strParser;

  @Mock
  private AggregationArticleListQueryBuilder articleListQuery;

  private KeywordArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS);
    Mockito.when(articleIdFieldMapper.getField()).thenReturn(ArticleField.ID_SAGSYS);
    Mockito.doCallRealMethod().when(strParser).apply(Mockito.anyString(), Mockito.any());
    Mockito.doCallRealMethod().when(strParser).applyParsers(Mockito.anyString(), Mockito.any());
  }

  @Test
  public void shouldBuildReferenceMatchQuery() {
    // Given
    criteria.setText("oc90");
    // When
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
    // Then
    Mockito.verify(articleIdFieldMapper, Mockito.times(0)).getField();
  }

  @Test
  public void shouldBuildDirectMatchQueryMultipleAggregation() {
    // Given
    final String freeText = "oc90";
    criteria.setText(freeText);
    criteria.setSupplierRaws(Collections.singletonList("KUMHO"));
    criteria.setGaIds(Collections.singletonList("1191"));
    criteria.onAggregated();
    criteria.setDirectMatch(true);
    // When
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
    // Then
    Mockito.verify(articleIdFieldMapper, Mockito.times(1)).getField();
  }

  @Test
  public void shouldBuildDirectMatchQuerySubAggregated() {
    // Given
    final String freeText = "oc90";
    criteria.setText(freeText);
    criteria.setSupplierRaws(Collections.singletonList("KUMHO"));
    criteria.setGaIds(Collections.singletonList("1191"));
    criteria.onAggregated();
    criteria.setDirectMatch(true);
    criteria.setUseMultipleAggregation(true);
    // When
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
    // Then
    Mockito.verify(articleIdFieldMapper, Mockito.times(1)).getField();
  }

  @Test
  public void shouldBuildDirectMatchQuerySuccessfully() {
    // Given
    final String freeText = "oc90";
    criteria.setText(freeText);
    criteria.setSupplierRaws(Collections.singletonList("KUMHO"));
    criteria.setGaIds(Collections.singletonList("1191"));
    criteria.onAggregated();
    criteria.setDirectMatch(true);
    criteria.setUseMultipleAggregation(true);
    criteria.setNeedSubAggregated(true);
    // When
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
    // Then
    Mockito.verify(articleIdFieldMapper, Mockito.times(1)).getField();
  }
}
