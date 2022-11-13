package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.common.utils.SagBeanUtils;
import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.criteria.article.ReferenceAndArtNumSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.article.AggregationArticleListQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.AggregationFreeTextQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.AggregationOEAndArtNumQueryBuilder;
import com.sagag.services.elasticsearch.query.articles.article.GenArtIdAndReferenceArticleQueryBuilder;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class GenArtIdAndReferenceArticleQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private GenArtIdAndReferenceArticleQueryBuilder builder;

  @Mock
  private AggregationArticleListQueryBuilder articleListQuery;

  @Mock
  private AggregationFreeTextQueryBuilder freeTextQuery;

  @Mock
  private AggregationOEAndArtNumQueryBuilder aggregationOEAndArtNumQueryBuilder;

  private ReferenceAndArtNumSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = SagBeanUtils.map(new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS),
      ReferenceAndArtNumSearchCriteria.class);
  }

  @Test
  public void testWithExample_714098190219_ArtNum() {
    criteria.setArticleNrs(Arrays.asList("714098190219"));
    builder.buildQuery(criteria, Pageable.unpaged());
  }

  @Test
  public void testWithExample_714098190219_Freetext() {
    criteria.setArticleNrs(Arrays.asList("714098190219"));
    criteria.setFromFreetextSearch(true);
    builder.buildQuery(criteria, Pageable.unpaged());
  }
}
