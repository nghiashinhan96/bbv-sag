package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.OilArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.oils.OilArticleQueryBuilder;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class OilArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private OilArticleQueryBuilder builder;

  private OilArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = OilArticleSearchCriteria.builder().build();
  }

  @Test
  public void shouldBuildQuery_WithEmptyValue() {
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_WithFullValues() {
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    final List<String> samples = Arrays.asList("1");
    criteria.setVehicles(samples);
    criteria.setAggregates(samples);
    criteria.setViscosities(samples);
    criteria.setBottleSizes(samples);
    criteria.setSpecifications(samples);
    criteria.setApproved(samples);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }
}
