package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.parser.OnlyArticleNrStringParser;
import com.sagag.services.elasticsearch.query.articles.article.ArtNrAndSupplierIdQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class ArtNrAndSupplierIdQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private ArtNrAndSupplierIdQueryBuilder builder;

  @Mock
  private OnlyArticleNrStringParser strParser;

  private KeywordArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS);
    Mockito.doCallRealMethod().when(strParser).apply(Mockito.anyString(), Mockito.any());
    Mockito.doCallRealMethod().when(strParser).applyParsers(Mockito.anyString(), Mockito.any());
  }

  @Test
  public void shouldBuildQuery_With_030_793_Dirko() {
    criteria.setText("030.793 Dirko");
    criteria.setIdDlnr("10");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
    Mockito.verify(strParser, Mockito.times(2)).applyParsers(Mockito.anyString(), Mockito.any());
  }


}
