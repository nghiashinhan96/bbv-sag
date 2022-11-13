package com.sagag.services.elasticsearch.query.articles.article.freetext;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.parser.ArticleFreetextStringParser;
import com.sagag.services.elasticsearch.parser.FreetextStringParser;
import com.sagag.services.elasticsearch.query.articles.AbstractArticleQueryBuilderTest;
import com.sagag.services.elasticsearch.query.articles.article.AggregationArticleListQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.AggregationFreeTextQueryBuilder;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FreetextArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  private static final IFreetextFields DF_FREE_TEXT_FIELDS = new IFreetextFields() { };

  @InjectMocks
  private FreetextArticleQueryBuilder builder;
  @Mock
  private AggregationArticleListQueryBuilder articleListQuery;
  @Mock
  private AggregationFreeTextQueryBuilder aggBuilder2;
  @Mock
  private IFreetextFields freetextFields;

  @Mock
  private ArticleFreetextStringParser articleFreetextStringParser;

  @Mock
  private FreetextStringParser freetextStringParser;

  private KeywordArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS);
    Mockito.when(freetextFields.refsFields()).thenReturn(DF_FREE_TEXT_FIELDS.refsFields());
    Mockito.when(freetextFields.criteriaFields()).thenReturn(DF_FREE_TEXT_FIELDS.criteriaFields());
    Mockito.when(freetextFields.partFields()).thenReturn(DF_FREE_TEXT_FIELDS.partFields());
    Mockito.when(freetextFields.productInfoTextFields())
        .thenReturn(DF_FREE_TEXT_FIELDS.productInfoTextFields());
    Mockito.when(freetextFields.attributesBoost())
        .thenReturn(DF_FREE_TEXT_FIELDS.attributesBoost());
    Mockito.when(freetextFields.refFieldsFullText())
        .thenReturn(DF_FREE_TEXT_FIELDS.refFieldsFullText());
  }

  @Test
  public void shouldBuildQuery_With_audi() {
    final String freeText = "audi";
    criteria.setText(freeText);
    Mockito.when(articleFreetextStringParser.apply(Mockito.anyString(), Mockito.any()))
      .thenReturn(freeText);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_TyreCode() {
    final String freeText = "195/65R15 91TLM-32 D16";
    criteria.setText(freeText);
    Mockito.when(articleFreetextStringParser.apply(Mockito.anyString(), Mockito.any()))
      .thenReturn(freeText);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_TyreCodeLowerCase() {
    final String freeText = "195/65r15 91tlm-32 d16";
    criteria.setText(freeText);
    Mockito.when(articleFreetextStringParser.apply(Mockito.anyString(), Mockito.any()))
      .thenReturn(freeText);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_audi_suppliers_And_gaids() {
    final String freeText = "audi";
    criteria.setText(freeText);
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    criteria.setGaIds(Arrays.asList("1191"));
    criteria.onAggregated();
    criteria.setUseMultipleAggregation(false);
    Mockito.when(articleFreetextStringParser.apply(Mockito.anyString(), Mockito.any()))
      .thenReturn(freeText);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_audi_suppliers_And_gaids_multiple_aggregation() {
    final String freeText = "audi";
    criteria.setText(freeText);
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    criteria.setGaIds(Arrays.asList("1191"));
    criteria.onAggregated();
    criteria.setUseMultipleAggregation(true);
    Mockito.when(articleFreetextStringParser.apply(Mockito.anyString(), Mockito.any()))
      .thenReturn(freeText);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuerySuccessfully() {
    // Given
    final String freeText = "audi";
    criteria.setText(freeText);
    criteria.setSupplierRaws(Collections.singletonList("KUMHO"));
    criteria.setGaIds(Collections.singletonList("1191"));
    criteria.onAggregated();
    criteria.setUseMultipleAggregation(true);
    criteria.setNeedSubAggregated(true);
    Mockito.when(articleFreetextStringParser.apply(Mockito.anyString(), Mockito.any()))
        .thenReturn(freeText);
    // When
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
    // Then
    verify(articleFreetextStringParser).apply(any(), any());
  }

  @Test
  public void shouldBuildQueryWithDirectMatch() {
    // Given
    final String freeText = "audi";
    criteria.setText(freeText);
    criteria.setPerfectMatched(true);
    Mockito.when(articleFreetextStringParser.apply(Mockito.anyString(), Mockito.any()))
        .thenReturn(freeText);
    // When
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
    // Then
    verify(articleFreetextStringParser).apply(any(), any());
  }
}
