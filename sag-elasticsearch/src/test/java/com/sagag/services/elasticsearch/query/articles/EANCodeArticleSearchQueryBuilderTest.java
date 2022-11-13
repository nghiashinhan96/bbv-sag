package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.article.parts.EanArticleSearchQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class EANCodeArticleSearchQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private EanArticleSearchQueryBuilder builder;

  private KeywordArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllArgEx_WithEmptyEANCode() {
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_EANCode_12Digits() {
    criteria.setText("123456789123");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_EANCode_12Digits_With_Suppliers() {
    criteria.setText("123456789123");
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_EANCode_13Digits() {
    criteria.setText("1234567891234");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_EANCode_13Digits_With_Suppliers() {
    criteria.setText("1234567891234");
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

}
