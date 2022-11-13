package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.BaseTyreArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.tyres.TyreSearchQueryConverter;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class TyreSearchQueryConverterTest {

  @InjectMocks
  private TyreSearchQueryConverter converter;

  private BaseTyreArticleSearchCriteria criteria;

  private Set<String> gaIds;

  @Before
  public void setup() {
    criteria = new BaseTyreArticleSearchCriteria();
    gaIds = new HashSet<>();
  }

  @Test
  public void shouldConvert_WithEmptyValue() {
    BoolQueryBuilder queryBuilder = converter.apply(criteria, gaIds);
    Assert.assertThat(queryBuilder, Matchers.notNullValue());
    Assert.assertThat(queryBuilder.hasClauses(), Matchers.is(true));
  }

  @Test
  public void shouldConvert_WithFullValues() {
    gaIds.add("1991");
    criteria.setSupplier("KUMHO");
    criteria.setWidthCvp("201");
    criteria.setHeightCvp("10");
    criteria.setRadiusCvp("12");
    criteria.setSpeedIndexCvps(Arrays.asList("3"));
    criteria.setTyreSegmentCvps(Arrays.asList("4"));
    criteria.setRunflat(true);
    criteria.setSpike(true);
    criteria.setLoadIndexCvps(Arrays.asList("5"));

    BoolQueryBuilder queryBuilder = converter.apply(criteria, gaIds);
    Assert.assertThat(queryBuilder, Matchers.notNullValue());
    Assert.assertThat(queryBuilder.hasClauses(), Matchers.is(true));
  }
}
