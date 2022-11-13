package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.BatteryArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.batteries.BatteryArticleQueryBuilder;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class BatteryArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private BatteryArticleQueryBuilder builder;

  private BatteryArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = BatteryArticleSearchCriteria.builder().build();
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
    criteria.setAmpereHours(samples);
    criteria.setWidths(samples);
    criteria.setHeights(samples);
    criteria.setLengths(samples);
    criteria.setInterconnections(samples);
    criteria.setTypeOfPoles(samples);
    criteria.setWithoutStartStop(true);
    criteria.setWithStartStop(true);
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }
}
