package com.sagag.services.elasticsearch.query.articles;

import com.sagag.services.elasticsearch.criteria.article.KeywordArticleSearchCriteria;
import com.sagag.services.elasticsearch.query.articles.article.ArticleNumberDeepLinkQueryBuilder;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class ArticleNumberDeepLinkQueryBuilderTest extends AbstractArticleQueryBuilderTest {

  @InjectMocks
  private ArticleNumberDeepLinkQueryBuilder builder;

  private KeywordArticleSearchCriteria criteria;

  @Before
  public void setup() {
    criteria = new KeywordArticleSearchCriteria(StringUtils.EMPTY, LOCKS);
  }

  @Test
  public void shouldBuildQuery_with_articleNr_5001100420() {
    criteria.setText("5001100420");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_with_articleNr_oc90() {
    criteria.setText("oc90");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }

  @Test
  public void shouldBuildQuery_with_articleId_1001595494() {
    criteria.setText("1001595494");
    builder.buildQuery(criteria, Pageable.unpaged(), INDEX);
  }
}
