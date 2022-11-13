package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.BulbArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.bulbs.BulbArticleQueryBuilder;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class BulbArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private BulbArticleQueryBuilder builder;

  private BulbArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = BulbArticleSearchCriteria.builder().build();
  }

  @Test
  public void shouldBuildQuery_WithEmptyValue() {
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_WithFullValues() {
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    final List<String> samples = Arrays.asList("1");
    criteria.setVoltages(samples);
    criteria.setWatts(samples);
    criteria.setCodes(samples);
    criteria.setSuppliers(samples);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }
}
