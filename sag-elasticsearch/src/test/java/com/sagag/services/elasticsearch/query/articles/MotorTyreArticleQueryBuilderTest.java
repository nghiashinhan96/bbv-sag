package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.MotorTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.tyres.MotorTyreArticleQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.tyres.TyreSearchQueryConverter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class MotorTyreArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private MotorTyreArticleQueryBuilder builder;

  @Mock
  private TyreSearchQueryConverter queryConverter;

  private MotorTyreArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new MotorTyreArticleSearchCriteria();
  }

  @Test
  public void shouldBuildQuery_WithEmptyValue() {
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_WithFullValues() {
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    criteria.setGaIds(Arrays.asList("1"));
    criteria.setCategoryGenArtIds(Stream.of("123").collect(Collectors.toSet()));
    criteria.onAggregated();
    Mockito.when(queryConverter.apply(Mockito.any(), Mockito.any()))
      .thenReturn(QueryBuilders.boolQuery());

    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);

    Mockito.verify(queryConverter, Mockito.times(1)).apply(Mockito.any(), Mockito.any());
  }
}
