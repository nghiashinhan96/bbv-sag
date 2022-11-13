package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.config.ArticleIdFieldMapper;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.enums.ArticleField;
import com.sagag.services.elasticsearch.query.articles.article.ArticleIdArticleSearchQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class ArticleIdArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private ArticleIdArticleSearchQueryBuilder builder;

  @Mock
  private ArticleIdFieldMapper articleIdFieldMapper;

  private KeywordArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS);
    Mockito.when(articleIdFieldMapper.getField()).thenReturn(ArticleField.ID_SAGSYS);
  }

  @Test
  public void shouldBuildQuery_With_1000001() {
    criteria.setText("1000001");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_With_oc90_suppliers_And_gaids() {
    final String freeText = "1000001";
    criteria.setText(freeText);
    criteria.setSupplierRaws(Arrays.asList("KUMHO"));
    criteria.setGaIds(Arrays.asList("1191"));
    criteria.onAggregated();
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

}
